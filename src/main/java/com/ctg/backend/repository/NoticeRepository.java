package com.ctg.backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ctg.backend.entity.Notice;
import com.ctg.backend.entity.ContentStatus;
import com.ctg.backend.entity.NoticeType;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    
    // ACTIVE 상태의 게시글만 조회 (createdAt 기준 내림차순)
    Page<Notice> findByContentStatusOrderByCreatedAtDesc(ContentStatus status, Pageable pageable);
    
    // 특정 사용자의 ACTIVE 상태 게시글 조회 (createdAt 기준 내림차순)
    Page<Notice> findByUser_UserIdAndContentStatusOrderByCreatedAtDesc(Long userId, ContentStatus status, Pageable pageable);
    
    // 특정 도메인의 ACTIVE 상태 게시글 조회 (createdAt 기준 내림차순)
    Page<Notice> findByDomain_DomainIdAndContentStatusOrderByCreatedAtDesc(Long domainId, ContentStatus status, Pageable pageable);
    
    // 제목 또는 내용으로 검색 (ACTIVE 상태만, createdAt 기준 내림차순)
    @Query("SELECT n FROM Notice n WHERE n.contentStatus = :status AND (n.title LIKE %:search% OR n.content LIKE %:search%) ORDER BY n.createdAt DESC")
    Page<Notice> searchByTitleOrContent(@Param("search") String search, @Param("status") ContentStatus status, Pageable pageable);
    
    // NoticeType별 ACTIVE 상태 게시글 조회 (createdAt 기준 내림차순)
    Page<Notice> findByNoticeTypeAndContentStatusOrderByCreatedAtDesc(NoticeType noticeType, ContentStatus status, Pageable pageable);
    
    // 특정 사용자의 특정 NoticeType 게시글 조회 (createdAt 기준 내림차순)
    Page<Notice> findByUser_UserIdAndNoticeTypeAndContentStatusOrderByCreatedAtDesc(
        Long userId, NoticeType noticeType, ContentStatus status, Pageable pageable);
    
    // 조회수 기준 정렬 (ACTIVE 상태만)
    @Query("SELECT n FROM Notice n WHERE n.contentStatus = :status ORDER BY n.view DESC")
    Page<Notice> findAllOrderByViewDesc(@Param("status") ContentStatus status, Pageable pageable);
    
    // 좋아요 수 기준 정렬 (ACTIVE 상태만)
    @Query("SELECT n FROM Notice n WHERE n.contentStatus = :status ORDER BY SIZE(n.likes) DESC")
    Page<Notice> findAllOrderByLikesDesc(@Param("status") ContentStatus status, Pageable pageable);
    
    // 날짜 범위로 검색 (ACTIVE 상태만)
    @Query("SELECT n FROM Notice n WHERE n.contentStatus = :status AND n.createdAt BETWEEN :startDate AND :endDate")
    Page<Notice> findByDateRange(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("status") ContentStatus status,
        Pageable pageable
    );
    
    // 특정 기간 동안의 인기 게시글 조회 (ACTIVE 상태만)
    @Query("SELECT n FROM Notice n WHERE n.contentStatus = :status AND n.createdAt >= :startDate ORDER BY SIZE(n.likes) DESC")
    Page<Notice> findPopularNoticesByDateRange(
        @Param("startDate") LocalDateTime startDate,
        @Param("status") ContentStatus status,
        Pageable pageable
    );
    
    // NoticeType과 검색어로 검색 (ACTIVE 상태만)
    @Query("SELECT n FROM Notice n WHERE n.contentStatus = :status AND n.noticeType = :noticeType AND (n.title LIKE %:search% OR n.content LIKE %:search%)")
    Page<Notice> searchByTypeAndKeyword(
        @Param("noticeType") NoticeType noticeType,
        @Param("search") String search,
        @Param("status") ContentStatus status,
        Pageable pageable
    );
} 