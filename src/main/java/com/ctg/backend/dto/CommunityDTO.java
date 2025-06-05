package com.ctg.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ctg.backend.entity.Community;
import com.ctg.backend.entity.CommunityType;
import com.ctg.backend.entity.ContentStatus;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class CommunityDTO {
    private Long boardId;
    private Long userId;
    private Long domainId;
    private String title;
    private String content;
    private CommunityType communityType;
    private ContentStatus contentStatus;
    private Integer view;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentDTO> comments;
    private List<ImageDTO> images;
    private String userNickname;
    private String userProfileImage;
    private boolean isLiked;

    public CommunityDTO(Community community) {
        this.boardId = community.getBoardId();
        this.userId = community.getUser().getUserId();
        this.domainId = community.getDomain().getDomainId();
        this.title = community.getTitle();
        this.content = community.getContent();
        this.communityType = community.getCommunityType();
        this.contentStatus = community.getContentStatus();
        this.view = community.getView();
        this.createdAt = community.getCreatedAt();
        this.updatedAt = community.getUpdatedAt();
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
    public Long getDomainId() {
        return domainId;
    }
    public void setDomainId(Long domainId) {
        this.domainId = domainId;
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
    public CommunityType getCommunityType() {
        return communityType;
    }
    public void setCommunityType(CommunityType communityType) {
        this.communityType = communityType;
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