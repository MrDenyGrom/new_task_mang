package com.example.taskmanagement.service;

import com.example.taskmanagement.config.UserDetail;
import com.example.taskmanagement.dto.CreateTaskDTO;
import com.example.taskmanagement.dto.TaskFilterDTO;
import com.example.taskmanagement.dto.UpdateTaskDTO;
import com.example.taskmanagement.exception.ResourceNotFoundException;
import com.example.taskmanagement.exception.TaskNotFoundException;
import com.example.taskmanagement.model.AppUser;
import com.example.taskmanagement.model.Status;
import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public Task createTask(CreateTaskDTO taskDTO) {
        logger.info("Attempting to create a new task with title: {}", taskDTO.getHead());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            logger.error("Authentication failed. No user is currently logged in.");
            throw new AccessDeniedException("You must be logged in to create a task.");
        }

        String username = authentication.getName();
        logger.debug("Authenticated user: {}", username);

        AppUser author = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Task task = mapCreateDTOToTask(taskDTO, author);

        Task savedTask = taskRepository.save(task);
        logger.info("Task successfully created with ID: {}", savedTask.getId());

        return savedTask;
    }

    private Task mapCreateDTOToTask(CreateTaskDTO taskDTO, AppUser author) {
        Task task = new Task();
        task.setHead(taskDTO.getHead());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setPriority(taskDTO.getPriority());
        task.setUserAuthor(author);

        if (taskDTO.getExecutorUsername() != null) {
            AppUser executor = userRepository.findByUsername(taskDTO.getExecutorUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("Executor user not found"));
            task.setUserExecutor(executor);
        }

        task.setDueDate(taskDTO.getDueDate());
        return task;
    }

    public Task editTask(UpdateTaskDTO taskDTO, long id) {
        logger.info("Attempting to edit task with ID: {}", id);

        Task task = findTaskById(id);
        AppUser currentUser = getAuthenticatedUser();

        if (!task.getUserAuthor().equals(currentUser)) {
            logger.error("User '{}' is not authorized to edit task with ID: {}", currentUser.getUsername(), id);
            throw new AccessDeniedException("You are not authorized to edit this task.");
        }

        updateTaskFromDTO(task, taskDTO);

        Task updatedTask = taskRepository.save(task);
        logger.info("Task with ID: {} successfully updated", updatedTask.getId());

        return updatedTask;
    }

    private Task findTaskById(long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + id));
    }

    private AppUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            logger.error("No authentication found. Operation not permitted.");
            throw new AccessDeniedException("You must be logged in to perform this action.");
        }

        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }

    private void updateTaskFromDTO(Task task, UpdateTaskDTO taskDTO) {
        task.setHead(taskDTO.getHead());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setPriority(taskDTO.getPriority());
        if (taskDTO.getExecutorUsername() != null) {
            AppUser executor = userRepository.findByUsername(taskDTO.getExecutorUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("Executor user not found"));
            task.setUserExecutor(executor);
        }
        task.setDueDate(taskDTO.getDueDate());
    }

    public void deleteTask(long id) {
        logger.info("Attempting to delete task with ID: {}", id);

        Task task = findTaskById(id);
        AppUser currentUser = getAuthenticatedUser();

        if (!task.getUserAuthor().equals(currentUser)) {
            logger.error("User '{}' is not authorized to delete task with ID: {}", currentUser.getUsername(), id);
            throw new AccessDeniedException("You are not authorized to delete this task.");
        }

        taskRepository.deleteById(id);
        logger.info("Task with ID: {} successfully deleted", id);
    }

    public List<Task> getAllTasks() {
        logger.info("Retrieving all tasks");
        return taskRepository.findAll();
    }

    public Page<Task> getFilteredTasks(Pageable pageable, TaskFilterDTO filterDTO) {
        logger.info("Retrieving filtered tasks");

        Specification<Task> spec = createTaskSpecification(filterDTO);
        return taskRepository.findAll(spec, pageable);
    }

    private Specification<Task> createTaskSpecification(TaskFilterDTO filterDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filterDTO != null && filterDTO.getHead() != null) {
                predicates.add(criteriaBuilder.like(root.get("head"), "%" + filterDTO.getHead() + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public List<Task> getTasksBetweenDates(Date startDate, Date endDate) {
        return taskRepository.findByDueDateBetween(startDate, endDate);
    }

    public List<Task> getTasksByFilter(Specification<Task> spec) {
        return taskRepository.findAll(spec);
    }

    public List<Task> getAllTasksByUser(String username) {
        AppUser user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return taskRepository.findByUserAuthorOrUserExecutor(user, user);
    }

    public Task getTaskById(long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }

    public List<Long> getAllTaskIds() {
        return taskRepository.getAllTaskIds();
    }

    public Page<Task> getAllTasksId(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    public List<Task> getTasksByStatus(Status status) {
        return taskRepository.findByStatus(status);
    }

    @Transactional
    public Task assignTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));


        task.setUserExecutor(user);
        return taskRepository.save(task);
    }

    @Transactional
    public void deleteAnyTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Transactional
    public Task updateAnyTask(Long id, UpdateTaskDTO updateTaskDTO) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        return taskRepository.save(task);
    }

    public Task setStatus(long id, Status status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + id));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser currentUser = userRepository.findByUsername(authentication.getName()).orElseThrow(ResourceNotFoundException::new);

        if (!task.getUserAuthor().equals(currentUser) && !task.getUserExecutor().equals(currentUser)) {
            throw new AccessDeniedException("You are not authorized to change the status of this task.");
        }

        task.setStatus(status);
        return taskRepository.save(task);
    }


}
