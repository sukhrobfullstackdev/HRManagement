package uz.sudev.hrmanagement.service;


import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.sudev.hrmanagement.entity.State;
import uz.sudev.hrmanagement.entity.Task;
import uz.sudev.hrmanagement.entity.User;
import uz.sudev.hrmanagement.entity.enums.RoleName;
import uz.sudev.hrmanagement.entity.enums.StateName;
import uz.sudev.hrmanagement.payload.Message;
import uz.sudev.hrmanagement.payload.TaskDto;
import uz.sudev.hrmanagement.repository.AuthenticationRepository;
import uz.sudev.hrmanagement.repository.RoleRepository;
import uz.sudev.hrmanagement.repository.StateRepository;
import uz.sudev.hrmanagement.repository.TaskRepository;

import java.util.Optional;
import java.util.UUID;


@Service
public class TaskService {
    final TaskRepository taskRepository;
    final JavaMailSender javaMailSender;
    final RoleRepository roleRepository;
    final AuthenticationRepository authenticationRepository;
    final StateRepository stateRepository;

    public TaskService(@Lazy JavaMailSender javaMailSender, @Lazy  StateRepository stateRepository, @Lazy  AuthenticationRepository authenticationRepository, @Lazy  RoleRepository roleRepository, @Lazy  TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.roleRepository = roleRepository;
        this.authenticationRepository = authenticationRepository;
        this.stateRepository = stateRepository;
        this.javaMailSender = javaMailSender;
    }

    private boolean sendEmail(String taskName, String taskGivenBy, String sendingEmail, String type) {
        String body = "";
        if (type.equals("post")) {
            body = "<p>Good day! You have been assigned a task called " + taskName + " by " + taskGivenBy + "!</p>";
        } else if (type.equals("put")) {
            body = "<p>Good day! The task named " + taskName + " that you gave to " + taskGivenBy + " was edited!</p>";
        }
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

    public ResponseEntity<Message> addTask(TaskDto taskDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> optionalUser = authenticationRepository.findById(taskDto.getUserId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (authentication.getAuthorities().contains(roleRepository.findByRoleName(RoleName.ROLE_EMPLOYEE))) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Message(false, "You do not have the right to attach a task!"));
            } else if (user.getAuthorities().contains(roleRepository.findByRoleName(RoleName.ROLE_DIRECTOR))) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Message(false, "An assignment cannot be assigned to an employee in this position!"));
            } else if (user.getAuthorities().contains(roleRepository.findByRoleName(RoleName.ROLE_HR_MANAGER)) && authentication.getAuthorities().contains(roleRepository.findByRoleName(RoleName.ROLE_MANAGER))) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Message(false, "You cannot attach an assignment to an employee in this position!"));
            } else {
                Task task = new Task();
                task.setName(task.getName());
                task.setDescription(task.getDescription());
                task.setUser(user);
                Optional<State> stateOptional = stateRepository.findByStateName(StateName.TASK_OPEN);
                if (stateOptional.isPresent()) {
                    task.setState(stateOptional.get());
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message(false, "The current type of state is not found!"));
                }
                task.setShouldDoneAt(taskDto.getShouldDoneAt());
                taskRepository.save(task);
                Optional<User> userOptional = authenticationRepository.findById(task.getCreatedBy());
                if (userOptional.isPresent()) {
                    boolean sendEmail = sendEmail(task.getName(), optionalUser.get().getFirstName(), user.getEmail(), "post");
                    if (sendEmail) {
                        return ResponseEntity.status(HttpStatus.CREATED).body(new Message(true, "The task is successfully assigned to " + user.getFirstName() + " " + user.getLastName() + ", and reported him about this task!"));
                    } else {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(new Message(false, "The task is successfully assigned to " + user.getFirstName() + " " + user.getLastName() + ", but an error is occurred while sending email to him!"));
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message(false, "The task is saved, but you are not found to report about this task to the employee by email!"));
                }

            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message(false, "The employee is not found!"));
        }
    }

    public ResponseEntity<Message> editTask(UUID id, TaskDto taskDto) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        Optional<State> optionalState = stateRepository.findById(taskDto.getStateId());
        if (optionalTask.isPresent() && optionalState.isPresent()) {
            Task task = optionalTask.get();
            task.setState(optionalState.get());
            taskRepository.save(task);
            Optional<User> givenBy = authenticationRepository.findById(task.getCreatedBy());
            if (givenBy.isPresent()) {
                boolean sendEmail = sendEmail(task.getName(), task.getUser().getFirstName(), givenBy.get().getEmail(), "edit");
                if (sendEmail) {
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Message(true, "The task is successfully edited, and reported about this to who gave this task to you!"));
                } else {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(new Message(true, "The task is successfully edited, but an error is occurred while reporting about this to who gave this task to you!"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message(false, "The task is successfully edited, but the employee who gave this task to you is not found!"));
            }

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message(false, "The task is not found or the given type of state is ot found in our database!"));
        }
    }

    public ResponseEntity<Message> deleteTask(UUID id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            taskRepository.delete(optionalTask.get());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Message(true, "The task is successfully deleted!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message(false, "The task is not found!"));
        }
    }

    public ResponseEntity<Page<Task>> getTasks(int page, int size) {
        return ResponseEntity.ok(taskRepository.findAll(PageRequest.of(page, size)));
    }

    public ResponseEntity<Task> getTask(UUID id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        return optionalTask.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    public ResponseEntity<Page<Task>> getTasksForEmployee(int page, int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(taskRepository.findAllByUser(user, PageRequest.of(page, size)));
    }

    public ResponseEntity<Task> getTaskForEmployee(UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Optional<Task> optionalTask = taskRepository.findByUserAndId(user, id);
        return optionalTask.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
}
