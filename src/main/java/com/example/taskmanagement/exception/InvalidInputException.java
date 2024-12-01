package com.example.taskmanagement.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidInputException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(InvalidInputException.class);

    public InvalidInputException(String message) {
        super(message);
        logger.error("InvalidInputException: Invalid input with message: {}", message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
        logger.error("InvalidInputException: Invalid input with message: {}, Cause: {}", message, cause.getMessage(), cause);
    }
}
