package com.hoolhool.backend.dto;

import java.time.LocalDateTime;

import com.hoolhool.backend.entity.PointTransactionChangeType;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class PointTransactionDTO {
    private Long transactionId;
    private UserDTO user; // 사용자 정보 포함
    private PointTransactionChangeType changeType;
    private Integer amount;
    private String description;
    private LocalDateTime pointTransactionDate;
    
    public Long getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }
    public UserDTO getUser() {
        return user;
    }
    public void setUser(UserDTO user) {
        this.user = user;
    }
    public PointTransactionChangeType getChangeType() {
        return changeType;
    }
    public void setChangeType(PointTransactionChangeType changeType) {
        this.changeType = changeType;
    }
    public Integer getAmount() {
        return amount;
    }
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDateTime getPointTransactionDate() {
        return pointTransactionDate;
    }
    public void setPointTransactionDate(LocalDateTime pointTransactionDate) {
        this.pointTransactionDate = pointTransactionDate;
    }
    
    
    
}
