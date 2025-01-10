package com.hoolhool.backend.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name = "point_transaction")
public class PointTransaction {
    @Id
    @Column(name = "transaction_id", nullable = false)
    private Long transactionId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "change_type", nullable = false, columnDefinition = "ENUM('EARN', 'SPEND')")
    private PointTransactionChangeType changeType;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "pointtransaction_date", nullable = false)
    private LocalDateTime pointTransactionDate;
}
