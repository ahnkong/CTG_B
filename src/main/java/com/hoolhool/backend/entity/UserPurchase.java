package com.hoolhool.backend.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name = "user_purchase")
public class UserPurchase {
    @Id
    @Column(name = "purchase_id", nullable = false)
    private Long purchaseId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @Column(name = "purchase_date", nullable = false)
    private LocalDateTime purchaseDate;
}
