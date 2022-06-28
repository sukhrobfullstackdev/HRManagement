package uz.sudev.hrmanagement.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;


@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
public class EmployeeDto {
    @NotNull(message = "Please enter the employee's first name!")
    private String firstName;
    @NotNull(message = "Please enter the employee's last name!")
    private String lastName;
    @NotNull(message = "Please enter the employee's email!")
    private String email;
}
