package com.hoolhool.backend.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "produce_date", nullable = false)
    private LocalDateTime produceDate;

    @Column(name = "purchase_id")
    private Long purchaseId;
}

