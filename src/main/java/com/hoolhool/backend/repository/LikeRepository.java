package com.hoolhool.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoolhool.backend.entity.Like;
import com.hoolhool.backend.entity.LikeType;

public interface LikeRepository extends JpaRepository<Like, Long> {
    
     // 특정 게시글, 댓글, 대댓글의 좋아요 여부 확인
    Optional<Like> findByUser_UserIdAndTypeAndBoard_BoardId(String userId, LikeType type, Long boardId);
    Optional<Like> findByUser_UserIdAndTypeAndComment_CommentId(String userId, LikeType type, Long commentId);
    Optional<Like> findByUser_UserIdAndTypeAndReComment_RecommentId(String userId, LikeType type, Long recommentId);

    // 특정 게시글, 댓글, 대댓글의 전체 좋아요 수
    long countByTypeAndBoard_BoardId(LikeType type, Long boardId);
    long countByTypeAndComment_CommentId(LikeType type, Long commentId);
    long countByTypeAndReComment_RecommentId(LikeType type, Long recommentId);

    // 특정 게시글, 댓글, 대댓글의 좋아요 삭제
    void deleteByUser_UserIdAndTypeAndBoard_BoardId(String userId, LikeType type, Long boardId);
    void deleteByUser_UserIdAndTypeAndComment_CommentId(String userId, LikeType type, Long commentId);
    void deleteByUser_UserIdAndTypeAndReComment_RecommentId(String userId, LikeType type, Long recommentId);

    // 게시글 ID로 좋아요 삭제
    void deleteByBoard_BoardId(Long boardId);

    // 댓글 ID로 좋아요 삭제
    void deleteByComment_CommentId(Long commentId);

    // 대댓글 ID로 좋아요 삭제
    void deleteByReComment_RecommentId(Long recommentId);

}
