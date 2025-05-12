package com.ctg.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ctg.backend.entity.Community;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
    Page<Community> findByUserUserId(String userId, Pageable pageable);
} 