package main.controllers;

import lombok.extern.log4j.Log4j2;
import main.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Log4j2
@RestController
@RequestMapping(value = "api/auth/")
public class ApiAuthController
{
    private final UserService userService;

    public ApiAuthController(UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping(value = "check")
    public ResponseEntity checkAuth(HttpServletRequest httpServletRequest)
    {
        HttpSession httpSession = httpServletRequest.getSession();
        log.info("Message");
        return new ResponseEntity<>(userService.check(httpSession), HttpStatus.OK);
    }
}