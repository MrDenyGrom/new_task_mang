package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.CreateTaskDTO;
import com.example.taskmanagement.dto.UpdateTaskDTO;
import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.Status;
import com.example.taskmanagement.service.TaskService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@Valid @RequestBody CreateTaskDTO taskDTO,
                                           Authentication authentication) {
        logger.info("User {} is creating a task: {}", authentication.getName(), taskDTO);
        Task createdTask = taskService.createTask(taskDTO, authentication.getName());
        return ResponseEntity.ok(createdTask);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Task> editTask(@PathVariable("id") long id,
                                         @Valid @RequestBody UpdateTaskDTO taskDTO,
                                         Authentication authentication) {
        logger.info("User {} is editing task with id {}: {}", authentication.getName(), id, taskDTO);
        Task updatedTask = taskService.editTask(taskDTO, id, authentication.getName());
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") long id,
                                             Authentication authentication) {
        logger.info("User {} is deleting task with id {}", authentication.getName(), id);
        taskService.deleteTask(id, authentication.getName());
        return ResponseEntity.ok("Task successfully deleted");
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable("id") long id) {
        logger.info("Retrieving task with id {}", id);
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/myTasks")
    public ResponseEntity<List<Task>> getMyTasks(Authentication authentication) {
        logger.info("User {} is retrieving their tasks", authentication.getName());
        List<Task> tasks = taskService.getAllTasksByUser(authentication.getName());
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/setStatus/{id}")
    public ResponseEntity<Task> setStatus(@PathVariable("id") long id,
                                          @RequestParam("status") Status status,
                                          Authentication authentication) {
        logger.info("User {} is setting status of task with id {} to {}", authentication.getName(), id, status);
        Task updatedTask = taskService.setStatus(id, status, authentication.getName());
        return ResponseEntity.ok(updatedTask);
    }
}
