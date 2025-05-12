package com.ctg.backend.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.ctg.backend.entity.Like;
import com.ctg.backend.entity.LikeType;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    
    // 게시판 타입의 좋아요 조회
    Optional<Like> findByUser_UserIdAndLikeTypeAndTargetId(Long userId, LikeType likeType, Long targetId);

    // 댓글 좋아요 조회
    Optional<Like> findByUser_UserIdAndLikeTypeAndComment_CommentId(Long userId, LikeType likeType, Long commentId);

    // 대댓글 좋아요 조회
    Optional<Like> findByUser_UserIdAndLikeTypeAndReComment_RecommentId(Long userId, LikeType likeType, Long recommentId);

    // 게시판 타입의 좋아요 삭제
    @Modifying
    @Transactional
    void deleteByUser_UserIdAndLikeTypeAndTargetId(Long userId, LikeType likeType, Long targetId);

    // 댓글 좋아요 삭제
    @Modifying
    @Transactional
    void deleteByUser_UserIdAndLikeTypeAndComment_CommentId(Long userId, LikeType likeType, Long commentId);

    // 대댓글 좋아요 삭제
    @Modifying
    @Transactional
    void deleteByUser_UserIdAndLikeTypeAndReComment_RecommentId(Long userId, LikeType likeType, Long recommentId);

    // 게시판 타입의 좋아요 개수 조회
    long countByLikeTypeAndTargetId(LikeType likeType, Long targetId);

    // 댓글 좋아요 개수 조회
    long countByLikeTypeAndComment_CommentId(LikeType likeType, Long commentId);

    // 대댓글 좋아요 개수 조회
    long countByLikeTypeAndReComment_RecommentId(LikeType likeType, Long recommentId);

    // 댓글 ID로 좋아요 삭제
    void deleteByComment_CommentId(Long commentId);

    // 대댓글 ID로 좋아요 삭제
    void deleteByReComment_RecommentId(Long recommentId);

}
