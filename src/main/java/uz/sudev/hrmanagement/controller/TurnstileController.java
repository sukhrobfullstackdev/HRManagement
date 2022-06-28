package uz.sudev.hrmanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.sudev.hrmanagement.payload.Message;
import uz.sudev.hrmanagement.payload.TurnstileDto;
import uz.sudev.hrmanagement.service.TurnstileService;

@RestController
@RequestMapping(value = "/turnstile")
public class TurnstileController {
    final TurnstileService turnstileService;

    public TurnstileController(TurnstileService turnstileService) {
        this.turnstileService = turnstileService;
    }

    @PostMapping
    public ResponseEntity<Message> turnstile(@RequestBody TurnstileDto turnstileDto) {
        return turnstileService.turnstile(turnstileDto);
    }
}
