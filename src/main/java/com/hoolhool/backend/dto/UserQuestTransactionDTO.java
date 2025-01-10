package com.hoolhool.backend.dto;

import java.time.LocalDateTime;

import com.hoolhool.backend.entity.UserQuestTransactionStatus;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class UserQuestTransactionDTO {
    private Long userQuestId;
    private String userId;
    private Long questId;
    private UserQuestTransactionStatus status;
    private Integer progress;
    private LocalDateTime uQDate;
    
    public Long getUserQuestId() {
        return userQuestId;
    }
    public void setUserQuestId(Long userQuestId) {
        this.userQuestId = userQuestId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Long getQuestId() {
        return questId;
    }
    public void setQuestId(Long questId) {
        this.questId = questId;
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
    public UserQuestTransactionStatus getStatus() {
        return status;
    }
    public void setStatus(UserQuestTransactionStatus status) {
        this.status = status;
    }
}
