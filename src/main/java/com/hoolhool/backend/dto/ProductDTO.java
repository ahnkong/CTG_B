package com.hoolhool.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long productId;
    private String productName;
    private String description;
    private Integer price;
    private Integer stock;
    private LocalDateTime produceDate;
    private List<UserPurchaseDTO> purchases; // 구매 정보 포함
    
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getPrice() {
        return price;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }
    public Integer getStock() {
        return stock;
    }
    public void setStock(Integer stock) {
        this.stock = stock;
    }
    public LocalDateTime getProduceDate() {
        return produceDate;
    }
    public void setProduceDate(LocalDateTime produceDate) {
        this.produceDate = produceDate;
    }
    public List<UserPurchaseDTO> getPurchases() {
        return purchases;
    }
    public void setPurchases(List<UserPurchaseDTO> purchases) {
        this.purchases = purchases;
    }
    
    
}