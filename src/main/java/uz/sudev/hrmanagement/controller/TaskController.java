package uz.sudev.hrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.sudev.hrmanagement.entity.Task;
import uz.sudev.hrmanagement.payload.Message;
import uz.sudev.hrmanagement.payload.TaskDto;
import uz.sudev.hrmanagement.service.TaskService;

import java.util.UUID;

@RestController
@RequestMapping(value = "/task")
public class TaskController {
    final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PreAuthorize("hasAnyRole('DIRECTOR','HR_MANAGER','MANAGER')")
    @GetMapping
    public ResponseEntity<Page<Task>> getTasks(@RequestParam int page,@RequestParam int size) {
        return taskService.getTasks(page,size);
    }
    @PreAuthorize("hasAnyRole('DIRECTOR','HR_MANAGER','MANAGER')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Task> getTask(@PathVariable UUID id) {
        return taskService.getTask(id);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping(value = "/forEmployee")
    public ResponseEntity<Page<Task>> getTasksForEmployee(@RequestParam int page,@RequestParam int size) {
        return taskService.getTasksForEmployee(page,size);
    }
    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping(value = "/forEmployee/{id}")
    public ResponseEntity<Task> getTaskForEmployee(@PathVariable UUID id) {
        return taskService.getTaskForEmployee(id);
    }

    @PreAuthorize("hasAnyRole('DIRECTOR','HR_MANAGER','MANAGER')")
    @PostMapping
    public ResponseEntity<Message> addTask(@RequestBody TaskDto taskDto) {
        return taskService.addTask(taskDto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Message> editTask(@PathVariable UUID id, @RequestBody TaskDto taskDto) {
        return taskService.editTask(id, taskDto);
    }

    @PreAuthorize("hasRole('DIRECTOR')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Message> deleteTask(@PathVariable UUID id) {
        return taskService.deleteTask(id);
    }
}
