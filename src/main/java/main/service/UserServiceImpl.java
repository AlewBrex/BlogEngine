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
import org.springframework.web.multipart.MultipartFile;

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
        User user = getCurrentUser(principal.getName());
        return new LoginResultResponse(getAllUserInformation(user));
    }

    public ResultResponse login(LoginRequest loginRequest) {
        Authentication auth = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getEmail(),
                                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) auth.getPrincipal();
        User currentUser = getCurrentUser(user.getUsername());
        return new LoginResultResponse(getAllUserInformation(currentUser));
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
        User user = getCurrentUser(principal.getName());
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
        User user = getCurrentUser(principal.getName());
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

    public ResultResponse restorePassword(RestoreRequest restoreRequest) {
        String email = restoreRequest.getEmail();
        User user = userRepository.getByEmail(email).get();
        boolean inCorrectMail = !email.matches(correctMail);
        boolean userNotExist = user == null;
        if (inCorrectMail || userNotExist) {
            log.info("Incorrect email or user don't exist");
            return new FalseResultResponse();
        } else {
            String codeForRecovery = generateHashCode();
            user.setCode(codeForRecovery);
            userRepository.save(user);
            SimpleMailMessage mimeMessage = new SimpleMailMessage();
            mimeMessage.setTo(email);
            mimeMessage.setSubject(subjectString);
            mimeMessage.setText(contactStringForMessage + codeForRecovery);
            JavaMailSender javaMailSender = mailConfig.javaMailSender();
            javaMailSender.send(mimeMessage);
            log.info("Sent message with recovery code");
            return new OkResultResponse();
        }
    }

    public ResultResponse changePassword(ChangePasswordRequest changePasswordRequest) {
        BadResultResponse badResultResponse = new BadResultResponse();
        String code = changePasswordRequest.getCode();
        String password = changePasswordRequest.getPassword();
        String captcha = changePasswordRequest.getCaptcha();
        String captchaSecret = changePasswordRequest.getCaptchaSecret();
        User user = userRepository.findByCode(code);
        String captchaCode = captchaCodeRepository.getCaptchaCodeBySecretCode(captchaSecret);
        boolean inCorrectCode = code.isBlank();
        boolean inCorrectPassword = password.isBlank() || (password.length() < minLengthPassword);
        boolean inCorrectCaptchaCode = captcha.isBlank() || !captchaCode.equals(code) || captchaSecret.isBlank();
        if (inCorrectCode) {
            badResultResponse.addError("code", "Ссылка для восстановления пароля устарела." +
                    "<a href=\"/auth/restore\">Запросить ссылку снова</a>");
        }
        if (inCorrectPassword) {
            badResultResponse.addError("password", "Слишком короткий пароль");
        }
        if (inCorrectCaptchaCode) {
            badResultResponse.addError("captcha", "Код с картинки введён неверно");
        }
        if (badResultResponse.getErrors().size() > 0) {
            return badResultResponse;
        } else {
            String newHashPassword = generateHashPassword(password);
            user.setPassword(newHashPassword);
            userRepository.save(user);
            return new OkResultResponse();
        }
    }

    public ResultResponse registerUser(RegisterRequest registerRequest) {
        BadResultResponse badResultResponse = new BadResultResponse();
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();
        String name = registerRequest.getName();
        String captcha = registerRequest.getCaptcha();
        String captchaSecret = registerRequest.getCaptchaSecret();
        boolean emailFalse = !email.matches(correctMail) && userRepository.getByEmail(email) != null;
        boolean passwordFalse = password.isBlank() && password.length() < minLengthPassword;
        boolean nameFalse = name.isBlank();
        boolean captchaFalse = captcha.isBlank();
        boolean captchaSecretFalse = !captchaCodeRepository.getCaptchaCodeBySecretCode(captchaSecret).equals(captcha);
        if (emailFalse) {
            badResultResponse.addError("email", "Этот e-mail уже зарегистрирован");
        }
        if (nameFalse) {
            badResultResponse.addError("name", "Имя указано неверно");
        }
        if (passwordFalse) {
            badResultResponse.addError("password", "Слишком короткий пароль");
        }
        if (captchaFalse || captchaSecretFalse) {
            badResultResponse.addError("captcha", "Код с картинки введён неверно");
        }
        if (badResultResponse.getErrors().size() > 0 || badResultResponse.getErrors() == null) {
            return badResultResponse;
        } else {
            String newHashPassword = generateHashPassword(password);
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(newHashPassword);
            user.setRegTime(LocalDateTime.now());
            userRepository.save(user);
            return new OkResultResponse();
        }
    }

    public ResultResponse editMyProfile(ChangeDataMyProfile changeDataMyProfile, Principal principal) {
        BadResultResponse badResultResponse = new BadResultResponse();
        String name = changeDataMyProfile.getName();
        String email = changeDataMyProfile.getEmail();
        String password = changeDataMyProfile.getPassword();
        Byte removePhoto = changeDataMyProfile.getRemovePhoto();
        MultipartFile multipartFile = changeDataMyProfile.getPhoto();
        User user = getCurrentUser(principal.getName());
        String nameUserHttpSession = user.getName();
        String emailUserHttpSession = user.getEmail();
        boolean trueEmail = email.matches(correctMail);
        boolean existEmail = userRepository.existEmailOrNot(emailUserHttpSession) != null;
        boolean notEqualsEmail = !email.equals(emailUserHttpSession);
        boolean nameTrue = !name.isBlank() && !name.equals(nameUserHttpSession);

        if (nameTrue && trueEmail && existEmail && notEqualsEmail) {
            user.setName(name);
            user.setEmail(email);
        } else {
            badResultResponse.addError("name", "Имя указано неверно");
            badResultResponse.addError("email", "Этот e-mail уже зарегистрирован");
        }
        if (password != null) {
            if (password.length() > minLengthPassword) {
                String newPasswordUserHttpSession = generateHashPassword(password);
                user.setPassword(newPasswordUserHttpSession);
            } else {
                badResultResponse.addError("password", "Слишком короткий пароль");
            }
        }
        if (removePhoto != null && multipartFile != null) {
            if (removePhoto == 0 && multipartFile.getSize() < maxSizeForFile) {
                imageServiceImpl.deletePhoto(user.getPhoto());
                String pathForSetUser = imageServiceImpl.resizeImage(multipartFile);
                user.setPhoto(pathForSetUser);
            } else {
                badResultResponse.addError("photo", "Фото слишком большое, нужно не более 5 Мб");
            }
            if (removePhoto == 1 && multipartFile.getSize() == 0) {
                String pathForDeleteImage = user.getPhoto();
                imageServiceImpl.deletePhoto(pathForDeleteImage);
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

    public User getCurrentUser(String email) {
        User currentUser = userRepository
                .getByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        return currentUser;
    }
}