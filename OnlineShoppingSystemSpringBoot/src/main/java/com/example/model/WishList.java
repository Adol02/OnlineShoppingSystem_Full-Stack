package com.example.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;


@Entity
public class WishList {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="wishListID")
	private int wishListID;
	
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private User user;
    

	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(
	        name = "wishlist_product",
	        joinColumns = @JoinColumn(name = "wishlist_id"),
	        inverseJoinColumns = @JoinColumn(name = "product_id")
	    )
    private List<Product> product;
    
    

	public int getWishListID() {
		return wishListID;
	}

	public void setWishListID(int wishListID) {
		this.wishListID = wishListID;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Product> getProduct() {
		return product;
	}

	public void setProduct(List<Product> product) {
		this.product = product;
	}
	
	
}
