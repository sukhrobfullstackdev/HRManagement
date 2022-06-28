package uz.sudev.hrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.sudev.hrmanagement.payload.LoginDto;
import uz.sudev.hrmanagement.payload.Message;
import uz.sudev.hrmanagement.payload.RegisterDto;
import uz.sudev.hrmanagement.service.AuthenticationService;


@RestController
@RequestMapping(value = "/authentication")
public class AuthenticationController {
    final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Message> register(@RequestBody RegisterDto registerDto) {
        return authenticationService.register(registerDto);
    }

    @PostMapping("/login")
    public ResponseEntity<Message> login(@RequestBody LoginDto loginDto) {
        return authenticationService.login(loginDto);
    }

    @GetMapping("/confirmEmail")
    public ResponseEntity<Message> confirmEmail(@RequestParam String emailCode, @RequestParam String sendingEmail) {
        return authenticationService.confirmEmail(emailCode, sendingEmail);
    }
}
