package com.example.taskmanagement.dto;

import com.example.taskmanagement.model.Priority;
import com.example.taskmanagement.model.Status;
import jakarta.validation.constraints.NotNull;

public class UpdateTaskDTO {
    @NotNull
    private String head;

    private String description;

    @NotNull
    private Status status;

    @NotNull
    private Priority priority;

    private String executorUsername;

    public String getExecutorUsername() {
        return executorUsername;
    }

    public void setExecutorUsername(String executorUsername) {
        this.executorUsername = executorUsername;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}