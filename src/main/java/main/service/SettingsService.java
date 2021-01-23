package main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import main.api.request.SettingsRequest;
import main.api.response.SettingsResponse;
import main.model.GlobalSettings;
import main.repository.GlobalSettingsRepository;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class SettingsService {
    private final GlobalSettingsRepository globalSettingsRepository;

    public SettingsResponse getSettings() {
        GlobalSettings multi = globalSettingsRepository.findByCode("MULTIUSER_MODE");
        GlobalSettings post = globalSettingsRepository.findByCode("POST_PREMODERATION");
        GlobalSettings stat = globalSettingsRepository.findByCode("STATISTICS_IS_PUBLIC");
        log.info("Message");
        return new SettingsResponse(
                strgBoolean(multi.getValue()),
                strgBoolean(post.getValue()),
                strgBoolean(stat.getValue()));
    }

    public void saveGlobalSettings(SettingsRequest settingsRequest) {
        boolean multi = settingsRequest.isMultiuserMode();
        boolean post = settingsRequest.isPostPremoderation();
        boolean stat = settingsRequest.isStatisticsIsPublic();
        String multiUser = blnString(multi);
        String postPre = blnString(post);
        String statistics = blnString(stat);
        GlobalSettings strMulti = globalSettingsRepository.findByCode("MULTIUSER_MODE");
        GlobalSettings strPost = globalSettingsRepository.findByCode("POST_PREMODERATION");
        GlobalSettings strStat = globalSettingsRepository.findByCode("STATISTICS_IS_PUBLIC");
        strMulti.setValue(multiUser);
        strPost.setValue(postPre);
        strPost.setValue(statistics);
        globalSettingsRepository.save(strMulti);
        globalSettingsRepository.save(strPost);
        globalSettingsRepository.save(strStat);
        log.info("Message");
    }

    private boolean strgBoolean(String code) {
        return code.equals("YES");
    }

    private String blnString(boolean bln) {
        return bln ? "YES" : "NO";
    }
}