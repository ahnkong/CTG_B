package com.hoolhool.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long commentId;
    private Long boardId;
    private String userId;
    private String content;
    private LocalDateTime coCDate;
    private Integer coLikes;
    private Long reCommentId;
    private List<ReCommentDTO> reComments; // 대댓글 리스트 추가

    
    public Long getCommentId() {
        return commentId;
    }
    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
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
    public LocalDateTime getCoCDate() {
        return coCDate;
    }
    public void setCoCDate(LocalDateTime coCDate) {
        this.coCDate = coCDate;
    }
    public Integer getCoLikes() {
        return coLikes;
    }
    public void setCoLikes(Integer coLikes) {
        this.coLikes = coLikes;
    }
    public Long getReCommentId() {
        return reCommentId;
    }
    public void setReCommentId(Long reCommentId) {
        this.reCommentId = reCommentId;
    }
    public List<ReCommentDTO> getReComments() {
        return reComments;
    }
    public void setReComments(List<ReCommentDTO> reComments) {
        this.reComments = reComments;
    }

    
}