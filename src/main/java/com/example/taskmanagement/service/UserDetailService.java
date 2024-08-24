package com.example.taskmanagement.service;

import com.example.taskmanagement.config.UserDetail;
import com.example.taskmanagement.model.AppUser;
import com.example.taskmanagement.repository.UserRepository;
import com.example.taskmanagement.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailService.class);

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;


    public UserDetailService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Loading user by username: {}", username);
        return userRepository.findByUsername(username)
                .map(UserDetail::new)
                .orElseThrow(() -> {
                    logger.error("User '{}' not found", username);
                    return new UsernameNotFoundException(String.format("User '%s' not found", username));
                });
    }
}
