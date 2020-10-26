package main.controllers;
import lombok.extern.log4j.Log4j2;
import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.model.GlobalSettings;
import main.model.User;
import main.repository.GlobalSettingsRepository;
import main.repository.UserRepository;
import main.service.SettingsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("api")
public class ApiGeneralController
{
    private final SettingsService settingsService;
    private final InitResponse initResponse;

    public ApiGeneralController(SettingsService settingsService,
                                InitResponse initResponse)
    {
        this.settingsService = settingsService;
        this.initResponse = initResponse;
    }

    @GetMapping("/init")
    private InitResponse init()
    {
        return initResponse;
    }

    @GetMapping("/settings")
    private ResponseEntity<SettingsResponse> getSettings()
    {
        log.info("Message");
        return new ResponseEntity<>(settingsService.getGlobalSettings(), HttpStatus.OK);
    }

    @PutMapping("/settings")
    private ResponseEntity<SettingsResponse> saveSettings(@RequestBody GlobalSettings globalSettings)
    {
//        Optional<User> userOptional = new UserRepository().
        settingsService.saveGlobalSettings(globalSettings);
        log.info("Message");
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}