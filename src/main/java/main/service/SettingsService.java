package main.service;

import lombok.extern.log4j.Log4j2;
import main.api.response.SettingsResponse;
import main.model.GlobalSettings;
import main.repository.GlobalSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class SettingsService
{
    private final GlobalSettingsRepository globalSettingsRepository;

    @Autowired
    public SettingsService(GlobalSettingsRepository globalSettingsRepository)
    {
        this.globalSettingsRepository = globalSettingsRepository;
    }

    public SettingsResponse getGlobalSettings()
    {
        SettingsResponse settingsResponse = new SettingsResponse(false,
                true, false);
        log.info("Message");
        return settingsResponse;
    }

    public void saveGlobalSettings(GlobalSettings globalSettings)
    {
        globalSettingsRepository.save(globalSettings);
        log.info("Message");
    }
}