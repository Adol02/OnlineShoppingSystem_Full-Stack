package com.example.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cartID")
	private int cartID;

	private double subtotal;
	private double discount;
	private double total;
	private double shippingcost;

	public double getShippingcost() {
		return shippingcost;
	}

	public void setShippingcost(double shippingcost) {
		this.shippingcost = shippingcost;
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

//	@Column(name ="cartquantity")
//	private int cartquantity;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id")
	private User user;

	public Cart() {
	}

	public Cart(User user, List<CartProduct> cartProducts) {
		this.user = user;
		this.cartProduct = cartProducts;
	}

	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<CartProduct> cartProduct = new ArrayList<>();

	public List<CartProduct> getCartProduct() {
		return cartProduct;
	}

	public void setCartProduct(List<CartProduct> cartProduct) {
		this.cartProduct = cartProduct;
	}

	public int getCartID() {
		return cartID;
	}

	public void setCartID(int cartID) {
		this.cartID = cartID;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

//	 public void addProduct(Product product, int quantity) {
//	        for (CartProduct cartProduct : cartProduct) {
//	            if (cartProduct.getProduct().getProductId() == product.getProductId()) {
//	                cartProduct.setCpquantity(cartProduct.getCpquantity() + quantity);
//	                return;
//	            }
//	        }
//	        CartProduct newCartProduct = new CartProduct(this, product, quantity);
//	        cartProduct.add(newCartProduct);
//	    }

	public void removeProduct(Product product) {
		cartProduct.removeIf(cartProduct -> cartProduct.getProduct().getProductId() == product.getProductId());
	}

	public void updateProductQuantity(Product product, int quantity) {
		for (CartProduct cartProduct : cartProduct) {
			if (cartProduct.getProduct().getProductId() == product.getProductId()) {
				cartProduct.setCpquantity(quantity);
				return;
			}
		}
	}

	public void addCartProduct(CartProduct newCartProduct) {
		newCartProduct.setCart(this); // Set the cart reference in the CartProduct
		this.cartProduct.add(newCartProduct);

	}

}
