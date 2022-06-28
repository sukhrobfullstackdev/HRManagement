package uz.sudev.hrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.sudev.hrmanagement.payload.EmployeeDto;
import uz.sudev.hrmanagement.payload.Message;
import uz.sudev.hrmanagement.service.EmployeeService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {
    final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PreAuthorize("hasAnyRole('DIRECTOR','HR_MANAGER')")
    @PostMapping(value = "/addManager")
    public ResponseEntity<Message> addManager(@Valid @RequestBody EmployeeDto employeeDto) {
        return employeeService.addManager(employeeDto);
    }
    @PreAuthorize("hasRole('DIRECTOR')")
    @PostMapping(value = "/addHRManager")
    public ResponseEntity<Message> addHRManager(@Valid @RequestBody EmployeeDto employeeDto) {
        return employeeService.addHRManager(employeeDto);
    }
    @PreAuthorize("hasRole('HR_MANAGER')")
    @PostMapping(value = "/addEmployee")
    public ResponseEntity<Message> addEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        return employeeService.addEmployee(employeeDto);
    }

    @PostMapping(value = "/verify")
    public ResponseEntity<Message> verifyEmail(@RequestParam String sendingEmail, @RequestParam String emailCode, HttpServletRequest request) {
        return employeeService.verifyEmail(sendingEmail,emailCode,request);
    }
}
