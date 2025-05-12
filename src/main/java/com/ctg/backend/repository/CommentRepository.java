package com.ctg.backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ctg.backend.entity.BoardType;
import com.ctg.backend.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 댓글에 속한 대댓글만 조회
    List<Comment> findByReComments_RecommentId(Long recommentId);

    // 특정 게시글의 댓글 수 반환 (활성화된 댓글만)
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.boardType = :boardType AND c.boardId = :boardId AND c.contentStatus = 'ACTIVE'")
    long countCommentsByBoardTypeAndBoardId(@Param("boardType") BoardType boardType, @Param("boardId") Long boardId);

    // 특정 게시글의 활성화된 댓글만 조회
    @Query("SELECT c FROM Comment c WHERE c.boardType = :boardType AND c.boardId = :boardId AND c.contentStatus = 'ACTIVE'")
    List<Comment> findActiveCommentsByBoardTypeAndBoardId(
        @Param("boardType") BoardType boardType,
        @Param("boardId") Long boardId
    );

    // 특정 사용자의 활성화된 댓글만 조회 (생성일 기준 내림차순)
    @Query("SELECT c FROM Comment c WHERE c.user.userId = :userId AND c.contentStatus = 'ACTIVE' ORDER BY c.createdAt DESC")
    List<Comment> findActiveCommentsByUserId(@Param("userId") Long userId);

    // 활성화된 댓글만 검색
    @Query("SELECT c FROM Comment c WHERE c.content LIKE %:keyword% AND c.contentStatus = 'ACTIVE'")
    List<Comment> searchActiveCommentsByKeyword(@Param("keyword") String keyword);

    // 특정 게시글의 모든 댓글을 DELETED 상태로 변경
    @Modifying
    @Query("UPDATE Comment c SET c.contentStatus = 'DELETED', c.updatedAt = CURRENT_TIMESTAMP " +
           "WHERE c.boardType = :boardType AND c.boardId = :boardId AND c.contentStatus = 'ACTIVE'")
    void markCommentsAsDeletedByBoardTypeAndBoardId(
        @Param("boardType") BoardType boardType,
        @Param("boardId") Long boardId
    );

    // 특정 댓글을 DELETED 상태로 변경
    @Modifying
    @Query("UPDATE Comment c SET c.contentStatus = 'DELETED', c.updatedAt = CURRENT_TIMESTAMP " +
           "WHERE c.commentId = :commentId AND c.contentStatus = 'ACTIVE'")
    void markCommentAsDeleted(@Param("commentId") Long commentId);
}
