package com.hoolhool.backend.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class QuestDTO {
    private Long questId;
    private String qName;
    private String qDescription;
    private Integer rewardPoint;
    private Boolean isActive;
    private LocalDateTime questDate;
    private Long userQuestId;
    
    public Long getQuestId() {
        return questId;
    }
    public void setQuestId(Long questId) {
        this.questId = questId;
    }
    public String getqName() {
        return qName;
    }
    public void setqName(String qName) {
        this.qName = qName;
    }
    public String getqDescription() {
        return qDescription;
    }
    public void setqDescription(String qDescription) {
        this.qDescription = qDescription;
    }
    public Integer getRewardPoint() {
        return rewardPoint;
    }
    public void setRewardPoint(Integer rewardPoint) {
        this.rewardPoint = rewardPoint;
    }
    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    public LocalDateTime getQuestDate() {
        return questDate;
    }
    public void setQuestDate(LocalDateTime questDate) {
        this.questDate = questDate;
    }
    public Long getUserQuestId() {
        return userQuestId;
    }
    public void setUserQuestId(Long userQuestId) {
        this.userQuestId = userQuestId;
    }
}
