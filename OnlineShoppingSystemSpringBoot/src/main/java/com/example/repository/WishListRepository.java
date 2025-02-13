package com.example.repository;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//import java.util.ArrayList;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
import com.example.model.WishList;
@Repository
public interface WishListRepository extends JpaRepository<WishList, Integer>{
	Optional<WishList> findByUserId(Integer id);
//	 // Custom method to get wishlist items by username
//    ArrayList<WishList> findByUsername(String username);
//
//    // Custom method to delete a product from wishlist by username and product name
//    @Modifying
//    @Query("DELETE FROM WishList w WHERE w.username = :username AND w.productName = :productName")
//    void deleteFromWishlist(@Param("username") String username, @Param("productName") String productName);
//
}
