package com.example.taskmanagement.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TaskNotFoundException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(TaskNotFoundException.class);

    public TaskNotFoundException(String message) {
        super(message);
        logger.warn("TaskNotFoundException: Task not found with message: {}", message);
    }

    public TaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
        logger.warn("TaskNotFoundException: Task not found with message: {}, Cause: {}", message, cause.getMessage(), cause);
    }
}
