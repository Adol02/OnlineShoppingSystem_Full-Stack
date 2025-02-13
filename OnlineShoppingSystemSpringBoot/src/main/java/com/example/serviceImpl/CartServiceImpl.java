//package com.example.service;
//import java.security.Principal;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import com.example.dto.CartDTO;
//import com.example.dto.CartProductDTO;
//import com.example.exceptions.InvalidQuantityException;
//import com.example.exceptions.OutOfStockException;
//import com.example.exceptions.ProductNotFoundException;
//import com.example.model.Cart;
//import com.example.model.CartProduct;
//import com.example.model.Product;
//import com.example.model.User;
//import com.example.repository.CartRepository;
//import com.example.repository.UserRepository;
//import com.example.repository.ProductRepository;
//
//@Service
//public class CartService {
//	@Autowired
//	 private UserRepository userRepo;
//	@Autowired
//	 private ProductRepository productRepo;
//    @Autowired
//    private CartRepository cartRepo;
//   
////	public Cart addToCart(int productId, int quantity, Principal principal) {
////	    String username = principal.getName();
////	    System.out.println("s1"+username);
////	    User user = userRepo.findByEmail(username)
////	                              .orElseThrow(() -> new UsernameNotFoundException("User not found"));
////	    System.out.println("s14");
////	    Product product = productRepo.findById(productId);
////	    System.out.println("s2");
////	    // Check if the cart already exists for the user, else create a new one
////	    Cart cart = cartRepo.findByUserId(user.getId()).orElse(new Cart());
////	    System.out.println("s3");
////
////	    cart.setUser(user);
////	    cart.addProduct(product);  // Assuming addProduct handles adding to the product list
////	    cart.setCartquantity(quantity);
////	    System.out.println("s4");
////	    return cartRepo.save(cart);
////	}
////
////
////    public boolean removeProductFromCart(String username, int productId) {
////        User user = userRepo.findByEmail(username)
////                            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
////
////        Cart cart = cartRepo.findByUserId(user.getId()).orElse(null);
////
////        if (cart != null) {
////            List<Product> products = cart.getProduct();
////            Product productToRemove = products.stream()
////                                              .filter(product -> product.getProductId()==productId)
////                                              .findFirst()
////                                              .orElse(null);
////
////            if (productToRemove != null) {
////                products.remove(productToRemove);
////                cart.setProduct(products); // Update the cart
////                cartRepo.save(cart); // Save the changes
////                return true;
////            }
////        }
////        return false;
////    }
////
////
////	public boolean updateProductQuantityInCart(String username, int productId, int newQuantity) {
////        User user = userRepo.findByEmail(username)
////                            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
////
////        Cart cart = cartRepo.findByUserId(user.getId()).orElse(null);
////
////        if (cart != null) {
////            List<Product> products = cart.getProduct();
////            Product productToUpdate = products.stream()
////                                              .filter(product -> product.getProductId() == productId)
////                                              .findFirst()
////                                              .orElse(null);
////
////            if (productToUpdate != null) {
////                // Assuming Cart has a method to get and set the quantity for each product
////                // Update the quantity of the product in the cart
////                cart.setCartquantity(newQuantity); // Adjust based on your actual data model
////                cartRepo.save(cart);
////                return true;
////            }
////        }
////        return false;
////    }
//    
//    public CartDTO addToCart(int productId, int quantity, Principal principal) {
//    	 System.out.println("1");
//        String username = principal.getName();
//        System.out.println("2");
//        User user = userRepo.findByEmail(username)
//                            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        System.out.println("3");
//        Product product = productRepo.findById(productId)
//                                     .orElseThrow(() -> new ProductNotFoundException("Product not found"));
//        double price = product.getPrice();
//        // Check if the cart already exists for the user, else create a new one
//        Cart cart = cartRepo.findByUserId(user.getId()).orElse(new Cart());
//        cart.setUser(user);
//        Optional<CartProduct> existingCartProduct = cart.getCartProduct().stream()
//	            .filter(cp -> cp.getProduct().getProductId() == productId)
//	            .findFirst();
//
//	    if (existingCartProduct.isPresent()) {
//	        CartProduct cartProduct = existingCartProduct.get();
//	        int pquantity= cartProduct.getCpquantity() + quantity;
//	        cartProduct.setCpquantity(pquantity);
//	        cartProduct.setTotal_pprice(price*pquantity);
//	    } else {
//	        CartProduct newCartProduct = new CartProduct();
//	        newCartProduct.setProduct(product);
//	        newCartProduct.setCpquantity(quantity);
//	        newCartProduct.setPrice(price);
//	        newCartProduct.setTotal_pprice(quantity*price);
//	        cart.addCartProduct(newCartProduct);
//	    }
////        cart.addProduct(product, quantity);  // Updated to handle quantity and adding product
//        cartRepo.save(cart);
//        return convertToCartDTO(cart);
//    }
//
//    private CartDTO convertToCartDTO(Cart cart) {
//        List<CartProductDTO> cartProductDTOs = cart.getCartProduct().stream()
//                .map(cp -> new CartProductDTO(cp.getProduct().getProductId(), 
//                                               cp.getProduct().getProductname(),
//                                               cp.getCpquantity()))
//                .collect(Collectors.toList());
//
//        return new CartDTO(cart.getCartID(), 
//                           cart.getUser().getId(), 
//                           cartProductDTOs);
//    }
//    
//    public CartDTO removeFromCart(int productId, Principal principal) {
//        String username = principal.getName();
//        User user = userRepo.findByEmail(username)
//                            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        // Fetch the user's cart
//        Cart cart = cartRepo.findByUserId(user.getId())
//                            .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
//
//        // Find the cart product by productId
//        Optional<CartProduct> cartProductOptional = cart.getCartProduct().stream()
//                                                        .filter(cp -> cp.getProduct().getProductId() == productId)
//                                                        .findFirst();
//
//        if (cartProductOptional.isPresent()) {
//            // Remove the product from the cart
//            CartProduct cartProduct = cartProductOptional.get();
//            cart.getCartProduct().remove(cartProduct);
//
//            // Save the cart after modification
//            cartRepo.save(cart);
//        } else {
//            throw new IllegalArgumentException("Product not found in cart");
//        }
//
//        // Return the updated cart
//        return convertToCartDTO(cart);
//    }
//
//
//    public CartDTO updateCartProduct(int productId, int newQuantity, Principal principal) {
//        String username = principal.getName();
//        User user = userRepo.findByEmail(username)
//                            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        Cart cart = cartRepo.findByUserId(user.getId()).orElseThrow(() -> new IllegalArgumentException("Cart not found"));
//        Product product = productRepo.findById(productId)
//                .orElseThrow(() -> new ProductNotFoundException("Product not found in product table"));
//         int stockQuantity = product.getQuantity();
//        // Find the CartProduct by productId
//        CartProduct cartProduct = cart.getCartProduct().stream()
//                                      .filter(cp -> cp.getProduct().getProductId() == productId)
//                                      .findFirst()
//                                      .orElseThrow(() -> new ProductNotFoundException("Product not found in cart"));
//       // int cartQuantity = cartProduct.getCpquantity();
//        
//        if (newQuantity > stockQuantity) {
//            throw new OutOfStockException("Requested quantity exceeds available stock for product: " + product.getProductname());
//        } else if (newQuantity <= 0) {
//            throw new InvalidQuantityException("Quantity must be greater than zero for product: " + product.getProductname());
//        } else {
//            // Update the quantity
//            cartProduct.setCpquantity(newQuantity);
//        }
//
//        // Save the cart, which will cascade the update to CartProduct
//        Cart updatedCart = cartRepo.save(cart);
//
//        // Convert to DTO and return
//        return convertToCartDTO(updatedCart);
//    }
//
//
//
//}
package com.example.serviceImpl;

