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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productId")
    private int productId;

    @Column(name = "category")
    private String category;

    @Column(name = "productname")
    private String productname;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;

    @Column(name = "quantity")
    private int quantity;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();
    
    public void addImage(ProductImage image) {
        images.add(image);
        image.setProduct(this);  // Set the product reference in the image
    }

    // Method to remove an image
    public void removeImage(ProductImage image) {
        images.remove(image);
        image.setProduct(null);  // Remove product reference from image
    }

//    @Lob
//    @Column(name = "imageData", columnDefinition = "LONGBLOB")
//    private byte[] imageData;

    public List<ProductImage> getImages() {
		return images;
	}

	public void setImages(List<ProductImage> images) {
		this.images = images;
	}

	public List<CartProduct> getCartproduct() {
		return cartproduct;
	}

	public void setCartproduct(List<CartProduct> cartproduct) {
		this.cartproduct = cartproduct;
	}

	public List<WishList> getWishList() {
		return wishList;
	}

	public void setWishList(List<WishList> wishList) {
		this.wishList = wishList;
	}

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CartProduct> cartproduct;

    @ManyToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<WishList> wishList;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Rating> rating;

    // Constructors, getters, and setters

    public Product() {}

    public Product(String category, String productname, String description, double price, int quantity) {
        this.category = category;
        this.productname = productname;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }



    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public List<Rating> getRating() {
        return rating;
    }

    public void setRating(List<Rating> rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Product [id=" + productId + ", category=" + category + ", name=" + productname + ", description=" + description + ", price=" + price + ", quantity=" + quantity + "]";
    }
}
