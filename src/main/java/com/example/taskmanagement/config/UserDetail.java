package com.example.taskmanagement.config;

import com.example.taskmanagement.model.AppUser;
import com.example.taskmanagement.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record UserDetail(AppUser appUser) implements UserDetails {

    private static final Logger logger = LoggerFactory.getLogger(UserDetail.class);

    public UserDetail(AppUser appUser) {
        this.appUser = appUser;
        logger.info("UserDetail created for AppUser with ID: {}", appUser.getId());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        logger.debug("Fetching authorities for user with username: {}", appUser.getUsername());
        Role userRole = appUser.getRole();
        String roleName = "ROLE_" + userRole.name();
        logger.debug("User role: {} mapped to authority: {}", userRole, roleName);
        return List.of(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getPassword() {
        logger.debug("Fetching password for user with username: {}", appUser.getUsername());
        return appUser.getPassword();
    }

    @Override
    public String getUsername() {
        String username = appUser.getUsername();
        logger.debug("Fetching username: {}", username);
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        logger.debug("Checking if account is non-expired for user with username: {}", appUser.getUsername());
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        boolean isLocked = appUser.isLocked();
        logger.debug("Checking if account is non-locked for user with username: {}. Locked: {}", appUser.getUsername(), isLocked);
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        logger.debug("Checking if credentials are non-expired for user with username: {}", appUser.getUsername());
        return true;
    }

    @Override
    public boolean isEnabled() {
        boolean isEnabled = appUser.isEnabled();
        logger.debug("Checking if account is enabled for user with username: {}. Enabled: {}", appUser.getUsername(), isEnabled);
        return isEnabled;
    }

    public long getId() {
        long userId = appUser.getId();
        logger.debug("Fetching user ID: {}", userId);
        return userId;
    }
}