import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.dto.CartDTO;
import com.example.dto.CartProductDTO;
import com.example.exceptions.InvalidQuantityException;
import com.example.exceptions.OutOfStockException;
import com.example.exceptions.ProductNotFoundException;
import com.example.model.Cart;
import com.example.model.CartProduct;
import com.example.model.Product;
import com.example.model.ProductImage;
import com.example.model.User;
import com.example.repository.CartRepository;
import com.example.repository.UserRepository;
import com.example.repository.ProductRepository;
import com.example.serviceInterface.*;

@Service
public class CartServiceImpl implements CartService{
	private static final double SHIPPING_THRESHOLD = 100000.0;
	private static final double SHIPPING_COST = 50.0;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private ProductService productService;

	@Autowired
	private CartRepository cartRepo;

	public CartDTO addToCart(int productId, int quantity, Principal principal) {
		String username = principal.getName();
		User user = userRepo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException("Product not found"));

		Cart cart = cartRepo.findByUserId(user.getId()).orElse(new Cart());
		cart.setUser(user);

		Optional<CartProduct> existingCartProduct = cart.getCartProduct().stream()
				.filter(cp -> cp.getProduct().getProductId() == productId).findFirst();

		if (existingCartProduct.isPresent()) {
//			CartProduct cartProduct = CartProduct.builder().cart().build();
			
			CartProduct cartProduct = existingCartProduct.get();
			int updatedQuantity = cartProduct.getCpquantity() + quantity;
			if (updatedQuantity > product.getQuantity()) {
				throw new OutOfStockException(
						"Requested quantity exceeds available stock for product: " + product.getProductname());
			}
			cartProduct.setCpquantity(updatedQuantity);
			cartProduct.setTotal_pprice(cartProduct.getPrice() * updatedQuantity);
		} else {
			if (quantity > product.getQuantity()) {
				throw new OutOfStockException(
						"Requested quantity exceeds available stock for product: " + product.getProductname());
			}
			CartProduct newCartProduct = new CartProduct();
			newCartProduct.setProduct(product);
			newCartProduct.setCpquantity(quantity);
			newCartProduct.setPrice(product.getPrice());
			newCartProduct.setTotal_pprice(quantity * product.getPrice());
			cart.addCartProduct(newCartProduct);
		}

