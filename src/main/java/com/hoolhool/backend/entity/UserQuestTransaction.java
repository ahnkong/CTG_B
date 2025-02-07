package com.hoolhool.backend.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
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
}
