package com.example.taskmanagement.service;

import com.example.taskmanagement.config.UserDetail;
import com.example.taskmanagement.dto.AuthRequest;
import com.example.taskmanagement.dto.UpdateUserDTO;
import com.example.taskmanagement.exception.ResourceAlreadyExistsException;
import com.example.taskmanagement.exception.ResourceNotFoundException;
import com.example.taskmanagement.model.AppUser;
import com.example.taskmanagement.repository.UserRepository;
import com.example.taskmanagement.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(AuthenticationManager authenticationManager,
                       JwtTokenProvider jwtTokenProvider,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<AppUser> findByUsername(String username) {
        logger.info("Searching for user with username: {}", username);
        return userRepository.findByUsername(username);
    }

    @Transactional
    public ResponseEntity<AppUser> addUser(AppUser appUser) {
        logger.info("Attempting to add new user: {}", appUser.getUsername());
        if (userRepository.findByUsername(appUser.getUsername()).isPresent()) {
            logger.warn("User already exists: {}", appUser.getUsername());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        AppUser savedUser = userRepository.save(appUser);
        logger.info("User added successfully: {}", savedUser.getUsername());
        return ResponseEntity.ok(savedUser);
    }

    public String login(AuthRequest authRequest) {
        logger.info("Attempting login for username: {}", authRequest.getUsername());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            String token = jwtTokenProvider.generateToken(authentication);
            logger.info("Login successful for username: {}", authRequest.getUsername());
            return token;
        } catch (AuthenticationException e) {
            logger.error("Login failed for username: {}", authRequest.getUsername(), e);
            throw new IllegalArgumentException("Invalid username or password");
        }
    }

    public AppUser getCurrentUser(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Fetching current user from authentication context.");
        UserDetail userDetails = (UserDetail) authentication.getPrincipal();
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> {
                    logger.error("User not found for ID: {}", userDetails.getId());
                    return new ResourceNotFoundException("User not found");
                });
    }

    public List<AppUser> getAllUsers() {
        logger.info("Fetching all users.");
        return userRepository.findAll();
    }

    @Transactional
    public AppUser createUser(AppUser user) {
        logger.info("Creating new user: {}", user.getUsername());
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            logger.warn("Username already exists: {}", user.getUsername());
            throw new ResourceAlreadyExistsException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        AppUser savedUser = userRepository.save(user);
        logger.info("User created successfully: {}", savedUser.getUsername());
        return savedUser;
    }

    @Transactional
    public AppUser updateUser(Long id, UpdateUserDTO updateUserDTO) {
        logger.info("Updating user with ID: {}", id);
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", id);
                    return new ResourceNotFoundException("User not found with id: " + id);
                });
        if (updateUserDTO.getRole() != null) {
            user.setRole(updateUserDTO.getRole());
            logger.info("Updated role for user with ID: {}", id);
        }
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        logger.info("Deleting user with ID: {}", id);
        userRepository.deleteById(id);
        logger.info("User deleted successfully with ID: {}", id);
    }
}
