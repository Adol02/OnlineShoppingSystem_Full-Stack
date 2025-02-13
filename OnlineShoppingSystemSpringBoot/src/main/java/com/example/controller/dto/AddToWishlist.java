package com.example.dto;

public class AddToWishlist {
    private int productId;

    public AddToWishlist() {}

    public AddToWishlist(int productId) {
        this.productId = productId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
