package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.CreateTaskDTO;
import com.example.taskmanagement.dto.UpdateTaskDTO;
import com.example.taskmanagement.exception.TaskNotFoundException;
import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.Status;
import com.example.taskmanagement.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Create a new task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task created", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = {@Content}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content})
    })
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Task> createTask(@Valid @RequestBody CreateTaskDTO taskDTO) {
        Task createdTask = taskService.createTask(taskDTO);
        logger.info("Task created successfully: {}", createdTask.getId());
        return ResponseEntity.ok(createdTask);
    }

    @Operation(summary = "Edit an existing task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = {@Content}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content}),
            @ApiResponse(responseCode = "403", description = "Forbidden - Not the task author", content = {@Content}),
            @ApiResponse(responseCode = "404", description = "Task not found", content = {@Content})
    })
    @PutMapping("/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Task> editTask(@PathVariable("id") long id, @Valid @RequestBody UpdateTaskDTO taskDTO) {
        try {
            Task updatedTask = taskService.editTask(taskDTO, id);
            logger.info("Task with ID: {} updated successfully", id);
            return ResponseEntity.ok(updatedTask);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Delete a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted", content = {@Content}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content}),
            @ApiResponse(responseCode = "403", description = "Forbidden - Not the task author", content = {@Content}),
            @ApiResponse(responseCode = "404", description = "Task not found", content = {@Content})
    })
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") long id) {
        try {
            taskService.deleteTask(id);
            logger.info("Task with ID: {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Get a task by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task retrieved", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Task not found", content = {@Content}),
    })
    @GetMapping("/get/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable("id") long id) {
        try {
            Task task = taskService.getTaskById(id);
            logger.info("Task with ID: {} retrieved", id);
            return ResponseEntity.ok(task);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get current user's tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content})
    })
    @GetMapping("/myTasks")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Task>> getMyTasks(Authentication authentication) {
        List<Task> tasks = taskService.getAllTasksByUser(authentication.getName());
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Set the status of a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status updated", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content}),
            @ApiResponse(responseCode = "403", description = "Forbidden - Not the task author or executor", content = {@Content}),
            @ApiResponse(responseCode = "404", description = "Task not found", content = {@Content})
    })
    @PutMapping("/setStatus/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Task> setStatus(@PathVariable("id") long id, @RequestParam("status") Status status) {
        try {
            Task updatedTask = taskService.setStatus(id, status);
            return ResponseEntity.ok(updatedTask);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Get all tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All tasks retrieved", content = {@Content(mediaType = "application/json")})
    })
    @GetMapping("/allTasks")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> allTasks = taskService.getAllTasks();
        return ResponseEntity.ok(allTasks);
    }

    @Operation(summary = "Get tasks by status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks with the specified status retrieved", content = {@Content(mediaType = "application/json")})
    })
    @GetMapping("/tasksByStatus")
    public ResponseEntity<List<Task>> getTasksByStatus(@RequestParam("status") Status status) {
        List<Task> tasks = taskService.getTasksByStatus(status);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/tasksBetweenDates")
    public ResponseEntity<List<Task>> getTasksBetweenDates(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") String startDateString,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") String endDateString) {
        try {
            Date startDate = java.sql.Date.valueOf(LocalDate.parse(startDateString));
            Date endDate = java.sql.Date.valueOf(LocalDate.parse(endDateString));
            List<Task> tasks = taskService.getTasksBetweenDates(startDate, endDate);
            return ResponseEntity.ok(tasks);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
