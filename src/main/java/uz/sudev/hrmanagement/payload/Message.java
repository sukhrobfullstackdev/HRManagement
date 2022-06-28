package uz.sudev.hrmanagement.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Message {
    private boolean success;
    private String message;
    private String token;

    public Message(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

}
