package main.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import main.api.request.CommentRequest;
import main.api.request.ModerationRequest;
import main.api.request.SettingsRequest;
import main.api.request.change.ChangeDataMyProfile;
import main.api.response.InitResponse;
import main.exception.ContentNotAllowedException;
import main.service.interfaces.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Log4j2
@RestController
@RequestMapping("api/")
@RequiredArgsConstructor
public class ApiGeneralController {

  private final ImageService imageService;
  private final SettingsService settingsService;
  private final InitResponse initResponse;
  private final UserService userService;
  private final CommentService commentService;
  private final PostService postService;

  @GetMapping(value = "init")
  public InitResponse init() {
    log.info("Получен GET запрос api/init");
    return initResponse;
  }

  @GetMapping(value = "settings")
  public ResponseEntity<?> getSettings() {
    log.info("Получен GET запрос api/settings");
    return new ResponseEntity<>(settingsService.getSettings(), HttpStatus.OK);
  }

  @PutMapping(value = "settings")
  public ResponseEntity<?> saveSettings(
      @RequestBody SettingsRequest settingsRequest, Principal principal) {
    settingsService.saveGlobalSettings(settingsRequest, principal);
    log.info("Получен PUT запрос api/settings");
    return new ResponseEntity<>(settingsRequest, HttpStatus.OK);
  }

  @PostMapping(value = "image")
  public ResponseEntity uploadImage(@RequestParam MultipartFile image) {
    log.info("Получен POST запрос api/image");
    return new ResponseEntity<>(imageService.uploadFileAndResizeImage(image, false), HttpStatus.OK);
  }

  @PostMapping(value = "comment")
  public ResponseEntity<?> addComment(
      @RequestBody CommentRequest commentRequest, Principal principal)
      throws ContentNotAllowedException {
    log.info("Получен POST запрос api/comment");
    return commentService.addComment(commentRequest, principal);
  }

  @PostMapping(value = "comment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> addCommentMultipartFile(
      @RequestBody CommentRequest commentRequest, Principal principal)
      throws ContentNotAllowedException {
    log.info("Получен POST запрос api/comment");
    return commentService.addComment(commentRequest, principal);
  }

  @PostMapping(value = "moderation")
  public ResponseEntity<?> moderatePost(
      @RequestBody ModerationRequest moderationRequest, Principal principal) {
    log.info("Получен POST запрос api/moderation");
    return new ResponseEntity<>(
        postService.moderatePost(moderationRequest, principal), HttpStatus.OK);
  }

  @GetMapping(value = "calendar")
  public ResponseEntity<?> getPostsByCalendar(@RequestParam(value = "year") int year) {
    log.info("Получен GET запрос api/calendar");
    return new ResponseEntity<>(postService.postsByCalendar(year), HttpStatus.OK);
  }

  @PostMapping(value = "profile/my")
  public ResponseEntity<?> editProfile(
      @RequestBody ChangeDataMyProfile changeWithPassword, Principal principal) {
    log.info("Получен POST запрос api/profile/my");
    return new ResponseEntity<>(
        userService.editMyProfile(changeWithPassword, principal), HttpStatus.OK);
  }

  @PostMapping(value = "profile/my", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> editProfileAndMultipartFile(
      @ModelAttribute ChangeDataMyProfile changeWithPassword, Principal principal) {
    log.info("Получен POST запрос api/profile/my с изменением фотографии пользователя");
    return new ResponseEntity<>(
        userService.editMyProfile(changeWithPassword, principal), HttpStatus.OK);
  }

  @GetMapping(value = "statistics/my")
  public ResponseEntity<?> getMeStatistic(Principal principal) {
    log.info("Получен GET запрос api/statistics/my");
    return new ResponseEntity<>(userService.myStatistics(principal), HttpStatus.OK);
  }

  @GetMapping(value = "statistics/all")
  public ResponseEntity<?> getAllStatistic(Principal principal) {
    log.info("Получен GET запрос api/statistics/all");
    return new ResponseEntity<>(userService.allStatistics(principal), HttpStatus.OK);
  }
}
