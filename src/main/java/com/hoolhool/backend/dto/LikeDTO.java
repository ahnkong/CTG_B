package com.hoolhool.backend.dto;

import java.time.LocalDateTime;

import com.hoolhool.backend.entity.LikeType;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class LikeDTO {
    private Long likeId;
    private String userId;
    private LikeType type;
    private String targetId;
    private LocalDateTime likeDate;
    private Long boardId;
    private Long commentId;
    private Long recommentId;
    
    public Long getLikeId() {
        return likeId;
    }
    public void setLikeId(Long likeId) {
        this.likeId = likeId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getTargetId() {
        return targetId;
    }
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
    public LocalDateTime getLikeDate() {
        return likeDate;
    }
    public void setLikeDate(LocalDateTime likeDate) {
        this.likeDate = likeDate;
    }
    public Long getBoardId() {
        return boardId;
    }
    public void setBoardId(Long boardId) {
        this.boardId = boardId;
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
    public LikeType getType() {
        return type;
    }
    public void setType(LikeType type) {
        this.type = type;
    }
    
}
