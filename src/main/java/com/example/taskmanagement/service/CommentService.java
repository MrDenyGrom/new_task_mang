package com.example.taskmanagement.service;

import com.example.taskmanagement.dto.CommentDTO;
import com.example.taskmanagement.exception.CommentNotFoundException;
import com.example.taskmanagement.exception.ResourceNotFoundException;
import com.example.taskmanagement.exception.TaskNotFoundException;
import com.example.taskmanagement.model.AppUser;
import com.example.taskmanagement.model.Comment;
import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.repository.CommentRepository;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.repository.UserRepository;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Comment createComment(long taskId, CommentDTO commentDTO, Authentication authentication) {
        logger.info("Creating comment for task with ID: {}", taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + taskId));

        AppUser user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Comment comment = new Comment();
        comment.setText(commentDTO.getText());
        comment.setTask(task);
        comment.setAppUser(user);

        Comment savedComment = commentRepository.save(comment);
        logger.info("Comment created with ID: {}", savedComment.getId());
        return savedComment;
    }

    public List<Comment> getCommentsByTaskId(long taskId) {
        logger.info("Retrieving comments for task ID: {}", taskId);
        if (!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException("Task not found with ID: " + taskId);
        }
        return commentRepository.findByTaskId(taskId);
    }

    public void deleteComment(long commentId) {
        logger.info("Deleting comment with ID: {}", commentId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with ID: " + commentId));
        commentRepository.delete(comment);
        logger.info("Comment with ID: {} deleted", commentId);
    }

    @Transactional
    public Comment updateComment(Long commentId, CommentDTO commentDTO) {
        logger.info("Updating comment with ID: {}", commentId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with ID: " + commentId));
        comment.setText(commentDTO.getText());
        comment.setUpdatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByUsername(String username) {
        logger.info("Retrieving comments for user: {}", username);
        return commentRepository.findByUsername(username);
    }
}
