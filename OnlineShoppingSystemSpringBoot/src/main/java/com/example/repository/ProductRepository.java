package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.Product;
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
	   
    // Custom query to find a product by name
	Product findByProductname(String productName);
	Optional<Product> findById(int id);
	

    // Custom query to delete a product by name
    @Transactional
    void deleteByProductname(String name);

    // Custom query to find products by category
    List<Product> findByCategory(String category);

    // Example of a custom update method using @Query
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.quantity = p.quantity - :quantity WHERE p.productname = :productname")
    void reduceProductQuantity(@Param("productname") String productName, @Param("quantity") int quantity);

    @Query("SELECT DISTINCT p.category FROM Product p")
    List<String> findAllCategories();
}
