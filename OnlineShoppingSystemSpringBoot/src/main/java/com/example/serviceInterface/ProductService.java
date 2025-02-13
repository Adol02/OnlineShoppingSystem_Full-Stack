package com.example.serviceInterface;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.model.Product;

public interface ProductService {
	public void addProduct(Product product);
	 public List<Product> getAllProducts();
	 public void updateProduct(Product product);
	 public void deleteProduct(String name);
	 public Product getProductByName(String productName);
	 public List<Product> getByCategory(String category);
	 public List<String> getAllCategories();
	 public void reduceProductQuantity(String name, int quantity);
	 public List<byte[]> getProductImagesById(int productId);
	 public Optional<Product> getProductById(int id);
	 
	 
	 
}
