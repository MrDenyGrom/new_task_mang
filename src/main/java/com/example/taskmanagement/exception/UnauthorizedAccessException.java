package com.example.taskmanagement.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedAccessException extends AuthenticationException {

    private static final Logger logger = LoggerFactory.getLogger(UnauthorizedAccessException.class);

    public UnauthorizedAccessException(String message) {
        super(message);
        logger.error("UnauthorizedAccessException occurred: {}", message);
    }

    public UnauthorizedAccessException(String message, Throwable cause) {
        super(message, cause);
        logger.error("UnauthorizedAccessException occurred: {}, Cause: {}", message, cause.getMessage(), cause);
    }
}
