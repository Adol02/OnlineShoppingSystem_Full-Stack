package com.example.repository;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.model.Cart;
import com.example.model.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
	 // Custom method to get cart items by username
//    ArrayList<Cart> findByUsername(String username);

    // Custom method to delete cart item by product name
//    void deleteByProductName(String productName);

	Optional<Cart> findByUserId(Integer id);
}
