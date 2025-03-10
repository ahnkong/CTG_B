package com.hoolhool.backend.entity;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Long productId; // 상품 ID

    @Column(name = "product_code", nullable = false, unique = true)
    private String productCode; // 상품 코드 (예: SB_coffee_Americano)

    @Column(name = "brand", nullable = false)
    private String brand; // 브랜드 코드 (SB, CB, MG 등)

    @Column(name = "category", nullable = false)
    private String category; // 카테고리 코드 (coffee, bread 등)

    @Column(name = "product_name", nullable = false)
    private String productName; // 상품명 (아메리카노, 베이글 등)

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // 상품 설명

    @Column(name = "price", nullable = false)
    private Integer price; // 가격

    @Column(name = "stock", nullable = false)
    private Integer stock; // 재고 수량

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 상품 등록일

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPurchase> purchases; // 구매 내역
}
