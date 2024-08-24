package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.CommentDTO;
import com.example.taskmanagement.model.Comment;
import com.example.taskmanagement.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{taskId}/create")
    public ResponseEntity<Comment> createComment(@PathVariable("taskId") long taskId,
                                                 @Valid @RequestBody CommentDTO commentDTO,
                                                 Authentication authentication) {
        logger.info("User {} is creating a comment for task with id {}: {}",
                authentication.getName(), taskId, commentDTO);
        Comment createdComment = commentService.createComment(taskId, commentDTO, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @GetMapping("/{taskId}/comments")
    public ResponseEntity<List<Comment>> getAllComments(@PathVariable("taskId") long taskId) {
        logger.info("Retrieving all comments for task with id {}", taskId);
        List<Comment> comments = commentService.getAllComments(taskId);
        return ResponseEntity.ok(comments);
    }
}
