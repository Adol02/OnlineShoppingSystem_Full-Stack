package com.example.serviceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.Product;
import com.example.model.ProductImage;
import com.example.repository.ProductRepository;
import com.example.serviceInterface.ProductService;

import jakarta.persistence.EntityNotFoundException;
 
@Service
@Transactional
public class ProductServiceImpl implements ProductService {
	 
	 @Autowired
	    private ProductRepository productRepo;

	    public void addProduct(Product product) {
	        productRepo.save(product);
	    }
	    public List<Product> getAllProducts() {
	        return productRepo.findAll();
	    }
//	    @Transactional
//	    public void addProducts(List<Product> products) {
//	        int batchSize = 5; // Set the batch size based on your needs
//
//	        for (int i = 0; i < products.size(); i++) {
//	            productRepo.save(products.get(i));
//
//	            if (i % batchSize == 0 && i > 0) {
//	                // Flush and clear the entity manager to prevent memory issues
//	                productRepo.flush();
//	            }
//	        }
//	    }
	   

	    public void updateProduct(Product product) {
	        // Assuming the update is just saving the modified product
	        productRepo.save(product);
	    }

	    public void deleteProduct(String name) {
	    	 Product product = productRepo.findByProductname(name);
	         
	         if (product != null) {
	             // This will delete the product and cascade the delete to the images
	             productRepo.delete(product);
	         } else {
	             throw new EntityNotFoundException("Product not found with name: " + name);
	         }
	    }

	    public Product getProductByName(String productName) {
	        return productRepo.findByProductname(productName);
	    }

	    public List<Product> getByCategory(String category) {
	        return productRepo.findByCategory(category);
	    }

	    public List<String> getAllCategories() {
	        return productRepo.findAllCategories();
	    }

	    public void reduceProductQuantity(String name, int quantity) {
	        productRepo.reduceProductQuantity(name, quantity);
	    }
	    
//		public byte[] getProductImageById(int productId) {
//        Product product = productRepo.findById(productId).orElse(null);
//        if (product != null) {
//            return product.getImageData();  // Assuming `imageData` is a byte array in your Product entity
//        }
//        return null;
//    }
		public List<byte[]> getProductImagesById(int productId) {
		    Product product = productRepo.findById(productId).orElse(null);
		    if (product != null) {
		    	System.out.println("jiiii");
		        // Map the product's images to a list of byte arrays (image data)
		        return product.getImages().stream()
		                .map(ProductImage::getImageData)  // Assuming `getImageData()` returns the byte array for each image
		                .collect(Collectors.toList());
		    }
		    return Collections.emptyList();  // Return an empty list if the product is not found or has no images
		}
		public Optional<Product> getProductById(int id) {
			 return productRepo.findById(id);
		}
	}
