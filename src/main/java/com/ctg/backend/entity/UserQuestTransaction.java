package com.ctg.backend.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;



@Entity
@Getter
@Setter  // ✅ 추가하면 자동으로 setter가 생성됨
@Table(name = "user_quest_transaction")
public class UserQuestTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_quest_id", nullable = false)
    private Long userQuestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id", referencedColumnName = "quest_id", nullable = false)
    private Quest quest;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "ENUM('IN_PROGRESS', 'COMPLETED')")
    private UserQuestTransactionStatus status;

    @Column(name = "progress", nullable = false)
    private Integer progress;

    @Column(name = "u_q_date")
    private LocalDateTime uQDate;

    public Integer getProgress() {
        return progress;
    }
    
    public void setProgress(Integer progress) {
        this.progress = progress;
    }
    
    public void setStatus(UserQuestTransactionStatus status) {
        this.status = status;
    }
    
    public UserQuestTransactionStatus getStatus() {
        return status;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public void setQuest(Quest quest) {
        this.quest = quest;
    }
    
    public void setUQDate(LocalDateTime uQDate) {
        this.uQDate = uQDate;
    }
    
}
