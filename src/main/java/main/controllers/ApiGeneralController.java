package main.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import main.api.request.CommentRequest;
import main.api.request.ModerationRequest;
import main.api.request.SettingsRequest;
import main.api.request.change.ChangeWithPassword;
import main.api.response.InitResponse;
import main.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Log4j2
@RestController
@RequestMapping("api/")
@RequiredArgsConstructor
public class ApiGeneralController {
    private final TagService tagService;
    private final SettingsService settingsService;
    private final InitResponse initResponse;
    private final UserService userService;
    private final CommentService commentService;
    private final PostService postService;

    @GetMapping(value = "init")
    public InitResponse init() {
        return initResponse;
    }

    @GetMapping(value = "settings")
    public ResponseEntity<?> getSettings() {
        log.info("Message");
        return new ResponseEntity<>(settingsService.getSettings(), HttpStatus.OK);
    }

    @PutMapping(value = "settings")
    public ResponseEntity<?>  saveSettings(@RequestBody SettingsRequest settingsRequest) {
        settingsService.saveGlobalSettings(settingsRequest);
        log.info("Message");
        return new ResponseEntity<>(settingsRequest, HttpStatus.OK);
    }

    @GetMapping(value = "tag")
    public ResponseEntity<?>  getTags() {
        log.info("Message");
        return new ResponseEntity<>(tagService.getAllTags(), HttpStatus.OK);
    }

    @GetMapping(value = "tag", params = {"query"})
    public ResponseEntity<?>  getTagsWithParameters(@RequestParam(value = "query") String query) {
        log.info("Message");
        return new ResponseEntity<>(tagService.getTagsWithQuery(query), HttpStatus.OK);
    }
// change after check!
//    @PostMapping(value = "image")
//    public ResponseEntity uploadImage(@RequestParam MultipartFile multipartFile, HttpServletRequest
//    httpServletRequest) {
//        return  new ResponseEntity<>("", HttpStatus.OK);
//    }

    @PostMapping(value = "comment")
    public ResponseEntity<?>  addComment(@RequestBody CommentRequest commentRequest,
                                     HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        log.info("Message");
        return new ResponseEntity<>(commentService.addComment(commentRequest, httpSession), HttpStatus.OK);
    }

    @PostMapping(value = "moderation")
    public ResponseEntity<?>  moderatePost(@RequestBody ModerationRequest moderationRequest,
                                       HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        log.info("Message");
        return new ResponseEntity<>(postService.moderatePost(moderationRequest, httpSession), HttpStatus.OK);
    }

    @GetMapping(value = "calendar", params = {"year"})
    public ResponseEntity<?>  getPostsByCalendar(@RequestParam(value = "year") Integer year) {
        log.info("Message");
        return new ResponseEntity<>(postService.postsByCalendar(year), HttpStatus.OK);
    }

    @PostMapping(value = "profile/my")
    public ResponseEntity<?>  editProfile(@RequestBody ChangeWithPassword changeWithPassword,
                                      HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        log.info("Message");
        return new ResponseEntity<>(userService.editMyProfile(changeWithPassword, httpSession), HttpStatus.OK);
    }

    @GetMapping(value = "statistics/my")
    public ResponseEntity<?>  getMyStatistic(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        log.info("Message");
        return new ResponseEntity<>(userService.myStatistics(httpSession), HttpStatus.OK);
    }

    @GetMapping(value = "statistics/all")
    public ResponseEntity<?> getAllStatistic(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        log.info("Message");
        return new ResponseEntity<>(userService.allStatistics(httpSession), HttpStatus.OK);
    }
}