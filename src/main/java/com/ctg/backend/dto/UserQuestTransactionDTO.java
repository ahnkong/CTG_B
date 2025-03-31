package com.ctg.backend.dto;

import java.time.LocalDateTime;

import com.ctg.backend.entity.UserQuestTransactionStatus;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class UserQuestTransactionDTO {
    private Long userQuestId;
    private UserDTO user; // 사용자 정보 포함
    private QuestDTO quest; // 퀘스트 정보 포함
    private UserQuestTransactionStatus status;
    private Integer progress;
    private LocalDateTime uQDate;

    
    public Long getUserQuestId() {
        return userQuestId;
    }
    public void setUserQuestId(Long userQuestId) {
        this.userQuestId = userQuestId;
    }
    public UserDTO getUser() {
        return user;
    }
    public void setUser(UserDTO user) {
        this.user = user;
    }
    public QuestDTO getQuest() {
        return quest;
    }
    public void setQuest(QuestDTO quest) {
        this.quest = quest;
    }
    public UserQuestTransactionStatus getStatus() {
        return status;
    }
    public void setStatus(UserQuestTransactionStatus status) {
        this.status = status;
    }
    public Integer getProgress() {
        return progress;
    }
    public void setProgress(Integer progress) {
        this.progress = progress;
    }
    public LocalDateTime getuQDate() {
        return uQDate;
    }
    public void setuQDate(LocalDateTime uQDate) {
        this.uQDate = uQDate;
    }
    
    
    
}
