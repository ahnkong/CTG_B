package com.hoolhool.backend.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name = "user_quest_transaction")
public class UserQuestTransaction {
    @Id
    @Column(name = "user_quest_id", nullable = false)
    private Long userQuestId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "quest_id", nullable = false)
    private Long questId;

    @Column(name = "status", nullable = false, columnDefinition = "ENUM('IN_PROGRESS', 'COMPLETED')")
    private String status;

    @Column(name = "progress", nullable = false)
    private Integer progress;

    @Column(name = "u_q_date")
    private LocalDateTime uQDate;
}

