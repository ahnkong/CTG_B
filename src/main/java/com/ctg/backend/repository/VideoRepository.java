package com.ctg.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ctg.backend.entity.Video;
import com.ctg.backend.entity.ContentStatus;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    
    // ACTIVE 상태의 게시글만 조회 (videoDate 기준 내림차순)
    Page<Video> findByContentStatusOrderByVideoDateDesc(ContentStatus status, Pageable pageable);
    
    // 특정 사용자의 ACTIVE 상태 게시글 조회 (videoDate 기준 내림차순)
    Page<Video> findByUser_UserIdAndContentStatusOrderByVideoDateDesc(Long userId, ContentStatus status, Pageable pageable);
    
    // 특정 도메인의 ACTIVE 상태 게시글 조회 (videoDate 기준 내림차순)
    Page<Video> findByDomain_DomainIdAndContentStatusOrderByVideoDateDesc(Long domainId, ContentStatus status, Pageable pageable);
    
    // 제목 또는 내용으로 검색 (ACTIVE 상태만, videoDate 기준 내림차순)
    @Query("SELECT v FROM Video v WHERE v.contentStatus = :status AND (v.title LIKE %:search% OR v.subTitle LIKE %:search%) ORDER BY v.videoDate DESC")
    Page<Video> searchByTitleOrSubTitle(@Param("search") String search, @Param("status") ContentStatus status, Pageable pageable);
    
    // 설교자로 검색 (ACTIVE 상태만, videoDate 기준 내림차순)
    Page<Video> findByPeacherAndContentStatusOrderByVideoDateDesc(String peacher, ContentStatus status, Pageable pageable);
    
    // 조회수 기준 정렬 (ACTIVE 상태만)
    @Query("SELECT v FROM Video v WHERE v.contentStatus = :status ORDER BY v.view DESC")
    Page<Video> findAllOrderByViewDesc(@Param("status") ContentStatus status, Pageable pageable);
    
    // 좋아요 수 기준 정렬 (ACTIVE 상태만)
    @Query("SELECT v FROM Video v WHERE v.contentStatus = :status ORDER BY SIZE(v.likes) DESC")
    Page<Video> findAllOrderByLikesDesc(@Param("status") ContentStatus status, Pageable pageable);
} 