package main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import main.Main;
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
import main.config.MailConfig;
import main.exception.ContentNotAllowedException;
import main.exception.LoginUserWrongCredentialsException;
import main.exception.WrongParameterException;
import main.model.User;
import main.model.repository.GlobalSettingsRepository;
import main.model.repository.PostRepository;
import main.model.repository.UserRepository;
import main.service.interfaces.CaptchaService;
import main.service.interfaces.ImageService;
import main.service.interfaces.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private static final Logger LOGGER = LogManager.getLogger(Main.class);

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

  @Value("${file.size}")
  private int maxSizeForFile;

  private char[] alphabetAndDigits = "ab1cd2ef3g45hij6klm7no8pq9rst0uvwxyz".toCharArray();

  private final MailConfig mailConfig;
  private final ImageService imageService;
  private final CaptchaService captchaService;
  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final PasswordEncoder passwordEncoder;
  private final GlobalSettingsRepository settingsRepository;
  private final AuthenticationManager authenticationManager;

  @Override
  public ResultResponse check(Principal principal) {
    if (principal == null) {
      return new FalseResultResponse();
    }
    User user = getCurrentUserByEmail(principal.getName());
    return new LoginResultResponse(getAllUserInformation(user));
  }

  @Override
  public ResultResponse login(LoginRequest req)
      throws LoginUserWrongCredentialsException, IllegalStateException {
    String email = req.getEmail();
    String password = req.getPassword();
    User userRepo =
        userRepository
            .getByEmail(req.getEmail())
            .orElseThrow(LoginUserWrongCredentialsException::new);

    if (!passwordEncoder.matches(password, userRepo.getPassword())) {
      log.info("password not correct");
      throw new LoginUserWrongCredentialsException();
    }

    Authentication auth =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password));

    if (!auth.isAuthenticated()) {
      return new FalseResultResponse();
    }
    SecurityContextHolder.getContext().setAuthentication(auth);
    org.springframework.security.core.userdetails.User user =
        (org.springframework.security.core.userdetails.User) auth.getPrincipal();
    User currentUser = getCurrentUserByEmail(user.getUsername());
    return new LoginResultResponse(getAllUserInformation(currentUser));
  }

  @Override
  public ResultResponse logout() {
    SecurityContextHolder.clearContext();
    return new OkResultResponse();
  }

  @Override
  public ResultResponse myStatistics(Principal principal)
      throws LoginUserWrongCredentialsException, IllegalStateException {
    User user;
    if (principal != null) {
      Optional<User> optionalUser = userRepository.getByEmail(principal.getName());
      if (optionalUser.isPresent()) {
        user = optionalUser.get();
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

        LOGGER.info("Available my statistics");
        return new StatisticsResponse(
            postsCount, likesCount, dislikeCount, viewsCount, timeFirstPublication);
      }
    }
    throw new LoginUserWrongCredentialsException();
  }

  @Override
  public ResultResponse allStatistics(Principal principal)
      throws LoginUserWrongCredentialsException, WrongParameterException {
    User user;
    boolean moderatorAuth = false;
    if (principal != null) {
      Optional<User> optionalUser = userRepository.getByEmail(principal.getName());
      if (optionalUser.isPresent()) {
        user = optionalUser.get();
        if (user.userModerator()) {
          moderatorAuth = true;
        }
      }
    }

    boolean statisticsIsPublic = !settingsRepository.getStatIsPub();
    if (statisticsIsPublic && moderatorAuth) {
      LOGGER.info("Banned public display of statistics");
      throw new WrongParameterException();
    }
    int postsCount = postRepository.countPostForModStatusAccepted().orElse(0);
    int allLikesCount = postRepository.countLikesAllPosts().orElse(0);
    int allDislikeCount = postRepository.countDislikesAllPosts().orElse(0);
    int allViewsCount = postRepository.countViewsAllPosts().orElse(0);
    LocalDateTime time = postRepository.timeFirstPublication();
    long timeFirstPublication = time.atZone(ZoneId.systemDefault()).toEpochSecond();
    LOGGER.info("Available ALL statistics");
    return new StatisticsResponse(
        postsCount, allLikesCount, allDislikeCount, allViewsCount, timeFirstPublication);
  }

  @Override
  public ResultResponse restorePassword(RestoreRequest req)
      throws LoginUserWrongCredentialsException {
    User user =
        userRepository
            .getByEmail(req.getEmail())
            .orElseThrow(LoginUserWrongCredentialsException::new);
    if (!isEmailCorrect(req.getEmail())) {
      LOGGER.info("Incorrect email");
      return new FalseResultResponse();
    }

    String codeForRecovery = generateHashCode();
    user.setCode(codeForRecovery);
    userRepository.save(user);

    SimpleMailMessage mimeMessage = new SimpleMailMessage();
    mimeMessage.setTo(req.getEmail());
    mimeMessage.setSubject(subjectString);
    mimeMessage.setText(contactStringForMessage + codeForRecovery);
    JavaMailSender javaMailSender = mailConfig.javaMailSender();
    javaMailSender.send(mimeMessage);
    LOGGER.info("Sent message with recovery code");
    return new OkResultResponse();
  }

  @Override
  public ResultResponse changePassword(ChangePasswordRequest req) throws UsernameNotFoundException {
    User user =
        userRepository
            .findByCode(req.getCode())
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        String.format("user with code %s not found", req.getCode())));

    if (addErrorsForChangePassword(req).hasErrors()) {
      return addErrorsForChangePassword(req);
    }

    user.setPassword(passwordEncoder.encode(req.getPassword()));
    userRepository.save(user);
    return new OkResultResponse();
  }

  @Override
  public ResultResponse registerUser(RegisterRequest req) throws ContentNotAllowedException {
    if (!settingsRepository.getMultiUser()) {
      throw new ContentNotAllowedException().createWith("регистрация", "Регистрация запрещена!");
    }
    if (addErrorsForRegister(req).hasErrors()) {
      return addErrorsForRegister(req);
    }

    String newHashPassword = passwordEncoder.encode(req.getPassword());
    User user = new User(LocalDateTime.now(), req.getName(), req.getEmail(), newHashPassword);
    userRepository.save(user);
    return new OkResultResponse();
  }

  @Override
  @Transactional
  public ResultResponse editMyProfile(ChangeDataMyProfile change, Principal principal) {
    BadResultResponse badResultResponse = new BadResultResponse();

    User user = null;
    if (principal == null) {
      throw new LoginUserWrongCredentialsException();
    }
    Optional<User> optionalUser = userRepository.getByEmail(principal.getName());
    if (optionalUser.isPresent()) {
      user = optionalUser.get();
    }

    assert user != null;
    String emailUserHttpSession = user.getEmail();

    boolean existEmail = userRepository.existEmailOrNot(emailUserHttpSession) != null;
    boolean nameTrue = !change.getName().equals(" ");

    if (nameTrue) {
      user.setName(change.getName());
    } else {
      badResultResponse.addError("name", "Имя указано неверно");
    }

    if (isEmailCorrect(change.getEmail()) && existEmail) {
      user.setEmail(change.getEmail());
    } else {
      badResultResponse.addError("email", "Этот e-mail уже зарегистрирован");
    }

    if (change.getPassword() != null) {
      if (isPasswordValid(change.getPassword())) {
        badResultResponse.addError("password", "Слишком короткий пароль");
      }
      user.setPassword(passwordEncoder.encode(change.getPassword()));
    }

    if (change.getRemovePhoto() != null && change.getPhoto() != null) {
      if (change.getRemovePhoto() == 0 && change.getPhoto().getSize() < maxSizeForFile) {
        user.setPhoto(imageService.uploadFileAndResizeImage(change.getPhoto(), true));
      } else {
        badResultResponse.addError("photo", "Фото слишком большое, нужно не более 5 Мб");
      }
      if (change.getRemovePhoto() == 1 && change.getPhoto().getSize() == 0) {
        imageService.deletePhoto(user.getPhoto());
      }
    }

    userRepository.save(user);

    if (badResultResponse.hasErrors()) {
      return badResultResponse;
    }
    return new OkResultResponse();
  }

  @Override
  public User getCurrentUserByEmail(String email) {
    Optional<User> currentUser = userRepository.getByEmail(email);
    if (currentUser.isEmpty()) {
      throw new UsernameNotFoundException(String.format("user with email %s not found", email));
    } else {
      return currentUser.get();
    }
  }

  private String generateHashCode() {
    return IntStream.range(0, lengthHashKey)
        .map(i -> (int) (Math.random() * (alphabetAndDigits.length - 1)))
        .mapToObj(a -> String.valueOf(alphabetAndDigits[a]))
        .collect(Collectors.joining());
  }

  private AllUserInformationResponse getAllUserInformation(User user) {
    return new AllUserInformationResponse(
        user.getId(),
        user.getName(),
        user.getPhoto(),
        user.getEmail(),
        user.userModerator(),
        user.userModerator()
            ? postRepository.countPostsUserForModerationStatusNew(user.getId())
            : 0,
        user.userModerator());
  }

  private BadResultResponse addErrorsForRegister(RegisterRequest req) {
    BadResultResponse badResultResponse = new BadResultResponse();

    if (isUserExist(req.getEmail())) {

      badResultResponse.addError("email", "Этот e_mail уже зарегистрирован");
    }

    if (!isEmailCorrect(req.getEmail())) {
      badResultResponse.addError("email", "e_mail введен некорректно");
    }

    if (isNameBlank(req.getName())) {
      badResultResponse.addError("name", "Имя указано неверно");
    }

    if (isPasswordValid(req.getPassword())) {
      badResultResponse.addError("password", "Слишком короткий пароль");
    }

    if (!isCaptchaSecretValid(req.getCaptcha(), req.getCaptchaSecret())) {
      badResultResponse.addError("captcha", "Код с картинки введён неверно");
    }

    return new BadResultResponse(badResultResponse.getErrors());
  }

  private BadResultResponse addErrorsForChangePassword(ChangePasswordRequest req) {
    BadResultResponse badResultResponse = new BadResultResponse();

    if (req.getCode().isBlank()) {
      badResultResponse.addError(
          "code",
          "Ссылка для восстановления пароля устарела."
              + "<a href=\"/auth/restore\">Запросить ссылку снова</a>");
    }

    if (isPasswordValid(req.getPassword())) {
      badResultResponse.addError("password", "Слишком короткий пароль");
    }

    if (!isCaptchaSecretValid(req.getCaptcha(), req.getCaptchaSecret())) {
      badResultResponse.addError("captcha", "Код с картинки введён неверно");
    }

    return new BadResultResponse(badResultResponse.getErrors());
  }

  private Boolean isUserExist(String email) {
    return userRepository.getByEmail(email).isPresent();
  }

  private Boolean isEmailCorrect(String email) {
    return email.matches(correctMail);
  }

  private Boolean isNameBlank(String name) {
    return name.isBlank();
  }

  private Boolean isPasswordValid(String password) {
    return password.isBlank() && password.length() < minLengthPassword;
  }

  private Boolean isCaptchaSecretValid(String captcha, String captchaSecret) {
    return captcha.isBlank()
        || captchaSecret.isBlank()
        || captchaService.checkCaptcha(captcha, captchaSecret);
  }
}
