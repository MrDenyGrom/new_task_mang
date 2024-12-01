package com.example.taskmanagement.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(ResourceNotFoundException.class);

    public ResourceNotFoundException(String message) {
        super(message);
        logger.warn("ResourceNotFoundException: Resource not found with message: {}", message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
        logger.warn("ResourceNotFoundException: Resource not found with message: {}, Cause: {}", message, cause.getMessage(), cause);
    }

    public ResourceNotFoundException() {
        super("Resource not found.");
        logger.warn("ResourceNotFoundException: Default constructor invoked. Resource not found.");
    }
}
