package com.hoolhool.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoolhool.backend.entity.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {
    
    // 특정 게시글, 댓글, 대댓글의 좋아요 여부 확인
    Optional<Like> findByUserIdAndTypeAndBoardId(String userId, String type, Long boardId);
    Optional<Like> findByUserIdAndTypeAndCommentId(String userId, String type, Long commentId);
    Optional<Like> findByUserIdAndTypeAndRecommentId(String userId, String type, Long recommentId);

    // 특정 게시글, 댓글, 대댓글의 전체 좋아요 수
    long countByTypeAndBoardId(String type, Long boardId);
    long countByTypeAndCommentId(String type, Long commentId);
    long countByTypeAndRecommentId(String type, Long recommentId);

    // 특정 게시글, 댓글, 대댓글의 좋아요 삭제
    void deleteByUserIdAndTypeAndBoardId(String userId, String type, Long boardId);
    void deleteByUserIdAndTypeAndCommentId(String userId, String type, Long commentId);
    void deleteByUserIdAndTypeAndRecommentId(String userId, String type, Long recommentId);

    // 게시글 ID로 좋아요 삭제
    void deleteByBoardId(Long boardId);

    // 댓글 ID로 좋아요 삭제
    void deleteByCommentId(Long commentId);

    // 대댓글 ID로 좋아요 삭제
    void deleteByRecommentId(Long recommentId);

}
