package main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import main.api.request.LoginRequest;
import main.api.request.RegisterRequest;
import main.api.request.RestoreRequest;
import main.api.request.change.ChangeDataMyProfile;
import main.api.request.change.ChangePasswordRequest;
import main.api.response.ResultResponse;
import main.api.response.StatisticsResponse;
import main.api.response.result.BadResultResponse;
import main.api.response.result.FalseResultResponse;
import main.api.response.result.LoginResultResponse;
import main.api.response.result.OkResultResponse;
import main.api.response.user.AllUserInformationResponse;
import main.api.response.user.UserWithPhotoResponse;
import main.config.MailConfig;
import main.model.User;
import main.model.repository.CaptchaCodeRepository;
import main.model.repository.GlobalSettingsRepository;
import main.model.repository.PostRepository;
import main.model.repository.UserRepository;
import main.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${user.email.valid_reg}")
    private String correctMail;
    @Value("${user.password.min_length}")
    private int minLengthPassword;
    @Value("${user.password.length_key}")
    private int lengthHashKey;
    @Value("${user.password.subject_string}")
    private String subjectString;
    @Value("${user.password.concat_string}")
    private String contactStringForMessage;
    @Value("${user.password.algorithm_string}")
    private String algorithmString;
    @Value("${user.upload_file.size}")
    private int maxSizeForFile;

    private char[] alphabetAndDigits = "ab1cd2ef3g45hij6klm7no8pq9rst0uvwxyz".toCharArray();

    private final MailConfig mailConfig;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ImageServiceImpl imageServiceImpl;
    private final GlobalSettingsRepository settingsRepository;
    private final CaptchaCodeRepository captchaCodeRepository;
    private final AuthenticationManager authenticationManager;

    public ResultResponse check(Principal principal) {
        if (principal == null) {
            return new FalseResultResponse();
        }
        User user = getCurrentUserByEmail(principal.getName());
        return new LoginResultResponse(getAllUserInformation(user));
    }

    public ResultResponse login(LoginRequest req) {
        User userRepo = getCurrentUserByEmail(req.getEmail());
        String checkHashPassword = generateHashPassword(req.getPassword());
        if (userRepo.getPassword().equals(checkHashPassword)) {
            Authentication auth = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    req.getEmail(),
                                    req.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(auth);
            org.springframework.security.core.userdetails.User user =
                    (org.springframework.security.core.userdetails.User) auth.getPrincipal();

            User currentUser = getCurrentUserByEmail(user.getUsername());

            return new LoginResultResponse(getAllUserInformation(currentUser));
        } else {
            return new FalseResultResponse();
        }
    }

    public ResultResponse logout(Principal principal) {
        if (principal == null) {
            log.warn("User isn't authorized");
            return new FalseResultResponse();
        }

        SecurityContextHolder.clearContext();

        return new OkResultResponse();
    }

    public ResultResponse myStatistics(Principal principal) {
        User user = getCurrentUserByEmail(principal.getName());
        int userId = user.getId();
        int postsCount = postRepository.countPostStatus(userId, "ACCEPTED").orElse(0);
        int likesCount = postRepository.countLikesMyPosts(userId).orElse(0);
        int dislikeCount = postRepository.countDislikesMyPosts(userId).orElse(0);
        int viewsCount = postRepository.countViewsMyPosts(userId).orElse(0);
        LocalDateTime time = postRepository.timeMyFirstPublication(userId);
        long timeFirstPublication = 0;
        if (time != null) {
            timeFirstPublication = time.atZone(ZoneId.systemDefault()).toEpochSecond();
        }
        log.info("Available my statistics");
        return new StatisticsResponse(postsCount, likesCount, dislikeCount, viewsCount, timeFirstPublication);
    }

    public ResultResponse allStatistics(Principal principal) {
        User user = getCurrentUserByEmail(principal.getName());
        boolean statisticsIsPublic = settingsRepository.codeAndValue("STATISTICS_IS_PUBLIC", "NO") > 0;
        boolean isModerator = user == null || user.getIsModerator() == 0;
        if (statisticsIsPublic && isModerator) {
            log.info("Banned public display of statistics or user is not a moderator");
        }
        int postsCount = postRepository.countPostForModStatusAccepted().orElse(0);
        int allLikesCount = postRepository.countLikesAllPosts().orElse(0);
        int allDislikeCount = postRepository.countDislikesAllPosts().orElse(0);
        int allViewsCount = postRepository.countViewsAllPosts().orElse(0);
        LocalDateTime time = postRepository.timeFirstPublication();
        long timeFirstPublication = time.atZone(ZoneId.systemDefault()).toEpochSecond();
        log.info("Available ALL statistics");
        return new StatisticsResponse(postsCount, allLikesCount, allDislikeCount, allViewsCount, timeFirstPublication);
    }

    public ResultResponse restorePassword(RestoreRequest req) {
        User user = getCurrentUserByEmail(req.getEmail());

        boolean inCorrectMail = !req.getEmail().matches(correctMail);
        boolean userNotExist = user == null;

        if (inCorrectMail || userNotExist) {
            log.info("Incorrect email or user don't exist");
            return new FalseResultResponse();
        } else {
            String codeForRecovery = generateHashCode();
            user.setCode(codeForRecovery);
            userRepository.save(user);
            SimpleMailMessage mimeMessage = new SimpleMailMessage();
            mimeMessage.setTo(req.getEmail());
            mimeMessage.setSubject(subjectString);
            mimeMessage.setText(contactStringForMessage + codeForRecovery);
            JavaMailSender javaMailSender = mailConfig.javaMailSender();
            javaMailSender.send(mimeMessage);
            log.info("Sent message with recovery code");
            return new OkResultResponse();
        }
    }

    public ResultResponse changePassword(ChangePasswordRequest req) {
        BadResultResponse badResultResponse = new BadResultResponse();

        User user = userRepository.findByCode(req.getCode())
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                String.format("user with code %s not found", req.getCode())));

        if (req.getCode().isBlank()) {
            badResultResponse.addError("code", "Ссылка для восстановления пароля устарела." +
                    "<a href=\"/auth/restore\">Запросить ссылку снова</a>");
        }

        if (isPasswordValid(req.getPassword())) {
            badResultResponse.addError("password", "Слишком короткий пароль");
        }

        if (isCaptchaSecretValid(req.getCaptcha(), req.getCaptchaSecret())) {
            badResultResponse.addError("captcha", "Код с картинки введён неверно");
        }

        if (badResultResponse.getErrors().size() > 0) {
            return badResultResponse;
        } else {
            user.setPassword(generateHashPassword(req.getPassword()));
            userRepository.save(user);
            return new OkResultResponse();
        }
    }

    public ResultResponse registerUser(RegisterRequest req) {
        BadResultResponse badResultResponse = new BadResultResponse();

        if (isUserExist(req.getEmail())) {
            badResultResponse.addError("email", "Этот e-mail уже зарегистрирован");
        }

        if (isNameBlank(req.getName())) {
            badResultResponse.addError("name", "Имя указано неверно");
        }

        if (isPasswordValid(req.getPassword())) {
            badResultResponse.addError("password", "Слишком короткий пароль");
        }

        if (isCaptchaSecretValid(req.getCaptcha(), req.getCaptchaSecret())) {
            badResultResponse.addError("captcha", "Код с картинки введён неверно");
        }

        if (badResultResponse.getErrors().size() > 0 || badResultResponse.getErrors() == null) {
            return badResultResponse;
        } else {
            String newHashPassword = generateHashPassword(req.getPassword());
            User user = new User();
            user.setName(req.getName());
            user.setEmail(req.getEmail());
            user.setPassword(newHashPassword);
            user.setRegTime(LocalDateTime.now());
            userRepository.save(user);
            return new OkResultResponse();
        }
    }

    public ResultResponse editMyProfile(ChangeDataMyProfile change, Principal principal) {
        BadResultResponse badResultResponse = new BadResultResponse();

        User user = getCurrentUserByEmail(principal.getName());

        String nameUserHttpSession = user.getName();
        String emailUserHttpSession = user.getEmail();

        boolean trueEmail = change.getEmail().matches(correctMail);
        boolean existEmail = userRepository.existEmailOrNot(emailUserHttpSession) != null;
        boolean notEqualsEmail = !change.getEmail().equals(emailUserHttpSession);
        boolean nameTrue = !change.getName().isBlank() && !change.getName().equals(nameUserHttpSession);

        if (nameTrue && trueEmail && existEmail && notEqualsEmail) {
            user.setName(change.getName());
            user.setEmail(change.getEmail());
        } else {
            badResultResponse.addError("name", "Имя указано неверно");
            badResultResponse.addError("email", "Этот e-mail уже зарегистрирован");
        }

        if (change.getPassword() != null) {
            if (isPasswordValid(change.getPassword())) {
                user.setPassword(generateHashPassword(change.getPassword()));
            } else {
                badResultResponse.addError("password", "Слишком короткий пароль");
            }
        }

        if (change.getRemovePhoto() != null && change.getPhoto() != null) {
            if (change.getRemovePhoto() == 0 && change.getPhoto().getSize() < maxSizeForFile) {
                imageServiceImpl.deletePhoto(user.getPhoto());
                user.setPhoto(imageServiceImpl.resizeImage(change.getPhoto()));
            } else {
                badResultResponse.addError("photo", "Фото слишком большое, нужно не более 5 Мб");
            }
            if (change.getRemovePhoto() == 1 && change.getPhoto().getSize() == 0) {
                imageServiceImpl.deletePhoto(user.getPhoto());
            }
        }

        userRepository.save(user);

        if (badResultResponse.getErrors().size() > 0) {
            return badResultResponse;
        } else {
            return new OkResultResponse();
        }
    }

    private String generateHashCode() {
        return IntStream.range(0, lengthHashKey).map(i -> (int) (Math.random() * (alphabetAndDigits.length - 1)))
                .mapToObj(a -> String.valueOf(alphabetAndDigits[a])).collect(Collectors.joining());
    }

    private String generateHashPassword(String password) {
        if (password == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithmString);
            messageDigest.update(password.getBytes());
            byte[] digest = messageDigest.digest();
            String myHash = DatatypeConverter.printHexBinary(digest);
            return myHash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private AllUserInformationResponse getAllUserInformation(User user) {
        UserWithPhotoResponse userWithPhotoResponse =
                new UserWithPhotoResponse(
                        user.getId(),
                        user.getName(),
                        user.getPhoto());
        AllUserInformationResponse allUserInformationResponse =
                new AllUserInformationResponse(
                        userWithPhotoResponse,
                        user.getEmail(),
                        user.userModerator(),
                        user.userModerator() ? postRepository
                                .countPostsUserForModerationStatusNew(user.getId()) : 0,
                        user.userModerator()
                );
        return allUserInformationResponse;
    }

    private Boolean isUserExist(String email) {
        return userRepository.getByEmail(email).isPresent() && !email.matches(correctMail);
    }

    private Boolean isNameBlank(String name) {
        return name.isBlank();
    }

    private Boolean isPasswordValid(String password) {
        return password.isBlank() && password.length() < minLengthPassword;
    }

    private Boolean isCaptchaSecretValid(String captcha, String captchaSecret) {
        return captcha.isBlank() || captchaSecret.isBlank() || !captchaCodeRepository.getCaptchaCodeBySecretCode(captchaSecret).equals(captcha);
    }

    public User getCurrentUserByEmail(String email) {
        User currentUser = userRepository
                .getByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("user with email %s not found", email)));
        return currentUser;
    }
}