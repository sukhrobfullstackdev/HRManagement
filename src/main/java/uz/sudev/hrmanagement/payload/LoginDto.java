package uz.sudev.hrmanagement.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
public class LoginDto {
    @NotNull(message = "Please enter your username!")
    @Email
    private String username;
    @NotNull(message = "Please enter your password!")
    @Size(min = 8)
    private String password;
}
