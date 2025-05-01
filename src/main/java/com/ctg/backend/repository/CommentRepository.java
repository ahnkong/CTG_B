package com.ctg.backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ctg.backend.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    // 특정 게시글에 속한 댓글 조회
    List<Comment> findByBoard_BoardId(Long boardId);

    // 특정 사용자가 작성한 댓글 조회
    List<Comment> findByUserId(String userId);

    // 특정 댓글 내용 검색 (키워드 포함)
    @Query("SELECT c FROM Comment c WHERE c.content LIKE %:keyword%")
    List<Comment> searchCommentsByKeyword(@Param("keyword") String keyword);

    // 댓글 삭제 (특정 게시글의 모든 댓글 삭제)
    void deleteByBoard_BoardId(Long boardId);

    // 특정 댓글에 속한 대댓글만 조회
    List<Comment> findByReComments_RecommentId(Long recommentId);

    // 특정 댓글에 있는 댓글 갯수 반환
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.board.boardId = :boardId")
    long countCommentsByBoardId(@Param("boardId") Long boardId);

    // // ✅ 특정 유저가 작성한 댓글 찾기
    List<Comment> findByUserIdOrderByCoCDateDesc(String userId);

}
