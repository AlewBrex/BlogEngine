package main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import main.api.request.SettingsRequest;
import main.api.response.SettingsResponse;
import main.model.GlobalSettings;
import main.model.User;
import main.model.repository.GlobalSettingsRepository;
import main.model.repository.UserRepository;
import main.service.interfaces.SettingsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class SettingsServiceImpl implements SettingsService {

    private final GlobalSettingsRepository repo;
    private final UserRepository userRepo;

    public SettingsResponse getSettings() {
        log.info("Получены настройки");
        return new SettingsResponse(
                repo.getMultiUser(),
                repo.getPostPremod(),
                repo.getStatIsPub());
    }

    public void saveGlobalSettings(SettingsRequest req, Principal principal) {
        Optional<User> optionalUser = null;
        if (principal != null) {
            optionalUser = userRepo.getByEmail(principal.getName());
        }
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.userModerator()) {
                repo.dropSettings();
                repo.setMultiUser(blnString(req.isMultiuserMode()));
                repo.setPostPremod(blnString(req.isPostPremoderation()));
                repo.setStatIsPub(blnString(req.isStatisticsIsPublic()));
                log.info("Установлены новые настройки");
            }
        }
    }

    private String blnString(boolean bln) {
        return bln ? "YES" : "NO";
    }
}