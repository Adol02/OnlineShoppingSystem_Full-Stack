package com.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

@Entity
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Store the image data as a byte array
    @Lob
    @Column(name = "imageData", columnDefinition = "LONGBLOB")
    private byte[] imageData;

    private String imageName;  // e.g., image file name
    
 // Many-to-one relationship with Product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    // Additional fields (optional)
    private String contentType;  // e.g., image/jpeg
    private long size;  // image size in bytes

    // Getters and setters

    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public byte[] getImageData() {
		return imageData;
	}
	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	
}
