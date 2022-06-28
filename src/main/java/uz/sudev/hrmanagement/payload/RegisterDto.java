package uz.sudev.hrmanagement.payload;


import lombok.*;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterDto {
    @NotNull(message = "Please enter your first name!")
    @Size(min = 3) // javani o'zidan turib length ini tekshiradi , database ga aloqasi yoq
    private String firstName;
    @NotNull(message = "Please enter your last name!")
    @Size(min = 5)
    private String lastName;
    @NotNull(message = "Please enter your active email!")
    @Email // example@gmail.com shakilida bo'lishini ta'minlab beradi.
    private String email;
    @NotNull(message = "Please enter your password!")
    @Size(min = 8)
    private String password;
}
