package com.example.taskmanagement.model;

public enum Priority {
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low"),
    CRITICAL("Critical"),
    LOWEST("Lowest");

    private final String displayName;

    Priority(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Priority fromString(String priority) {
        for (Priority p : Priority.values()) {
            if (p.displayName.equalsIgnoreCase(priority)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Unknown priority: " + priority);
    }

    public boolean isCritical() {
        return this == CRITICAL;
    }

    public boolean isHigh() {
        return this == HIGH;
    }

    public boolean isMedium() {
        return this == MEDIUM;
    }

    public boolean isLow() {
        return this == LOW;
    }

    public boolean isLowest() {
        return this == LOWEST;
    }
}
