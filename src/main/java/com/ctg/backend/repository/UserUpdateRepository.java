package com.ctg.backend.repository;

import com.ctg.backend.entity.UserUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserUpdateRepository extends JpaRepository<UserUpdate, Long> {
    Optional<UserUpdate> findByUser_UserId(Long userId);
} 