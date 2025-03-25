package com.hoolhool.backend.dto;

import java.time.LocalDateTime;

import com.hoolhool.backend.entity.ReComment;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ReCommentDTO {
    private Long recommentId;
    private String userId; 
    private Long commentId; 
    private String content;
    private LocalDateTime reCDate;
    private String userNickname;
    private String userProfileImage;
    private boolean isLiked;
    private boolean isEdited;
    
    public ReCommentDTO(ReComment reComment) {
        this.recommentId = reComment.getRecommentId();
        this.userId = reComment.getUserId();
        this.content = reComment.getContent();
        this.reCDate = reComment.getReCDate();
        // userNickname, userProfileImage, isLiked는 setter로 나중에 설정
    }

    public ReCommentDTO(Long recommentId, String userId, Long commentId, String content,
            LocalDateTime reCDate, boolean isLiked) {
        this.recommentId = recommentId;
        this.userId = userId;
        this.commentId = commentId;
        this.content = content;
        this.reCDate = reCDate;
        this.isLiked = isLiked; // ✅ isLiked 추가
    }

    public ReCommentDTO(Long recommentId, String userId, Long commentId, String content, LocalDateTime reCDate) {
        this.recommentId = recommentId;
        this.userId = userId;
        this.commentId = commentId;
        this.content = content;
        this.reCDate = reCDate;
    }


    public Long getRecommentId() {
        return recommentId;
    }
    public void setRecommentId(Long recommentId) {
        this.recommentId = recommentId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
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
    public LocalDateTime getReCDate() {
        return reCDate;
    }
    public void setReCDate(LocalDateTime reCDate) {
        this.reCDate = reCDate;
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
