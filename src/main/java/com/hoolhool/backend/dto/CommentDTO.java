package com.hoolhool.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long commentId;
    private String userId;
    private Long boardId;
    private String content;
    private LocalDateTime coCDate;
    private List<ReCommentDTO> reComments; // 대댓글 포함
    
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
    public Long getBoardId() {
        return boardId;
    }
    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public LocalDateTime getCoCDate() {
        return coCDate;
    }
    public void setCoCDate(LocalDateTime coCDate) {
        this.coCDate = coCDate;
    }
    public List<ReCommentDTO> getReComments() {
        return reComments;
    }
    public void setReComments(List<ReCommentDTO> reComments) {
        this.reComments = reComments;
    }
    

}