package com.example.taskmanagement.model;

public enum Role {
    USER("User"),
    ADMIN("Administrator"),
    MODERATOR("Moderator"),
    GUEST("Guest");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Role fromString(String role) {
        for (Role r : Role.values()) {
            if (r.displayName.equalsIgnoreCase(role)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + role);
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public boolean isUser() {
        return this == USER;
    }

    public boolean isModerator() {
        return this == MODERATOR;
    }

    public boolean isGuest() {
        return this == GUEST;
    }
}
