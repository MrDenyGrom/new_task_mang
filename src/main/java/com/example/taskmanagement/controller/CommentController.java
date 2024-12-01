package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.CommentDTO;
import com.example.taskmanagement.exception.TaskNotFoundException;
import com.example.taskmanagement.model.Comment;
import com.example.taskmanagement.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/comments")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @Operation(summary = "Create a new comment for a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = {@Content}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content}),
            @ApiResponse(responseCode = "404", description = "Task not found", content = {@Content})
    })
    @PostMapping("/{taskId}/create")
    public ResponseEntity<Comment> createComment(@PathVariable("taskId") long taskId,
                                                 @Valid @RequestBody CommentDTO commentDTO,
                                                 Authentication authentication) {
        try {
            logger.info("Received request to create comment for task ID: {}", taskId);
            Comment createdComment = commentService.createComment(taskId, commentDTO, authentication);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

    }


    @Operation(summary = "Get all comments for a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments retrieved", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Task not found", content = {@Content})
    })
    @GetMapping("/{taskId}/comments")
    public ResponseEntity<List<Comment>> getAllCommentsForTask(@PathVariable("taskId") long taskId) {
        try {
            logger.info("Fetching comments for task ID: {}", taskId);
            List<Comment> comments = commentService.getCommentsByTaskId(taskId);
            return ResponseEntity.ok(comments);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @Operation(summary = "Delete a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comment deleted", content = {@Content}),
            @ApiResponse(responseCode = "404", description = "Comment not found", content = {@Content})
    })
    @DeleteMapping("/{commentId}/delete")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") long commentId) {
        logger.info("Deleting comment with ID: {}", commentId);
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Update a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment updated", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = {@Content}),
            @ApiResponse(responseCode = "404", description = "Comment not found", content = {@Content})
    })
    @PutMapping("/{commentId}/update")
    public ResponseEntity<Comment> updateComment(@PathVariable("commentId") long commentId, @Valid @RequestBody CommentDTO commentDTO) {
        logger.info("Updating comment with ID: {}", commentId);
        Comment updatedComment = commentService.updateComment(commentId, commentDTO);
        return ResponseEntity.ok(updatedComment);
    }

    @GetMapping("/myComments")
    public ResponseEntity<List<Comment>> getMyComments(Authentication authentication) {
        logger.info("Fetching comments for user: {}", authentication.getName());
        List<Comment> comments = commentService.getCommentsByUsername(authentication.getName());
        return ResponseEntity.ok(comments);
    }
}