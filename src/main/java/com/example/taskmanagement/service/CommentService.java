package com.example.taskmanagement.service;

import com.example.taskmanagement.model.Comment;
import com.example.taskmanagement.dto.CommentDTO;
import com.example.taskmanagement.repository.CommentRepository;
import com.example.taskmanagement.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    public CommentService(CommentRepository commentRepository, TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional
    public Comment createComment(long taskId, CommentDTO commentDTO, Authentication authentication) {
        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Task not found with id " + taskId);
        }

        Comment comment = new Comment();
        comment.setTask(taskId);
        comment.setAppUser(authentication.getName());
        comment.setText(commentDTO.getText());

        logger.info("Creating comment {} for task with id {} by user {}", comment, taskId, authentication.getName());
        return commentRepository.save(comment);
    }

    public List<Comment> getAllComments(long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Task not found with id " + taskId);
        }
        logger.info("Retrieving all comments for task with id {}", taskId);
        return commentRepository.findByTask(taskId);
    }
}
