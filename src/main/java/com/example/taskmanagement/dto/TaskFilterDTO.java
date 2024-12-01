package com.example.taskmanagement.dto;

import com.example.taskmanagement.model.Priority;
import com.example.taskmanagement.model.Status;

import java.sql.Date;
import java.util.Objects;

public class TaskFilterDTO {
    private String head;
    private String description;
    private Status status;
    private Priority priority;
    private String authorUsername;
    private String executorUsername;
    private java.sql.Date startDate;
    private java.sql.Date endDate;

    public TaskFilterDTO(String head, String description, Status status, Priority priority, String authorUsername, String executorUsername, Date startDate, Date endDate) {
        this.head = head;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.authorUsername = authorUsername;
        this.executorUsername = executorUsername;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public TaskFilterDTO() {
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

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public String getExecutorUsername() {
        return executorUsername;
    }

    public void setExecutorUsername(String executorUsername) {
        this.executorUsername = executorUsername;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskFilterDTO that = (TaskFilterDTO) o;
        return Objects.equals(head, that.head) && Objects.equals(description, that.description) && status == that.status && priority == that.priority && Objects.equals(authorUsername, that.authorUsername) && Objects.equals(executorUsername, that.executorUsername) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, description, status, priority, authorUsername, executorUsername, startDate, endDate);
    }

    @Override
    public String toString() {
        return "TaskFilterDTO{" +
                "head='" + head + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", authorUsername='" + authorUsername + '\'' +
                ", executorUsername='" + executorUsername + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}