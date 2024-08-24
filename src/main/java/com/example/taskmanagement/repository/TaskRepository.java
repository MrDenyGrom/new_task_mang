package com.example.taskmanagement.repository;

import com.example.taskmanagement.model.AppUser;
import com.example.taskmanagement.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserAuthor(AppUser userAuthor);
    List<Task> findByUserExecutor(AppUser executor);
    Optional<Task> findByIdAndUserAuthorUsername(long id, String username);
    List<Task> findByUserAuthorOrUserExecutor(AppUser author, AppUser executor);
    List<Task> findAllByUserAuthorUsername(String username);
}