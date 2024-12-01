package com.example.taskmanagement.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceAlreadyExistsException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(ResourceAlreadyExistsException.class);

    public ResourceAlreadyExistsException(String message) {
        super(message);
        logger.error("ResourceAlreadyExistsException: Resource conflict with message: {}", message);
    }

    public ResourceAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
        logger.error("ResourceAlreadyExistsException: Resource conflict with message: {}, Cause: {}", message, cause.getMessage(), cause);
    }
}
