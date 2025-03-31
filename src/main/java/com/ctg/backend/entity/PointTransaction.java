package com.ctg.backend.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter // ✅ Getter 자동 생성
@Setter // ✅ Setter 자동 생성
@AllArgsConstructor
@Table(name = "point_transaction")
public class PointTransaction {
    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Column(name = "transaction_id", nullable = false)
    // private Long transactionId;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable =
    // false)
    // private User user;

    // @Enumerated(EnumType.STRING)
    // @Column(name = "change_type", nullable = false, columnDefinition =
    // "ENUM('EARN', 'SPEND')")
    // private PointTransactionChangeType changeType;

    // @Column(name = "amount", nullable = false)
    // private Integer amount;

    // @Column(name = "description", columnDefinition = "TEXT")
    // private String description;

    // @Column(name = "pointtransaction_date", nullable = false)
    // private LocalDateTime pointTransactionDate;
    // }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id", nullable = false)
    private Long transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "change_type", nullable = false, columnDefinition = "ENUM('EARN', 'SPEND')")
    private PointTransactionChangeType changeType;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "pointtransaction_date", nullable = false)
    private LocalDateTime pointTransactionDate;

    // ✅ 기본 생성자 (필수)
    public PointTransaction() {
    }

    // ✅ 모든 필드를 포함하는 생성자 추가
    public PointTransaction(User user, PointTransactionChangeType changeType, Integer amount, String description,
            LocalDateTime pointTransactionDate) {
        this.user = user;
        this.changeType = changeType;
        this.amount = amount;
        this.description = description;
        this.pointTransactionDate = pointTransactionDate;
    }

    public void setDate(LocalDateTime date) {
        this.pointTransactionDate = date;
    }
    
    public void setType(String type) {
        this.changeType = PointTransactionChangeType.valueOf(type.toUpperCase());
    }
    

}