package main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import main.api.request.LoginRequest;
import main.api.request.RegisterRequest;
import main.api.request.RestoreRequest;
import main.api.request.change.ChangePasswordRequest;
import main.api.request.change.ChangeWithPassword;
import main.api.response.StatisticsResponse;
import main.api.response.result.BadResultResponse;
import main.api.response.result.LoginResultResponse;
import main.api.response.result.OkResultResponse;
import main.api.response.user.AllUserInformation;
import main.api.response.user.UserWithPhoto;
import main.config.MailConfig;
import main.model.User;
import main.repository.CaptchaCodeRepository;
import main.repository.GlobalSettingsRepository;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {
    private Map<String, Integer> sessionIdAndUserId = new HashMap<>();
    private char[] alphabetAndDigits = "ab1cd2ef3g45hij6klm7no8pq9rst0uvwxyz".toCharArray();
    private String correctMail = "[\\w\\s\\d]@([\\w\\s\\d\\u002E])((ru)|(com))";

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostService postService;
    private final GlobalSettingsRepository settingsRepository;
    private final MailConfig mailConfig;
    private final CaptchaCodeRepository captchaCodeRepository;
    private final ImageService imageService;

    public LoginResultResponse check(HttpSession httpSession) {
        String sessionId = httpSession.getId();
        if (!sessionIdAndUserId.containsKey(sessionId)) {
            log.info("User with session id {} not authenticated", sessionId);
            return new LoginResultResponse(false);
        } else {
            Integer userId = sessionIdAndUserId.get(sessionId);
            User user = userRepository.findById(userId).get();
            return new LoginResultResponse(true, new AllUserInformation(new UserWithPhoto(user.getId(), user.getName(),
                    user.getPhoto()), user.getEmail(), itTrue(user), postService.countForModeration(), itTrue(user)
            ));
        }
    }

    public LoginResultResponse login(LoginRequest loginRequest, HttpSession httpSession) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        User user = userRepository.getByEmail(email);
        if (user == null) {
            log.info("User doesn't exist");
            return new LoginResultResponse(false);
        }
        int hashPassReq = password.hashCode();
        int hashPassUser = user.getPassword().hashCode();
        if ((hashPassUser != hashPassReq) && (user == null)) {
            log.info("Login not correct");
            return new LoginResultResponse(false);
        }
        log.info("Email and password correct");
        sessionIdAndUserId.put(httpSession.getId(), user.getId());
        return new LoginResultResponse(true, new AllUserInformation(new UserWithPhoto(user.getId(), user.getName(),
                user.getPhoto()), user.getEmail(), itTrue(user), postService.countForModeration(), itTrue(user)
        ));
    }

    public OkResultResponse logout(HttpSession httpSession) {
        String sessionId = httpSession.getId();
        if (!sessionIdAndUserId.containsKey(sessionId)) {
            log.info("User isn't authorized");
            return new OkResultResponse(true);
        }
        sessionIdAndUserId.remove(sessionId);
        return new OkResultResponse(true);
    }

    public StatisticsResponse myStatistics(HttpSession httpSession) {
        String sessionId = httpSession.getId();
        if (!sessionIdAndUserId.containsKey(sessionId)) {
            log.info("User isn't authorized");
        }
        Integer userSessionId = sessionIdAndUserId.get(sessionId);
        User user = userRepository.findById(userSessionId).get();
        int userId = user.getId();
        int postsCount = postRepository.countPostStatus(userId, "ACCEPTED");
        int likesCount = postRepository.countLikesMyPosts(userId);
        int dislikeCount = postRepository.countDislikesMyPosts(userId);
        int viewsCount = postRepository.countViewsMyPosts(userId);
        LocalDateTime time = postRepository.timeMyFirstPublication(userId);
        long timeFirstPublication = time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        log.info("Available my statistics");
        return new StatisticsResponse(postsCount, likesCount, dislikeCount, viewsCount, timeFirstPublication);
    }

    public StatisticsResponse allStatistics(HttpSession httpSession) {
        String sessionId = httpSession.getId();
        if (!sessionIdAndUserId.containsKey(sessionId)) {
            log.info("User isn't authorized");
        }
        Integer userSessionId = sessionIdAndUserId.get(sessionId);
        User user = userRepository.findById(userSessionId).get();
        boolean statisticsIsPublic = settingsRepository.codeAndValue("STATISTICS_IS_PUBLIC", "NO");
        boolean isModerator = user.getIsModerator() == 0;
        if (statisticsIsPublic && isModerator) {
            log.info("Banned public display of statistics or user is not a moderator");
        }
        int postsCount = postRepository.countPostForModStatusAccepted();
        int allLikesCount = postRepository.countLikesAllPosts();
        int allDislikeCount = postRepository.countDislikesAllPosts();
        int allViewsCount = postRepository.countViewsAllPosts();
        LocalDateTime time = postRepository.timeFirstPublication();
        long timeFirstPublication = time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        log.info("Available ALL statistics");
        return new StatisticsResponse(postsCount, allLikesCount, allDislikeCount, allViewsCount, timeFirstPublication);
    }

    public OkResultResponse restorePassword(RestoreRequest restoreRequest) {
        String email = restoreRequest.getEmail();
        if (!email.matches(correctMail)) {
            log.info("Incorrect email");
            return new OkResultResponse(false);
        }
        User user = userRepository.getByEmail(email);
        if (user == null) {
            log.info("User doesn't exist");
            return new OkResultResponse(false);
        }
        String codeForRecovery = generateHashCode();
        user.setCode(codeForRecovery);
        userRepository.save(user);
        SimpleMailMessage mimeMessage = new SimpleMailMessage();
        mimeMessage.setTo(email);
        mimeMessage.setText("/login/change-password/" + codeForRecovery);
        JavaMailSender javaMailSender = mailConfig.javaMailSender();
        javaMailSender.send(mimeMessage);
        log.info("Sent message with recovery code");
        return new OkResultResponse(true);
    }

    public BadResultResponse changePassword(ChangePasswordRequest changePasswordRequest) {
        String code = changePasswordRequest.getCode();
        String password = changePasswordRequest.getPassword();
        String captcha = changePasswordRequest.getCaptcha();
        String captchaSecret = changePasswordRequest.getCaptchaSecret();
        if (code.isBlank() || password.isBlank() || (password.length() < 6)
                || captcha.isBlank() || captchaSecret.isBlank()) {
            log.info("Incorrect data");
            return new BadResultResponse(false);
        }
        User user = userRepository.findByCode(code);
        String captchaCode = captchaCodeRepository.getCaptchaCodeBySecretCode(captchaSecret);
        if (!captchaCode.equals(code) || user == null) {
            log.info("CaptchaCode or user doesn't exist");
            return new BadResultResponse(false);
        }
        log.info("Message");
        String newHashPassword = generateHashPassword(password);
        user.setPassword(newHashPassword);
        userRepository.save(user);
        return new BadResultResponse(true);
    }

    public BadResultResponse registerUser(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();
        String name = registerRequest.getName();
        String captcha = registerRequest.getCaptcha();
        String captchaSecret = registerRequest.getCaptchaSecret();
        boolean emailFalse = !email.matches(correctMail) && userRepository.getByEmail(email) == null;
        boolean passwordFalse = password.isBlank() && password.length() < 6;
        boolean nameFalse = name.isBlank();
        boolean captchaFalse = captcha.isBlank();
        boolean captchaSecretFalse = !captchaCodeRepository.getCaptchaCodeBySecretCode(captchaSecret).equals(captcha);
        if (emailFalse || passwordFalse || nameFalse || captchaFalse || captchaSecretFalse) {
            log.info("Incorrect data");
            return new BadResultResponse(false);
        }
        log.info("Message");
        String newHashPassword = generateHashPassword(password);
        User user = new User(LocalDateTime.now(), email, newHashPassword, name);
        userRepository.save(user);
        return new BadResultResponse(true);
    }

    public BadResultResponse editMyProfile(ChangeWithPassword changeWithPassword, HttpSession httpSession) {
        String sessionId = httpSession.getId();
        if (!sessionIdAndUserId.containsKey(sessionId)) {
            log.info("User isn't authorized");
        }
        Integer userSessionId = sessionIdAndUserId.get(sessionId);
        User user = userRepository.findById(userSessionId).get();
        String name = changeWithPassword.getName();
        String email = changeWithPassword.getEmail();
        String password = changeWithPassword.getPassword();
        MultipartFile photo = changeWithPassword.getPhoto();
        Byte removePhoto = changeWithPassword.getRemovePhoto();
        String nameUserHttpSession = user.getName();
        String emailUserHttpSession = user.getEmail();
        boolean correctEmail = email.matches(correctMail) && userRepository.getByEmail(email) != null &&
                !email.equals(emailUserHttpSession);
        boolean nameFalse = !name.isBlank() && !name.equals(nameUserHttpSession);
        boolean passwordFalse = !password.isBlank() && password.length() > 6;
        boolean photoFalse = !photo.isEmpty();
        boolean remove = removePhoto == 0;
        if (nameFalse && correctEmail) {
            user.setName(name);
            user.setEmail(email);
            if (passwordFalse) {
                String newPasswordUserHttpSession = generateHashPassword(password);
                user.setPassword(newPasswordUserHttpSession);
                if (remove && photoFalse) {
                    String pathForImage = imageService.generateDirectoryAndName();
                    String pathAfterResize = imageService.resizeImage(photo, pathForImage);

                    user.setPhoto(pathAfterResize);
                    return new BadResultResponse(true);
                }
            }
            if (!remove && !photoFalse) {
                String pathForDeleteImage = imageService.generateDirectoryAndName();
                boolean resultDeletion = imageService.deletePhoto(pathForDeleteImage);
                return new BadResultResponse(resultDeletion);
            }
            userRepository.save(user);
            return new BadResultResponse(true);
        }
        return new BadResultResponse(false);
    }

    private boolean itTrue(User user) {
        return user.getIsModerator() == 1;
    }

    private String generateHashCode() {
        return IntStream.range(0, 55).map(i -> (int) (Math.random() * (alphabetAndDigits.length - 1)))
                .mapToObj(a -> String.valueOf(alphabetAndDigits[a])).collect(Collectors.joining());
    }

    private String generateHashPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes());
            byte[] digest = messageDigest.digest();
            String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
            return myHash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}