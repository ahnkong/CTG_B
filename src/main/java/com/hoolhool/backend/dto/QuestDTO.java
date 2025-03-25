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
    private Integer requiredAttempts;
    private Integer progress; // ✅ 진행도 추가 (필수)

    // ✅ Getter & Setter 추가
    public Long getQuestId() {
        return questId;
    }

    public void setQuestId(Long questId) {
        this.questId = questId;
    }

    public String getQName() { // ❌ 기존 getqName → ✅ getQName (대소문자 일관성 유지)
        return qName;
    }

    public void setQName(String qName) {
        this.qName = qName;
    }

    public String getQDescription() { // ❌ 기존 getqDescription → ✅ getQDescription
        return qDescription;
    }

    public void setQDescription(String qDescription) {
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

    public Integer getRequiredAttempts() {
        return requiredAttempts;
    }

    public void setRequiredAttempts(Integer requiredAttempts) {
        this.requiredAttempts = requiredAttempts;
    }

    public Integer getProgress() { // ✅ 진행도 Getter 추가
        return progress;
    }

    public void setProgress(Integer progress) { // ✅ 진행도 Setter 추가
        this.progress = progress;
    }
}
