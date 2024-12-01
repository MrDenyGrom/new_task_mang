package com.example.taskmanagement.model;

public enum Status {
    WAITING("Waiting"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    ON_HOLD("On Hold");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Status fromString(String status) {
        for (Status s : Status.values()) {
            if (s.displayName.equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }

    public boolean isCompleted() {
        return this == COMPLETED;
    }

    public boolean isInProgress() {
        return this == IN_PROGRESS;
    }

    public boolean isCancelled() {
        return this == CANCELLED;
    }
}
