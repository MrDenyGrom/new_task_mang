package com.example.taskmanagement.service;

import com.example.taskmanagement.dto.CreateTaskDTO;
import com.example.taskmanagement.dto.UpdateTaskDTO;
import com.example.taskmanagement.model.AppUser;
import com.example.taskmanagement.model.Status;
import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.exception.TaskNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public Task createTask(CreateTaskDTO taskDTO, String username) {
        AppUser userAuthor = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        AppUser userExecutor = null;
        if (taskDTO.getExecutorUsername() != null) {
            userExecutor = userService.findByUsername(taskDTO.getExecutorUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Executor not found: " + taskDTO.getExecutorUsername()));
        }

        Task task = new Task(taskDTO.getHead(), taskDTO.getDescription(), taskDTO.getStatus(), taskDTO.getPriority(), userAuthor, userExecutor);
        return taskRepository.save(task);
    }

    public Task editTask(UpdateTaskDTO taskDTO, long taskId, String username) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        if (!task.isAuthor(username)) {
            throw new AccessDeniedException("You do not have rights to edit this task");
        }

        task.setHead(taskDTO.getHead());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setPriority(taskDTO.getPriority());

        if (taskDTO.getExecutorUsername() != null) {
            AppUser userExecutor = userService.findByUsername(taskDTO.getExecutorUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Executor not found: " + taskDTO.getExecutorUsername()));
            task.setUserExecutor(userExecutor);
        }

        return taskRepository.save(task);
    }

    public void deleteTask(long taskId, String username) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        if (!task.isAuthor(username)) {
            throw new AccessDeniedException("You do not have rights to delete this task");
        }

        taskRepository.delete(task);
    }

    public Task getTaskById(long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }

    public List<Task> getAllTasksByUser(String username) {
        AppUser user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return taskRepository.findByUserAuthorOrUserExecutor(user, user);
    }

    public Task setStatus(long taskId, Status status, String username) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        if (!task.isAuthor(username) && !task.isExecutor(username)) {
            throw new AccessDeniedException("You do not have rights to change the status of this task");
        }

        task.setStatus(status);
        return taskRepository.save(task);
    }
}
