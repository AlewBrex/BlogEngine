package main.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import main.api.request.LoginRequest;
import main.api.request.RegisterRequest;
import main.api.request.RestoreRequest;
import main.api.request.change.ChangePasswordRequest;
import main.exception.ContentNotAllowedException;
import main.exception.LoginUserWrongCredentialsException;
import main.service.CaptchaServiceImpl;
import main.service.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Log4j2
@RestController
@RequestMapping(value = "api/auth/")
@RequiredArgsConstructor
public class ApiAuthController {

  private final UserService userService;
  private final CaptchaServiceImpl captchaService;

  @PostMapping(value = "login")
  public ResponseEntity login(@RequestBody LoginRequest loginRequest)
      throws LoginUserWrongCredentialsException {
    log.info("Get request api/auth/login");
    return new ResponseEntity<>(userService.login(loginRequest), HttpStatus.OK);
  }

  @GetMapping(value = "check")
  public ResponseEntity checkAuth(Principal principal) {
    log.info("Get request api/auth/check");
    return new ResponseEntity<>(userService.check(principal), HttpStatus.OK);
  }

  @PostMapping(value = "restore")
  public ResponseEntity restorePassword(@RequestBody RestoreRequest restoreRequest) {
    log.info("Get request api/auth/restore");
    return new ResponseEntity<>(userService.restorePassword(restoreRequest), HttpStatus.OK);
  }

  @PostMapping(value = "password")
  public ResponseEntity changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
    log.info("Get request api/auth/password");
    return new ResponseEntity<>(
        userService.changePassword(changePasswordRequest), HttpStatus.OK);
  }

  @PostMapping(value = "register")
  public ResponseEntity register(@RequestBody RegisterRequest registerRequest)
      throws ContentNotAllowedException {
    log.info("Get request api/auth/register");
    return new ResponseEntity<>(userService.registerUser(registerRequest), HttpStatus.OK);
  }

  @GetMapping(value = "captcha")
  public ResponseEntity captcha() {
    log.info("Get request api/auth/captcha");
    return new ResponseEntity<>(captchaService.generateCaptcha(), HttpStatus.OK);
  }

  @GetMapping(value = "logout")
  public ResponseEntity logout() {
    log.info("Get request api/auth/logout");
    return new ResponseEntity<>(userService.logout(), HttpStatus.OK);
  }
}
