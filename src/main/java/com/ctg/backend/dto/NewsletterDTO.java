package com.ctg.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ctg.backend.entity.Newsletter;
import com.ctg.backend.entity.ContentStatus;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class NewsletterDTO {
    private Long boardId;
    private Long userId;
    private String title;
    private String content;
    private ContentStatus contentStatus;
    private Integer view;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentDTO> comments;
    private List<ImageDTO> images;
    private String userNickname;
    private String userProfileImage;
    private boolean isLiked;

    public NewsletterDTO(Newsletter newsletter) {
        this.boardId = newsletter.getBoardId();
        this.userId = newsletter.getUser().getUserId();
        this.title = newsletter.getTitle();
        this.content = newsletter.getContent();
        this.contentStatus = newsletter.getContentStatus();
        this.view = newsletter.getView();
        this.createdAt = newsletter.getCreatedAt();
        this.updatedAt = newsletter.getUpdatedAt();
    }

    public Long getBoardId() {
        return boardId;
    }
    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public ContentStatus getContentStatus() {
        return contentStatus;
    }
    public void setContentStatus(ContentStatus contentStatus) {
        this.contentStatus = contentStatus;
    }
    public Integer getView() {
        return view;
    }
    public void setView(Integer view) {
        this.view = view;
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
    public List<CommentDTO> getComments() {
        return comments;
    }
    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }
    public List<ImageDTO> getImages() {
        return images;
    }
    public void setImages(List<ImageDTO> images) {
        this.images = images;
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
} 