package com.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class CartProduct {
	public CartProduct() {}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cpId;

	@ManyToOne()
	@JoinColumn(name = "cart_id")
	private Cart cart;

	public Long getCpId() {
		return cpId;
	}

	private double price;
	private double total_pprice;
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	private int cpquantity; // Quantity added to the cart

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getTotal_pprice() {
		return total_pprice;
	}

	public void setTotal_pprice(double total_pprice) {
		this.total_pprice = total_pprice;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public CartProduct(Cart cart, Product product, int quantity) {
		this.cart = cart;
		this.product = product;
		this.cpquantity = quantity;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getCpquantity() {
		return cpquantity;
	}

	public void setCpquantity(int cpquantity) {
		this.cpquantity = cpquantity;
	}

	// Getters and Setters
}
