package com.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long discountId;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private double discountValue;

    private String code;

    private double minimumCartValue;

    private boolean active;

    // Getters and setters

    public enum DiscountType {
        PERCENTAGE,
        FIXED,
        COUPON
    }
    public Long getDiscountId() {
		return discountId;
	}

	public void setDiscountId(Long discountId) {
		this.discountId = discountId;
	}

	public DiscountType getDiscountType() {
		return discountType;
	}

	public void setDiscountType(DiscountType discountType) {
		this.discountType = discountType;
	}

	public double getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(double discountValue) {
		this.discountValue = discountValue;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public double getMinimumCartValue() {
		return minimumCartValue;
	}

	public void setMinimumCartValue(double minimumCartValue) {
		this.minimumCartValue = minimumCartValue;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	

    // Constructors, Getters, Setters, and other methods...
}

