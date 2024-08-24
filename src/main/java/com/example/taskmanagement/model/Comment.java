package com.example.taskmanagement.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private long task;

    @Column(nullable = false)
    private String appUser;

    @Column(nullable = false)
    private String text;


    public Comment() {
    }

    public Comment(long id, long task, String appUser, String text) {
        this.id = id;
        this.task = task;
        this.appUser = appUser;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTask() {
        return task;
    }

    public void setTask(long task) {
        this.task = task;
    }

    public String getAppUser() {
        return appUser;
    }

    public void setAppUser(String appUser) {
        this.appUser = appUser;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id && task == comment.task && appUser.equals(comment.appUser) && text.equals(comment.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, task, appUser, text);
    }
}
