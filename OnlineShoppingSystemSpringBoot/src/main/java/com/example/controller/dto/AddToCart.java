package com.example.dto;

import lombok.Data;

@Data
public class AddToCart {
    private int productId;
    private int quantity;
 
    // Getters and Setters
    public int getProductId() {
        return productId;
    }
 
    public void setProductId(int productId) {
        this.productId = productId;
    }
 
    public int getQuantity() {
        return quantity;
    }
 
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
