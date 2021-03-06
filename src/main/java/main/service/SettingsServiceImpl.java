package main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import main.Main;
import main.api.request.SettingsRequest;
import main.api.response.SettingsResponse;
import main.exception.LoginUserWrongCredentialsException;
import main.model.User;
import main.model.repository.GlobalSettingsRepository;
import main.model.repository.UserRepository;
import main.service.interfaces.SettingsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class SettingsServiceImpl implements SettingsService {

  private static final Logger LOGGER = LogManager.getLogger(Main.class);

  private final GlobalSettingsRepository repo;
  private final UserRepository userRepository;

  @Override
  public SettingsResponse getSettings() {
    LOGGER.info("Получены настройки");
    return new SettingsResponse(repo.getMultiUser(), repo.getPostPremod(), repo.getStatIsPub());
  }

  @Override
  public void saveGlobalSettings(SettingsRequest req, Principal principal)
      throws LoginUserWrongCredentialsException {
    User user;
    if (principal != null) {
      Optional<User> optionalUser = userRepository.getByEmail(principal.getName());
      if (optionalUser.isPresent()) {
        user = optionalUser.get();
        if (user.userModerator()) {
          repo.dropSettings();
          repo.setMultiUser(blnString(req.isMultiuserMode()));
          repo.setPostPremod(blnString(req.isPostPremoderation()));
          repo.setStatIsPub(blnString(req.isStatisticsIsPublic()));
          LOGGER.info("Установлены новые настройки");
        }
      }
    }
    throw new LoginUserWrongCredentialsException();
  }

  private String blnString(Boolean bln) {
    return bln ? "YES" : "NO";
  }
}
