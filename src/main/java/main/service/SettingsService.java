package main.service;

import main.api.response.SettingsResponse;
import main.repository.GlobalSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingsService
{
    @Autowired
    private GlobalSettingsRepository globalSettingsRepository;

    public SettingsResponse getGlobalSettings()
    {
        SettingsResponse settingsResponse = new SettingsResponse(true,
                false, true);
        return settingsResponse;
    }
}