		updateCartTotals(cart);
		cartRepo.save(cart);

		return convertToCartDTO(cart);
	}

	public CartDTO updateCartProduct(int productId, int newQuantity, Principal principal) {
		String username = principal.getName();
		User user = userRepo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

		Cart cart = cartRepo.findByUserId(user.getId())
				.orElseThrow(() -> new IllegalArgumentException("Cart not found"));

		CartProduct cartProduct = cart.getCartProduct().stream()
				.filter(cp -> cp.getProduct().getProductId() == productId).findFirst()
				.orElseThrow(() -> new ProductNotFoundException("Product not found in cart"));

		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException("Product not found"));

		if (newQuantity > product.getQuantity()) {
			throw new OutOfStockException(
					"Requested quantity exceeds available stock for product: " + product.getProductname());
		} else if (newQuantity <= 0) {
			throw new InvalidQuantityException(
					"Quantity must be greater than zero for product: " + product.getProductname());
		} else {
			cartProduct.setCpquantity(newQuantity);
			cartProduct.setTotal_pprice(cartProduct.getPrice() * newQuantity);
		}

		updateCartTotals(cart);
		cartRepo.save(cart);

		return convertToCartDTO(cart);
	}

	public CartDTO removeFromCart(int productId, Principal principal) {
		String username = principal.getName();
		User user = userRepo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

		Cart cart = cartRepo.findByUserId(user.getId())
				.orElseThrow(() -> new IllegalArgumentException("Cart not found"));

		CartProduct cartProduct = cart.getCartProduct().stream()
				.filter(cp -> cp.getProduct().getProductId() == productId).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Product not found in cart"));

		cart.getCartProduct().remove(cartProduct);

		updateCartTotals(cart);
		cartRepo.save(cart);

		return convertToCartDTO(cart);
	}

	private void updateCartTotals(Cart cart) {
		double subtotal = cart.getCartProduct().stream().mapToDouble(CartProduct::getTotal_pprice).sum();

		double discount = calculateDiscount(subtotal);
		double total = subtotal - discount;
		double shippingCost = calculateShippingCost(total);

		cart.setSubtotal(subtotal);
		cart.setDiscount(discount);
		cart.setTotal(total + shippingCost);
		cart.setShippingcost(shippingCost);
	}

	private double calculateDiscount(double subtotal) {
		// Example: 10% discount for orders over 500
		return subtotal > 500 ? subtotal * 0.1 : 0;
	}

	private double calculateShippingCost(double total) {
		return total > SHIPPING_THRESHOLD ? 0 : SHIPPING_COST;
	}

	private CartDTO convertToCartDTO(Cart cart) {
		List<CartProductDTO> cartProductDTOs = cart.getCartProduct().stream()
				.map(cp -> new CartProductDTO(cp.getProduct().getProductId(), 
	                    cp.getProduct().getProductname(),
	                    cp.getCpquantity(), 
	                    cp.getPrice(), // Assuming you have a getter for the product price
	                    cp.getTotal_pprice(),// Total price for the product
	                    fetchProductImage(cp.getProduct().getProductId())))
				.collect(Collectors.toList());

		return new CartDTO(cart.getCartID(), cart.getUser().getId(), cartProductDTOs, cart.getSubtotal(),
				cart.getDiscount(), cart.getTotal(), cart.getShippingcost());
	}
	public List<String>  fetchProductImage(int productID) {
        List<byte[]> imageDataList = productService.getProductImagesById(productID);

        List<String> base64Images = imageDataList.stream()
            .map(imageData -> Base64.getEncoder().encodeToString(imageData))
            .collect(Collectors.toList());
        
        return base64Images;
        
        

	}

	public CartDTO getCartForUser(String username) {

	    User user = userRepo.findByEmail(username)
	                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
		Cart cart = cartRepo.findByUserId(user.getId())
				.orElseThrow(() -> new IllegalArgumentException("Cart not found"));

		return convertToCartDTO(cart);
	}

	public void clearCart(String username) {
        User user = userRepo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Cart cart = cartRepo.findByUserId(user.getId())
				.orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        // Remove all cart products and reset cart total
//        cartRepo.deleteByCart(cart);
        cart.getCartProduct().clear();;
        
        cart.setTotal(0);
        cart.setShippingcost(0);
        cart.setSubtotal(0);
        cart.setDiscount(0);
        cartRepo.save(cart);
    }
}
