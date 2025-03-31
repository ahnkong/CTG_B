package com.ctg.backend.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "quest")
public class Quest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quest_id", nullable = false)
    private Long questId; // 퀘스트 아이디

    @Column(name = "q_name", nullable = false)
    private String qName; // 퀘스트 이름

    @Column(name = "q_description", columnDefinition = "TEXT")
    private String qDescription; // 퀘스트 설명

    @Column(name = "reward_point", nullable = false)
    private Integer rewardPoint; // 퀘스트 수행했을 때 얻을 수 있는 포인트

    @Column(name = "required_attempts", nullable = false) // ⭐️ 추가된 부분
    private Integer requiredAttempts; // 퀘스트 완료까지 필요한 수행 횟수

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT 1")
    private Boolean isActive; //

    @Column(name = "quest_date", nullable = false)
    private LocalDateTime questDate;

    @Column(name = "point", nullable = false)
    private Integer point;

    @OneToMany(mappedBy = "quest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserQuestTransaction> questTransactions;

    // ✅ getter/setter 추가
    public Integer getRequiredAttempts() {
        return requiredAttempts;
    }

    public void setRequiredAttempts(Integer requiredAttempts) {
        this.requiredAttempts = requiredAttempts;
    }

    public Integer getRewardPoint() {
        return rewardPoint;
    }
    
}
