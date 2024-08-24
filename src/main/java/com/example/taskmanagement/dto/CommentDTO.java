package com.example.taskmanagement.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CommentDTO {

    @NotNull(message = "Comment text cannot be null")
    @Size(min = 1, max = 1000, message = "Comment text must be between 1 and 1000 characters")
    private String text;

    public CommentDTO() {
    }

    public CommentDTO(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
