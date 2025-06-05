package com.ctg.backend.repository;

import com.ctg.backend.entity.Community;
import com.ctg.backend.entity.ContentStatus;
import com.ctg.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
    
    List<Community> findByUserAndContentStatus(User user, ContentStatus contentStatus);
    
    @Query("SELECT c FROM Community c WHERE c.contentStatus = :status AND " +
           "(LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Community> searchByTitleOrContent(@Param("keyword") String keyword, @Param("status") ContentStatus status);
    
    List<Community> findByContentStatus(ContentStatus contentStatus);
} 