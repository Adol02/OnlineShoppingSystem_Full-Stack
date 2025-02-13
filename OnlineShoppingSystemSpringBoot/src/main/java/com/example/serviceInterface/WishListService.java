package com.example.serviceInterface;

import org.springframework.stereotype.Service;

import com.example.dto.WishListDTO;
import com.example.model.Product;
import com.example.model.User;


public interface WishListService {
	 public WishListDTO addToWishlist(User user, Product product) ;
	 public WishListDTO removeFromWishlist(User user, int productId);
	 public WishListDTO getWishlist(User user);
	 public boolean isProductInWishlist(User user, Product product);
}
