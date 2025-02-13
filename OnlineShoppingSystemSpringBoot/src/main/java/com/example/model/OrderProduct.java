package com.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class OrderProduct {

    @EmbeddedId
    private OrderProductKey id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Orders order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Column(name = "product_price")
    private double productPrice;

    // Stores the total price for this order item (product_price * quantity)
    @Column(name = "total_price")
    private double totalPrice;

    // Quantity ordered
    @Column(name = "quantity")
    private int quantity;

    // Category of the product at the time of purchase
    @Column(name = "category")
    private String category;

    // Constructors, getters, and setters

    public OrderProduct() {}

    public OrderProduct(Orders order, Product product, int quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.id = new OrderProductKey(order.getOrderID(), product.getProductId());
    }

    public OrderProductKey getId() {
        return id;
    }

    public void setId(OrderProductKey id) {
        this.id = id;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    @Override
    public String toString() {
        return "OrderProduct{" +
                "id=" + id +
                ", productName=" + (product != null ? product.getProductname() : "null") +
                ", quantity=" + quantity +
                ", price=" + productPrice +
                '}';
    }
}
