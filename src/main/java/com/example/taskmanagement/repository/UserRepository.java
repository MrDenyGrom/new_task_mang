package com.example.taskmanagement.repository;

import com.example.taskmanagement.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByEmail(String email);

    @Modifying
    @Query("UPDATE AppUser u SET u.password = :newPassword WHERE u.username = :username")
    int updatePassword(@Param("username") String username, @Param("newPassword") String newPassword);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
