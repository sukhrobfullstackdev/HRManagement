package uz.sudev.hrmanagement.service;



import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.sudev.hrmanagement.entity.Turnstile;
import uz.sudev.hrmanagement.entity.TurnstileTable;
import uz.sudev.hrmanagement.entity.User;
import uz.sudev.hrmanagement.payload.Message;
import uz.sudev.hrmanagement.payload.TurnstileDto;
import uz.sudev.hrmanagement.repository.AuthenticationRepository;
import uz.sudev.hrmanagement.repository.TurnstileRepository;
import uz.sudev.hrmanagement.repository.TurnstileTableRepository;

import java.util.Optional;


@Service
public class TurnstileService {
    final TurnstileTableRepository turnstileTableRepository;
    final TurnstileRepository turnstileRepository;
    final AuthenticationRepository authenticationRepository;

    public TurnstileService(@Lazy AuthenticationRepository authenticationRepository, @Lazy  TurnstileRepository turnstileRepository, @Lazy TurnstileTableRepository turnstileTableRepository) {
        this.turnstileTableRepository = turnstileTableRepository;
        this.turnstileRepository = turnstileRepository;
        this.authenticationRepository = authenticationRepository;
    }

    public ResponseEntity<Message> turnstile(TurnstileDto turnstileDto) {
        Optional<User> optionalUser = authenticationRepository.findById(turnstileDto.getUserId());
        Optional<Turnstile> optionalTurnstile = turnstileRepository.findById(turnstileDto.getTurnstileId());
        if (optionalTurnstile.isPresent() && optionalUser.isPresent()) {
            TurnstileTable turnstileTable = new TurnstileTable();
            turnstileTable.setTurnstile(optionalTurnstile.get());
            turnstileTable.setUser(optionalUser.get());
            turnstileTableRepository.save(turnstileTable);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Message(true,"The current employee is successfully registered!!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message(false,"The current employee is not found!"));
        }
    }
}
