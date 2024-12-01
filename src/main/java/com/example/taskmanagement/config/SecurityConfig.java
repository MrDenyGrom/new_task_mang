package com.example.taskmanagement.config;

import com.example.taskmanagement.security.JwtAuthenticationEntryPoint;
import com.example.taskmanagement.security.JwtAuthenticationFilter;
import com.example.taskmanagement.security.JwtTokenProvider;
import com.example.taskmanagement.service.UserDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailService userDetailService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider, UserDetailService userDetailService,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        logger.info("Initializing SecurityConfig with JwtTokenProvider, UserDetailService, and JwtAuthenticationEntryPoint.");
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailService = userDetailService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        logger.debug("Creating JwtAuthenticationFilter bean.");
        return new JwtAuthenticationFilter(jwtTokenProvider, userDetailService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, @Autowired JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        logger.info("Configuring SecurityFilterChain.");
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> {
                    logger.debug("Configuring authorization rules.");
                    authorizeRequests
                            .requestMatchers("/register", "/login", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html/**")
                            .permitAll()
                            .anyRequest().authenticated();
                })
                .exceptionHandling(exceptionHandling -> {
                    logger.debug("Configuring exception handling with JwtAuthenticationEntryPoint.");
                    exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint);
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        logger.debug("Creating AuthenticationProvider bean.");
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        logger.info("AuthenticationProvider configured with UserDetailService and PasswordEncoder.");
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        logger.debug("Creating AuthenticationManager bean.");
        AuthenticationManager manager = configuration.getAuthenticationManager();
        logger.info("AuthenticationManager created successfully.");
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.debug("Creating PasswordEncoder bean.");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        logger.info("BCryptPasswordEncoder initialized.");
        return encoder;
    }
}
