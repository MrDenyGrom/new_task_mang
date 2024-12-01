package com.example.taskmanagement.dto;

import com.example.taskmanagement.model.Priority;
import com.example.taskmanagement.model.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Date;
import java.util.Objects;

public class UpdateTaskDTO {

    @NotBlank(message = "Head is required")
    private String head;

    private String description;

    @NotNull(message = "Status is required")
    private Status status;

    @NotNull(message = "Priority is required")
    private Priority priority;

    private String executorUsername;

    private java.sql.Date dueDate;

    public UpdateTaskDTO(String head, String description, Status status, Priority priority, String executorUsername, Date dueDate) {
        this.head = head;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.executorUsername = executorUsername;
        this.dueDate = dueDate;
    }

    public UpdateTaskDTO() {
    }

    public @NotBlank(message = "Head is required") String getHead() {
        return head;
    }

    public void setHead(@NotBlank(message = "Head is required") String head) {
        this.head = head;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public @NotNull(message = "Status is required") Status getStatus() {
        return status;
    }

    public void setStatus(@NotNull(message = "Status is required") Status status) {
        this.status = status;
    }

    public @NotNull(message = "Priority is required") Priority getPriority() {
        return priority;
    }

    public void setPriority(@NotNull(message = "Priority is required") Priority priority) {
        this.priority = priority;
    }

    public String getExecutorUsername() {
        return executorUsername;
    }

    public void setExecutorUsername(String executorUsername) {
        this.executorUsername = executorUsername;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateTaskDTO that = (UpdateTaskDTO) o;
        return Objects.equals(head, that.head) && Objects.equals(description, that.description) && status == that.status && priority == that.priority && Objects.equals(executorUsername, that.executorUsername) && Objects.equals(dueDate, that.dueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, description, status, priority, executorUsername, dueDate);
    }

    @Override
    public String toString() {
        return "UpdateTaskDTO{" +
                "head='" + head + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", executorUsername='" + executorUsername + '\'' +
                ", dueDate=" + dueDate +
                '}';
    }
}