package com.example.serviceInterface;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dto.CartDTO;


public interface CartService {
	public CartDTO addToCart(int productId, int quantity, Principal principal);
	public CartDTO updateCartProduct(int productId, int newQuantity, Principal principal);
	public CartDTO removeFromCart(int productId, Principal principal);
	public CartDTO getCartForUser(String username);
	public void clearCart(String username);
	public List<String> fetchProductImage(int productID);
	
	
}
