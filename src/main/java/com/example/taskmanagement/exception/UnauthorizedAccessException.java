package com.example.taskmanagement.exception;

import org.springframework.security.core.AuthenticationException;

public class UnauthorizedAccessException extends AuthenticationException {

    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
