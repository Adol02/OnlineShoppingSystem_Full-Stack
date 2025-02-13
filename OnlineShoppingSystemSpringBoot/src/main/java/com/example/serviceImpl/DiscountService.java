//package com.example.service;
//
//import java.security.Principal;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import com.example.dto.CartDTO;
//import com.example.model.Cart;
//import com.example.model.CartProduct;
//import com.example.model.User;
//import com.example.repository.OrdersRepository;
//
////public class DiscountService {
////	@Autowired
////	private OrdersRepository orderRepo;
////	public boolean isFirstPurchase(User user) {
////	    return orderRepo.countByUserId(user.getId()) == 0;
////	}
////	public double applyFirst20Discount(double totalPrice) {
////	    return totalPrice * 0.80;  // 20% off
////	}
////	public double applyMin499Discount(double totalPrice, double deliveryCharge) {
////	    if (totalPrice >= 499) {
////	        return 0;  // No delivery charge
////	    }
////	    return deliveryCharge;
////	}
////	public double calculateTotalPrice(Cart cart, User user) {
////	    double totalPrice = 0.0;
////	    double deliveryCharge = 50.0;  // Assume a default delivery charge
////
////	    // Sum up the price of all products in the cart
////	    for (CartProduct cartProduct : cart.getCartProduct()) {
////	        totalPrice += cartProduct.getProduct().getPrice() * cartProduct.getCpquantity();
////	    }
////
////	    // Apply `FIRST20` discount if it's the user's first purchase
////	    if (isFirstPurchase(user)) {
////	        totalPrice = applyFirst20Discount(totalPrice);
////	    }
////
////	    // Apply `MIN499` discount to remove delivery charge if applicable
////	    deliveryCharge = applyMin499Discount(totalPrice, deliveryCharge);
////
////	    // Add delivery charge to total price
////	    totalPrice += deliveryCharge;
////
////	    return totalPrice;
////	}
////	public CartDTO updateCartWithDiscounts(Principal principal) {
////	    String username = principal.getName();
////	    User user = userRepo.findByEmail(username)
////	                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
////
////	    Cart cart = cartRepo.findByUserId(user.getId())
////	                        .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
////
////	    double totalPrice = calculateTotalPrice(cart, user);
////
////	    // Assuming CartDTO has a method to set the total price
////	    CartDTO cartDTO = convertToCartDTO(cart);
////	    cartDTO.setTotalPrice(totalPrice);
////
////	    return cartDTO;
////	}
//
//}
//package com;


