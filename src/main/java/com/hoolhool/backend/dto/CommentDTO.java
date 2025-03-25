package com.hoolhool.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.hoolhool.backend.entity.Comment;

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
    private String userNickname;
    private String userProfileImage;
    private boolean isLiked;
    private boolean isEdited;
    
    
    public CommentDTO(Comment comment) {
        this.commentId = comment.getCommentId();
        this.userId = comment.getUserId();
        this.boardId = comment.getBoard().getBoardId(); // Board 객체에서 ID 가져옴
        this.content = comment.getContent();
        this.coCDate = comment.getCoCDate();
        // 이 시점엔 reComments는 setReComments(...)로 따로 세팅해줄 거니까 생략
    }
    
    public CommentDTO(Long commentId, String userId, Long boardId, String content,
            LocalDateTime coCDate, List<ReCommentDTO> reComments, boolean isLiked, boolean isEdited) {
        this.commentId = commentId;
        this.userId = userId;
        this.boardId = boardId;
        this.content = content;
        this.coCDate = coCDate;
        this.reComments = reComments;
        this.isLiked = isLiked; // ✅ isLiked 추가
        this.isEdited = isEdited;
    }

    public CommentDTO(Long commentId, String userId, Long boardId, String content, LocalDateTime coCDate,
            List<ReCommentDTO> reComments) {
        this.commentId = commentId;
        this.userId = userId;
        this.boardId = boardId;
        this.content = content;
        this.coCDate = coCDate;
        this.reComments = reComments;
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