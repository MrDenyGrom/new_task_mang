package com.example.taskmanagement.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommentNotFoundException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(CommentNotFoundException.class);

    public CommentNotFoundException(String message) {
        super(message);
        logger.warn("CommentNotFoundException: Comment not found with message: {}", message);
    }

    public CommentNotFoundException(String message, Throwable cause) {
        super(message, cause);
        logger.warn("CommentNotFoundException: Comment not found with message: {}, Cause: {}", message, cause.getMessage(), cause);
    }
}