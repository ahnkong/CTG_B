package com.ctg.backend.dto;

import java.time.LocalDateTime;

import com.ctg.backend.entity.Like;
import com.ctg.backend.entity.LikeType;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class LikeDTO {    
    private Long likeId;
    private LikeType likeType;  // COMMUNITY, NOTICE, WORSHIP, COMMENT, RECOMMENT
    private Long userId;
    private LocalDateTime createdAt;
    private Long targetId;  // likeType에 따라 board_id, comment_id, recomment_id 값이 들어감
    private Long commentId;  // 댓글 좋아요인 경우에만 사용
    private Long recommentId;  // 대댓글 좋아요인 경우에만 사용

    public LikeDTO(Like like) {
        this.likeId = like.getLikeId();
        this.likeType = like.getLikeType();
        this.userId = like.getUser().getUserId();
        this.createdAt = like.getCreatedAt();
        this.targetId = like.getTargetId();
        if (like.getComment() != null) {
            this.commentId = like.getComment().getCommentId();
        }
        if (like.getReComment() != null) {
            this.recommentId = like.getReComment().getRecommentId();
        }
    }
    
    public Long getLikeId() {
        return likeId;
    }
    public void setLikeId(Long likeId) {
        this.likeId = likeId;
    }
    public LikeType getLikeType() {
        return likeType;
    }
    public void setLikeType(LikeType likeType) {
        this.likeType = likeType;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public Long getTargetId() {
        return targetId;
    }
    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
    public Long getCommentId() {
        return commentId;
    }
    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
    public Long getRecommentId() {
        return recommentId;
    }
    public void setRecommentId(Long recommentId) {
        this.recommentId = recommentId;
    }
}
