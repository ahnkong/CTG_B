package com.hoolhool.backend.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "quest")
public class Quest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quest_id", nullable = false)
    private Long questId;

    @Column(name = "q_name", nullable = false)
    private String qName;

    @Column(name = "q_description", columnDefinition = "TEXT")
    private String qDescription;

    @Column(name = "reward_point", nullable = false)
    private Integer rewardPoint;

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT 1")
    private Boolean isActive;

    @Column(name = "quest_date", nullable = false)
    private LocalDateTime questDate;

    @OneToMany(mappedBy = "quest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserQuestTransaction> questTransactions;
}
