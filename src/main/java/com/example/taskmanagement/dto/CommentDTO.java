package com.example.taskmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class CommentDTO {

    @NotBlank(message = "Comment text cannot be blank")
    @Size(min = 1, max = 1000, message = "Comment text must be between 1 and 1000 characters")
    private String text;

    public CommentDTO(String text) {
        this.text = text;
    }

    public CommentDTO() {
    }

    public @NotBlank(message = "Comment text cannot be blank") @Size(min = 1, max = 1000, message = "Comment text must be between 1 and 1000 characters") String getText() {
        return text;
    }

    public void setText(@NotBlank(message = "Comment text cannot be blank") @Size(min = 1, max = 1000, message = "Comment text must be between 1 and 1000 characters") String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentDTO that = (CommentDTO) o;
        return Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(text);
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
                "text='" + text + '\'' +
                '}';
    }
}