package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.AuthRequest;
import com.example.taskmanagement.dto.AuthResponse;
import com.example.taskmanagement.dto.UserRegistrationRequest;
import com.example.taskmanagement.model.AppUser;
import com.example.taskmanagement.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AppUser> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        logger.info("Registering new user: {}", request.getUsername());
        AppUser user = new AppUser(request.getUsername(), request.getPassword());
        ResponseEntity<AppUser> response = userService.addUser(user);
        if (response.getStatusCode().is2xxSuccessful()) {
            logger.info("A user registration successful");
            AppUser createdUser = response.getBody();
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } else if (response.getStatusCode() == HttpStatus.CONFLICT) {
            logger.info("A user with the same name already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(null);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody AuthRequest authRequest, HttpServletRequest request) {
        logger.info("User login attempt: {}", authRequest.getUsername());
        String token = userService.login(authRequest);
        request.getSession().setAttribute("token", token);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
