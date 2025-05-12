package com.ctg.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ctg.backend.entity.BoardType;
import com.ctg.backend.entity.Comment;
import com.ctg.backend.entity.ContentStatus;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long commentId;
    private String content;
    private LocalDateTime createdAt;
    private ContentStatus contentStatus;
    private LocalDateTime updatedAt;
    private BoardType boardType;
    private Long boardId;
    private Long userId;
    private String userNickname;
    private String userProfileImage;
    private List<ReCommentDTO> reComments;
    private Long likeCount;
    private boolean isLiked;
    private boolean isEdited;

    public CommentDTO(Comment comment) {
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.contentStatus = comment.getContentStatus();
        this.updatedAt = comment.getUpdatedAt();
        this.boardType = comment.getBoardType();
        this.boardId = comment.getBoardId();
        this.userId = comment.getUser().getUserId();
        this.userNickname = comment.getUser().getNickname();
        this.userProfileImage = comment.getUser().getProfileImage();
        this.likeCount = comment.getLikeCount();
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
    public ContentStatus getContentStatus() {
        return contentStatus;
    }
    public void setContentStatus(ContentStatus contentStatus) {
        this.contentStatus = contentStatus;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public BoardType getBoardType() {
        return boardType;
    }
    public void setBoardType(BoardType boardType) {
        this.boardType = boardType;
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
    public List<ReCommentDTO> getReComments() {
        return reComments;
    }
    public void setReComments(List<ReCommentDTO> reComments) {
        this.reComments = reComments;
    }
    public Long getLikeCount() {
        return likeCount;
    }
    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
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