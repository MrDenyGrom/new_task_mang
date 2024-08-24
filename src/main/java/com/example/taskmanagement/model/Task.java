package com.example.taskmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, length = 100)
    private String head;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    @JsonIgnoreProperties("hibernateLazyInitializer")
    private AppUser userAuthor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "executor_id")
    private AppUser userExecutor;

    public Task(String head, String description, Status status, Priority priority, AppUser userAuthor, AppUser userExecutor) {
        this.head = head;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.userAuthor = userAuthor;
        this.userExecutor = userExecutor;
    }

    public Task(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public AppUser getUserAuthor() {
        return userAuthor;
    }

    public void setUserAuthor(AppUser userAuthor) {
        this.userAuthor = userAuthor;
    }

    public AppUser getUserExecutor() {
        return userExecutor;
    }

    public void setUserExecutor(AppUser userExecutor) {
        this.userExecutor = userExecutor;
    }

    public boolean isAuthor(String username) {
        return userAuthor.getUsername().equals(username);
    }

    public boolean isExecutor(String username) {
        return userExecutor != null && userExecutor.getUsername().equals(username);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}