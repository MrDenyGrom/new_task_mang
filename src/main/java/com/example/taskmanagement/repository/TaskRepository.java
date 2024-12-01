package com.example.taskmanagement.repository;

import com.example.taskmanagement.model.AppUser;
import com.example.taskmanagement.model.Status;
import com.example.taskmanagement.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    List<Task> findByUserAuthorOrUserExecutor(AppUser author, AppUser executor);

    List<Task> findByStatus(Status status);

    List<Task> findAllByUserAuthorUsername(String username);

    @Query("SELECT t.id FROM Task t")
    List<Long> getAllTaskIds();

    @Query("SELECT t FROM Task t WHERE t.dueDate BETWEEN :startDate AND :endDate")
    List<Task> findByDueDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Modifying
    @Query("UPDATE Task t SET t.status = :status WHERE t.id = :taskId")
    int updateTaskStatus(@Param("taskId") Long taskId, @Param("status") Status status);

}
