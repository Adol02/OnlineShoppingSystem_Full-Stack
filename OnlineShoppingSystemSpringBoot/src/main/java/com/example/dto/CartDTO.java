package com.example.dto;

import java.util.List;

public class CartDTO {
    private int cartId;
    private int userId;
    private List<CartProductDTO> cartProducts;
    private double subtotal;
    private double discount;
    private double total;
    private double shippingcost;

    public CartDTO(int cartId, int userId, List<CartProductDTO> cartProducts, double subtotal, double discount, double total, double shippingcost) {
        this.cartId = cartId;
        this.userId = userId;
        this.cartProducts = cartProducts;
        this.subtotal = subtotal;
        this.discount = discount;
        this.total = total;
        this.shippingcost = shippingcost;
    }

    // Getters and Setters
    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<CartProductDTO> getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(List<CartProductDTO> cartProducts) {
        this.cartProducts = cartProducts;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getShippingcost() {
        return shippingcost;
    }

    public void setShippingcost(double shippingcost) {
        this.shippingcost = shippingcost;
    }
}
