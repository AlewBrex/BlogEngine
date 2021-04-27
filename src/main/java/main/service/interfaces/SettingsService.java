package main.service.interfaces;

import main.api.request.SettingsRequest;
import main.api.response.SettingsResponse;
import main.exception.LoginUserWrongCredentialsException;

import java.security.Principal;

public interface SettingsService {

    SettingsResponse getSettings();

    void saveGlobalSettings(SettingsRequest req, Principal principal)
            throws LoginUserWrongCredentialsException;
}
