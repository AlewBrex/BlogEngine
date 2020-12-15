package main.controllers;

import lombok.extern.log4j.Log4j2;
import main.api.request.SettingsRequest;
import main.api.response.InitResponse;
import main.service.SettingsService;
import main.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("api/")
public class ApiGeneralController
{
    private final TagService tagService;
    private final SettingsService settingsService;
    private final InitResponse initResponse;

    public ApiGeneralController(TagService tagService, SettingsService settingsService,
                                InitResponse initResponse)
    {
        this.tagService = tagService;
        this.settingsService = settingsService;
        this.initResponse = initResponse;
    }

    @GetMapping("init")
    public InitResponse init()
    {
        return initResponse;
    }

    @GetMapping("settings")
    public ResponseEntity getSettings()
    {
        log.info("Message");
        return new ResponseEntity<>(settingsService.getSettings(), HttpStatus.OK);
    }

    @PutMapping("settings")
    public ResponseEntity saveSettings(@RequestBody SettingsRequest settingsRequest)
    {
        settingsService.saveGlobalSettings(settingsRequest);
        log.info("Message");
        return ResponseEntity.status(HttpStatus.OK).body(settingsRequest);
    }

    @GetMapping("tag")
    public ResponseEntity getTagWq()
    {
        log.info("Message");
        return new ResponseEntity<>(tagService.getTagsWithQuery(), HttpStatus.OK);
    }

    @GetMapping(value = "tag", params = {"query"})
    public ResponseEntity getTags(@RequestParam(value = "query") String query)
    {
        log.info("Message");
        return new ResponseEntity<>(tagService.getTags(query), HttpStatus.OK);
    }
}