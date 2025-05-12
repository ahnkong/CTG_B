package com.ctg.backend.dto;

import java.time.LocalDateTime;

import com.ctg.backend.entity.ReComment;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ReCommentDTO {
    private Long recommentId;
    private Long userId;
    private Long commentId; 
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userNickname;
    private String userProfileImage;
    private boolean isLiked;
    private boolean isEdited;
    
    public ReCommentDTO(ReComment reComment) {
        this.recommentId = reComment.getRecommentId();
        this.userId = reComment.getUser().getUserId();
        this.commentId = reComment.getComment().getCommentId();
        this.content = reComment.getContent();
        this.createdAt = reComment.getCreatedAt();
        this.updatedAt = reComment.getUpdatedAt();
    }

    public ReCommentDTO(Long recommentId, Long userId, Long commentId, String content,
            LocalDateTime createdAt, LocalDateTime updatedAt, boolean isLiked) {
        this.recommentId = recommentId;
        this.userId = userId;
        this.commentId = commentId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isLiked = isLiked;
    }

    public ReCommentDTO(Long recommentId, Long userId, Long commentId, String content, 
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.recommentId = recommentId;
        this.userId = userId;
        this.commentId = commentId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getRecommentId() {
        return recommentId;
    }

    public void setRecommentId(Long recommentId) {
        this.recommentId = recommentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean isEdited) {
        this.isEdited = isEdited;
    }
}
