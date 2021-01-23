package main.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import main.api.request.LoginRequest;
import main.api.request.RegisterRequest;
import main.api.request.RestoreRequest;
import main.api.request.change.ChangePasswordRequest;
import main.service.CaptchaService;
import main.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Log4j2
@RestController
@RequestMapping(value = "api/auth/")
@RequiredArgsConstructor
public class ApiAuthController {
    private final UserService userService;
    private final CaptchaService captchaService;

    @PostMapping(value = "login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        log.info("Message");
        return new ResponseEntity<>(userService.login(loginRequest, httpSession), HttpStatus.OK);
    }

    @GetMapping(value = "check")
    public ResponseEntity checkAuth(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        log.info("Message");
        return new ResponseEntity<>(userService.check(httpSession), HttpStatus.OK);
    }

    @PostMapping(value = "restore")
    public ResponseEntity restorePassword(@RequestBody RestoreRequest restoreRequest) {
        log.info("Message");
        return new ResponseEntity<>(userService.restorePassword(restoreRequest), HttpStatus.OK);
    }

    @PostMapping(value = "password")
    public ResponseEntity changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        log.info("Message");
        return new ResponseEntity<>(userService.changePassword(changePasswordRequest), HttpStatus.OK);
    }

    @PostMapping(value = "register")
    public ResponseEntity register(@RequestBody RegisterRequest registerRequest) {
        log.info("Message");
        return new ResponseEntity<>(userService.registerUser(registerRequest), HttpStatus.OK);
    }

    @GetMapping(value = "captcha")
    public ResponseEntity captcha() {
        log.info("Message");
        return new ResponseEntity<>(captchaService.generateCaptcha(), HttpStatus.OK);
    }

    @GetMapping(value = "logout")
    public ResponseEntity logout(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        log.info("Message");
        return new ResponseEntity<>(userService.logout(httpSession), HttpStatus.OK);
    }
}