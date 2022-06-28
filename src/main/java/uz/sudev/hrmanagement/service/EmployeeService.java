package uz.sudev.hrmanagement.service;


import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.sudev.hrmanagement.entity.User;
import uz.sudev.hrmanagement.entity.enums.RoleName;
import uz.sudev.hrmanagement.payload.EmployeeDto;
import uz.sudev.hrmanagement.payload.Message;
import uz.sudev.hrmanagement.repository.AuthenticationRepository;
import uz.sudev.hrmanagement.repository.RoleRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;


@Service
public class EmployeeService {
    final AuthenticationRepository authenticationRepository;
    final JavaMailSender javaMailSender;
    final RoleRepository roleRepository;
    final PasswordEncoder passwordEncoder;

    public EmployeeService(PasswordEncoder passwordEncoder, JavaMailSender javaMailSender, @Lazy RoleRepository roleRepository, @Lazy AuthenticationRepository authenticationRepository) {
        this.authenticationRepository = authenticationRepository;
        this.roleRepository = roleRepository;
        this.javaMailSender = javaMailSender;
        this.passwordEncoder = passwordEncoder;
    }

    private boolean sendEmail(String sendingEmail, String emailCode) {
        String link = "http://localhost:8080/employee/verify?emailCode=" + emailCode + "&email=" + sendingEmail;
        String body = "<form action=" + link + " method=\"post\">\n" +
                "<label>Create password for your cabinet</label>" +
                "<br/><input type=\"text\" name=\"password\" placeholder=\"password\">\n" +
                "<br/>  <button>Submit</button>\n" +
                "</form>";

        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("sukhrobdev83@gmail.com");
            simpleMailMessage.setTo(sendingEmail);
            simpleMailMessage.setText(body);
            javaMailSender.send(simpleMailMessage);
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }

    public ResponseEntity<Message> addManager(EmployeeDto employeeDto) {
        if (authenticationRepository.existsByEmailAndEnabled(employeeDto.getEmail())) {
            User user = new User();
            user.setEmail(employeeDto.getEmail());
            user.setFirstName(employeeDto.getFirstName());
            user.setLastName(employeeDto.getLastName());
            user.setEmailCode(UUID.randomUUID().toString());
            user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_MANAGER)));
            authenticationRepository.save(user);
            boolean sent = sendEmail(user.getEmail(), user.getEmailCode());
            if (sent) {
                return ResponseEntity.status(HttpStatus.CREATED).body(new Message(true, "The manager is successfully created!"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Message(false, "An error occurred while sending email!"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Message(false, "The email is already in use!"));
        }
    }

    public ResponseEntity<Message> verifyEmail(String sendingEmail, String emailCode, HttpServletRequest request) {
        Optional<User> optionalUser = authenticationRepository.findByEmailAndEmailCode(sendingEmail, emailCode);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(passwordEncoder.encode(request.getParameter("password")));
            user.setEnabled(true);
            user.setEmailCode(null);
            authenticationRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Message(true,"The account is successfully verified!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message(false, "The account is not verified!"));
        }
    }

    public ResponseEntity<Message> addHRManager(EmployeeDto employeeDto) {
        if (authenticationRepository.existsByEmailAndEnabled(employeeDto.getEmail())) {
            User user = new User();
            user.setEmail(employeeDto.getEmail());
            user.setFirstName(employeeDto.getFirstName());
            user.setLastName(employeeDto.getLastName());
            user.setEmailCode(UUID.randomUUID().toString());
            user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_HR_MANAGER)));
            authenticationRepository.save(user);
            boolean sent = sendEmail(user.getEmail(), user.getEmailCode());
            if (sent) {
                return ResponseEntity.status(HttpStatus.CREATED).body(new Message(true, "The manager is successfully created!"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Message(false, "An error occurred while sending email!"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Message(false, "The email is already in use!"));
        }
    }

    public ResponseEntity<Message> addEmployee(EmployeeDto employeeDto) {
        if (authenticationRepository.existsByEmailAndEnabled(employeeDto.getEmail())) {
            User user = new User();
            user.setEmail(employeeDto.getEmail());
            user.setFirstName(employeeDto.getFirstName());
            user.setLastName(employeeDto.getLastName());
            user.setEmailCode(UUID.randomUUID().toString());
            user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_EMPLOYEE)));
            authenticationRepository.save(user);
            boolean sent = sendEmail(user.getEmail(), user.getEmailCode());
            if (sent) {
                return ResponseEntity.status(HttpStatus.CREATED).body(new Message(true, "The manager is successfully created!"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Message(false, "An error occurred while sending email!"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Message(false, "The email is already in use!"));
        }
    }
}
