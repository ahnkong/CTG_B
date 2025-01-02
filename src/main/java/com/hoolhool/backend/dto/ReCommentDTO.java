package com.hoolhool.backend.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ReCommentDTO {
    private Long recommentId;
    private Long commentId;
    private String userId;
    private String content;
    private LocalDateTime reCDate;
    private Integer reLikes;
    
    public Long getRecommentId() {
        return recommentId;
    }
    public void setRecommentId(Long recommentId) {
        this.recommentId = recommentId;
    }
    public Long getCommentId() {
        return commentId;
    }
    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
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
    public Integer getReLikes() {
        return reLikes;
    }
    public void setReLikes(Integer reLikes) {
        this.reLikes = reLikes;
    }
}
