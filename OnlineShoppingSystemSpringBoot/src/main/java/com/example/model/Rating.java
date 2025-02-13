package com.example.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Rating {
	@Override
	public String toString() {
		return "Rating [ratingid=" + ratingid + ", user=" + user + ", product=" + product + ", rating=" + rating
				+ ", review=" + review + "]";
	}
	public Rating(int ratingid, User user, Product product, int rating, String review) {
		super();
		this.ratingid = ratingid;
		this.user = user;
		this.product = product;
		this.rating = rating;
		this.review = review;
	}
	public Rating() {
		// TODO Auto-generated constructor stub
	}
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ratingid")
	private int ratingid;
	
	 @ManyToOne(cascade = CascadeType.ALL)
	    @JoinColumn(name= "username")
	    private User user;
	 
	 public int getRatingid() {
		return ratingid;
	}
	public void setRatingid(int ratingid) {
		this.ratingid = ratingid;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getReview() {
		return review;
	}
	public void setReview(String review) {
		this.review = review;
	}
	@ManyToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	 @JoinColumn(name= "productname")
    private Product product;
	 
	 
    
    @Column(name="rating")
    private int rating;
    @Column(name="review")
    private String review;
    
    
}
