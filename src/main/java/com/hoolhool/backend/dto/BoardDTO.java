package com.hoolhool.backend.dto;

import java.time.LocalDateTime;

import com.hoolhool.backend.entity.BoardType;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    private Long boardId;
    private String userId;
    private String content;
    private LocalDateTime cDate;
    private String hashTag;
    private Boolean hidden;
    private String title;
    private Integer view;
    private BoardType type;
    private Long commentId;
    private String status; // 상태: DRAFT or PUBLISHED
    private LocalDateTime lastSavedAt; // 마지막 임시 저장 시간

    
    public Long getBoardId() {
        return boardId;
    }
    public void setBoardId(Long boardId) {
        this.boardId = boardId;
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
    public LocalDateTime getcDate() {
        return cDate;
    }
    public void setcDate(LocalDateTime cDate) {
        this.cDate = cDate;
    }
    public String getHashTag() {
        return hashTag;
    }
    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }
    public Boolean getHidden() {
        return hidden;
    }
    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Integer getView() {
        return view;
    }
    public void setView(Integer view) {
        this.view = view;
    }
    public Long getCommentId() {
        return commentId;
    }
    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
    public BoardType getType() {
        return type;
    }
    public void setType(BoardType type) {
        this.type = type;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public LocalDateTime getLastSavedAt() {
        return lastSavedAt;
    }
    public void setLastSavedAt(LocalDateTime lastSavedAt) {
        this.lastSavedAt = lastSavedAt;
    }
    
    
}
