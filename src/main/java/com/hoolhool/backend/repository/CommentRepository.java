package com.hoolhool.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hoolhool.backend.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    // 특정 게시글에 속한 댓글 조회
    List<Comment> findByBoardId(Long boardId);

    // 특정 사용자가 작성한 댓글 조회
    List<Comment> findByUserId(String userId);

    // 특정 댓글 내용 검색 (키워드 포함)
    @Query("SELECT c FROM Comment c WHERE c.content LIKE %:keyword%")
    List<Comment> searchCommentsByKeyword(@Param("keyword") String keyword);

    // 댓글 삭제 (특정 게시글의 모든 댓글 삭제)
    void deleteByBoardId(Long boardId);

    // 특정 댓글에 속한 대댓글만 조회
    List<Comment> findByReCommentId(Long reCommentId);
}
