package com.example.taskmanagement.dto;

import com.example.taskmanagement.model.Role;

public class UpdateUserDTO {
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}