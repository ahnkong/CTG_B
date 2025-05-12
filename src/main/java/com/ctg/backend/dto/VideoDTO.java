package com.ctg.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.ctg.backend.entity.Video;
import com.ctg.backend.entity.ContentStatus;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class VideoDTO {
    private Long boardId;
    private Long userId;
    private String title;
    private String videoUrl;
    private String peacher;
    private LocalDate videoDate;
    private String reference;
    private String subTitle;
    private Integer view;
    private ContentStatus contentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentDTO> comments;
    private List<ImageDTO> images;
    private String userNickname;
    private String userProfileImage;
    private boolean isLiked;

    public VideoDTO(Video video) {
        this.boardId = video.getBoardId();
        this.userId = video.getUser().getUserId();
        this.title = video.getTitle();
        this.videoUrl = video.getVideoUrl();
        this.peacher = video.getPeacher();
        this.videoDate = video.getVideoDate();
        this.reference = video.getReference();
        this.subTitle = video.getSubTitle();
        this.view = video.getView();
        this.contentStatus = video.getContentStatus();
        this.createdAt = video.getCreatedAt();
        this.updatedAt = video.getUpdatedAt();
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
    public String getVideoUrl() {
        return videoUrl;
    }
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
    public String getPeacher() {
        return peacher;
    }
    public void setPeacher(String peacher) {
        this.peacher = peacher;
    }
    public LocalDate getVideoDate() {
        return videoDate;
    }
    public void setVideoDate(LocalDate videoDate) {
        this.videoDate = videoDate;
    }
    public String getReference() {
        return reference;
    }
    public void setReference(String reference) {
        this.reference = reference;
    }
    public String getSubTitle() {
        return subTitle;
    }
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
    public Integer getView() {
        return view;
    }
    public void setView(Integer view) {
        this.view = view;
    }
    public ContentStatus getContentStatus() {
        return contentStatus;
    }
    public void setContentStatus(ContentStatus contentStatus) {
        this.contentStatus = contentStatus;
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