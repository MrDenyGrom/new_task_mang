package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.AuthRequest;
import com.example.taskmanagement.dto.AuthResponse;
import com.example.taskmanagement.dto.UserRegistrationRequest;
import com.example.taskmanagement.model.AppUser;
import com.example.taskmanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;


import static com.example.taskmanagement.model.Role.USER;
import static org.springframework.http.ResponseEntity.badRequest;

@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(AuthenticationManager authenticationManager, UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully", content = {@Content}),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input data", content = {@Content}),
            @ApiResponse(responseCode = "409", description = "Conflict - Username already exists", content = {@Content})
    })
    public ResponseEntity<AppUser> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        logger.info("Registering new user: {}", request.getUsername());
        AppUser user = new AppUser(request.getUsername(), request.getPassword(), request.getEmail(), true, false, USER);
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
            return badRequest().build();
        }
    }


    @Operation(summary = "Login an existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful", content = {@Content}),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid credentials", content = {@Content})
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody AuthRequest authRequest, HttpServletRequest request) {
        logger.info("User login attempt: {}", authRequest.getUsername());
        String token = userService.login(authRequest);
        request.getSession().setAttribute("token", token);
        return ResponseEntity.ok(new AuthResponse(token));
    }


    @Operation(summary = "Get current user information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User information retrieved", content = {@Content}),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not logged in", content = {@Content})
    })
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AppUser> getCurrentUser(HttpServletRequest request) {
        AppUser user = userService.getCurrentUser(request);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Logout current user")
    @ApiResponse(responseCode = "200", description = "Logout successful", content = {@Content})
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> logoutUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok().build();
    }

}
