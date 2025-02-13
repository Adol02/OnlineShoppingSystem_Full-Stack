package com.example.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.authservice.UserService;
import com.example.dto.AddToCart;
import com.example.dto.AddToWishlist;
import com.example.dto.CartDTO;
import com.example.dto.CheckoutRequest;
import com.example.dto.OrderDTO;
import com.example.dto.ProductDTO;
import com.example.dto.WishListDTO;
import com.example.exceptions.ProductNotFoundException;
import com.example.model.Address;
import com.example.model.Orders;
import com.example.model.Product;
import com.example.model.User;
import com.example.model.UserInfo;
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;
import com.example.serviceInterface.CartService;
import com.example.serviceInterface.OrdersService;
import com.example.serviceInterface.ProductService;
import com.example.serviceInterface.WishListService;

@RequestMapping("/users")
@PreAuthorize("hasAuthority('ROLE_USER')")
@RestController
public class UserController {
	private final UserService userService;

	@Autowired
	private CartService cartService;
	@Autowired
	private OrdersService ordersService;
	@Autowired
	private WishListService wishListService;
	@Autowired
	private UserRepository userRepo;
	@Autowired
    private ProductService productService;
	@Autowired
	private ProductRepository productRepo;


	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/me")
	public ResponseEntity<UserInfo> authenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("hello");

		// Check the class of the principal
		Object principal = authentication.getPrincipal();
		System.out.println("Principal Class: " + principal.getClass());

		if (principal instanceof UserInfo) {
			UserInfo currentUser = (UserInfo) principal;
			System.out.println(currentUser);
			System.out.println("me");
			return ResponseEntity.ok(currentUser);
		} else {
			// Handle the case where the principal is not of type UserInfo
			System.out.println("Principal is not of type UserInfo");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@GetMapping("/")
	public ResponseEntity<List<User>> allUsers() {
		List<User> users = userService.allUsers();

		return ResponseEntity.ok(users);
	}

	 @PostMapping("/addToCart")
	 public ResponseEntity<CartDTO> addToCart(@RequestBody AddToCart addToCartRequest, Principal principal) {
		 System.out.println("11");
	     String username = principal.getName();
	     System.out.println("11");
	     User user = userRepo.findByEmail(username)
	                         .orElseThrow(() -> new UsernameNotFoundException("User not found"));
         System.out.println("12");
	     Product product = productRepo.findById(addToCartRequest.getProductId())
	                                  .orElseThrow(() -> new ProductNotFoundException("Product not found"));
	     System.out.println("13");
	     CartDTO cart = cartService.addToCart(product.getProductId(), addToCartRequest.getQuantity(), principal);
	     System.out.println("14");
	     return new ResponseEntity<>(cart, HttpStatus.OK);
	 }

	 @DeleteMapping("/removeFromCart/{productId}")
	 public ResponseEntity<CartDTO> removeFromCart(@PathVariable int productId, Principal principal) {
	     CartDTO updatedCart = cartService.removeFromCart(productId, principal);
	     return new ResponseEntity<>(updatedCart, HttpStatus.OK);
	 }

	 @GetMapping("/products")
	    public ResponseEntity<List<Product>> getAllProducts() {
	        List<Product> products = productService.getAllProducts();
	        return new ResponseEntity<>(products, HttpStatus.OK);
	    }
	 @GetMapping("/products/{id}")
	 public ResponseEntity<ProductDTO> getProductsById(@PathVariable int id) {
	     Optional<Product> optionalProduct = productService.getProductById(id);

	     // Check if the product exists
	     if (optionalProduct.isEmpty()) {
	         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	     }

	     // Get the product from Optional
	     Product product = optionalProduct.get();

	     // Map Product to ProductDTO
	     ProductDTO productDTO = new ProductDTO();
	     productDTO.setProductId(product.getProductId());
	     productDTO.setProductName(product.getProductname()); // Ensure this method exists in your Product class
	     productDTO.setDescription(product.getDescription());
	     productDTO.setPrice(product.getPrice());

	     // Process multiple images for each product
	     List<String> imageBase64List = product.getImages().stream()
	             .map(productImage -> Base64.getEncoder().encodeToString(productImage.getImageData()))
	             .collect(Collectors.toList());

	     // Set the Base64 encoded image data in the DTO
	     productDTO.setImageData(imageBase64List);

	     return new ResponseEntity<>(productDTO, HttpStatus.OK);
	 }

	 
	@PutMapping("/updateCart")
	public ResponseEntity<CartDTO> updateCart(@RequestBody AddToCart addToCartRequest, Principal principal) {
		int productId = addToCartRequest.getProductId();
		int newQuantity = addToCartRequest.getQuantity();

		CartDTO updatedCart = cartService.updateCartProduct(productId, newQuantity,principal);
		return new ResponseEntity<>(updatedCart, HttpStatus.OK);
	}
	@GetMapping("/products/category/{category}")
	public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable String category) {
	    List<Product> products = productService.getByCategory(category);
	    List<ProductDTO> productDTOs = products.stream().map(product -> {
            ProductDTO dto = new ProductDTO();
            dto.setProductId(product.getProductId());
            dto.setProductName(product.getProductname()); // Fixed typo (getProductname -> getProductName)
            dto.setDescription(product.getDescription());
            dto.setPrice(product.getPrice());

            // Process multiple images for each product
            List<String> imageBase64List = product.getImages().stream()
                    .map(productImage -> Base64.getEncoder().encodeToString(productImage.getImageData()))
                    .collect(Collectors.toList());

            // Set the Base64 encoded image data in the DTO
            dto.setImageData(imageBase64List);

            return dto;
        }).collect(Collectors.toList());
	    if (products.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    }
	    return new ResponseEntity<>(productDTOs, HttpStatus.OK);
	}
	@PostMapping("/addToWishList")
	public ResponseEntity<WishListDTO> addToWishlist(@RequestBody AddToWishlist addToWishlistRequest, Principal principal) {
	    String username = principal.getName();
	    
	    // Find the user by email
	    User user = userRepo.findByEmail(username)
	                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

	    // Find the product by its ID from the request body
	    Product product = productRepo.findById(addToWishlistRequest.getProductId())
	                                 .orElseThrow(() -> new ProductNotFoundException("Product not found"));

	    // Add the product to the wishlist for the user
	    WishListDTO updatedWishlist = wishListService.addToWishlist(user, product);

	    return new ResponseEntity<>(updatedWishlist, HttpStatus.OK);
	}

	    
	    // Remove product from wishlist
	    @DeleteMapping("/removeFromWishList/{productId}")
	    public ResponseEntity<WishListDTO> removeFromWishlist(@PathVariable int productId, Principal principal) {
	        String username = principal.getName();

	        // Find the user by email
	        User user = userRepo.findByEmail(username)
	                            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

	        // Remove the product from the wishlist
	        WishListDTO updatedWishlist = wishListService.removeFromWishlist(user, productId);

	        return new ResponseEntity<>(updatedWishlist, HttpStatus.OK);
	    }

	    @GetMapping("/cart")
	    public ResponseEntity<CartDTO> getCart(Principal principal) {
	        // Get the username of the logged-in user
	        String username = principal.getName();
	            // Fetch the cart for the user using the CartService
	            CartDTO cart = cartService.getCartForUser(username);

	            // Return the cart details if found
	            return new ResponseEntity<>(cart, HttpStatus.OK);
	    }
	    // Get the user's wishlist
	    @GetMapping("/wishlist")
	    public ResponseEntity<WishListDTO> getWishlist(Principal principal) {
	        String username = principal.getName();

	        // Find the user by email
	        User user = userRepo.findByEmail(username)
	                            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

	        // Get the user's wishlist as DTO
	        WishListDTO wishlistDTO = wishListService.getWishlist(user);

	        return new ResponseEntity<>(wishlistDTO, HttpStatus.OK);
	    }
	    
	    @PostMapping("/addProductToCartFromWishList")
	    public ResponseEntity<String> addProductToCartFromWishList(@RequestBody AddToCart addToCartRequest, Principal principal) {
	        // Get the user's email (username)
	        String username = principal.getName();

	        // Find the user
	        User user = userRepo.findByEmail(username)
	                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

	        // Find the product by ID
	        Product product = productRepo.findById(addToCartRequest.getProductId())
	                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

	        // Check if the product is in the user's wishlist
	        if (!wishListService.isProductInWishlist(user, product)) {
	            return new ResponseEntity<>("Product is not in the wishlist", HttpStatus.BAD_REQUEST);
	        }

	        // Remove the product from the wishlist
	        wishListService.removeFromWishlist(user, product.getProductId());

	        // Add the product to the cart with the specified quantity
	        cartService.addToCart(product.getProductId(), addToCartRequest.getQuantity(), principal);

	        // Return a success response
	        return new ResponseEntity<>("Product successfully added to cart from wishlist", HttpStatus.OK);
	    }
	    @GetMapping("/products/{id}/images")
	    public ResponseEntity<List<String>> getProductImagesAsBase64(@PathVariable("id") int productId) {
	        List<byte[]> imageDataList = productService.getProductImagesById(productId);

	        if (imageDataList.isEmpty()) {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }

	        List<String> base64Images = imageDataList.stream()
	            .map(imageData -> Base64.getEncoder().encodeToString(imageData))
	            .collect(Collectors.toList());

	        return new ResponseEntity<>(base64Images, HttpStatus.OK);
	    }
	    @GetMapping("/products/{id}/imagescheck")
	    public ResponseEntity<List<String>> getProductImage(@PathVariable("id") int productId) {
	    	List<String> imageData = cartService.fetchProductImage(productId);

	        if (imageData.isEmpty()) {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	        return new ResponseEntity<>(imageData , HttpStatus.OK);
	    }
	    @GetMapping("/userdetails/{userId}")
	    public ResponseEntity<User> getUserDetails(@PathVariable Integer userId) {
	        User user = userService.getUserById(userId);
	        return ResponseEntity.ok(user);
	    }

	    // Endpoint to add a new address for a user
	    @PostMapping("/{userId}/addaddress")
	    public ResponseEntity<String> addNewAddress(@PathVariable Integer userId, @RequestBody Address newAddress) {
	        userService.addAddressToUser(userId, newAddress);
	        return ResponseEntity.ok("Address added successfully");
	    }
	    @PostMapping("/checkout")
	    public ResponseEntity<Map<String, String>> checkout(Principal principal, @RequestBody CheckoutRequest checkout) {
	        System.out.println("Payment Method: " + checkout.getPayment());
	        System.out.println("Delivery Address: " + checkout.getAddress());

	        // Get the username of the logged-in user
	        String username = principal.getName();
	        
	        try {
	            // Call the checkout method from the OrdersService
	            ordersService.checkout(username, checkout.getPayment(), checkout.getAddress());

	            // Return a JSON response with a success message
	            Map<String, String> response = new HashMap<>();
	            response.put("message", "Checkout successful. Your order has been placed.");
	            return ResponseEntity.ok(response);

	        } catch (UsernameNotFoundException | ProductNotFoundException e) {
	            // Handle case when user or product is not found
	            Map<String, String> errorResponse = new HashMap<>();
	            errorResponse.put("error", e.getMessage());
	            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

	        } catch (IllegalStateException e) {
	            // Handle case when there is an issue with the cart
	            Map<String, String> errorResponse = new HashMap<>();
	            errorResponse.put("error", e.getMessage());
	            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

	        } catch (Exception e) {
	            // Handle any other exceptions
	            Map<String, String> errorResponse = new HashMap<>();
	            errorResponse.put("error", "An error occurred during checkout: " + e.getMessage());
	            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	    @GetMapping("/viewOrders")
	    public ResponseEntity<List<OrderDTO>> getOrdersbyId(Principal principal) {
	    	 String username = principal.getName();
	    	 User user = userRepo.findByEmail(username)
                     .orElseThrow(() -> new UsernameNotFoundException("User not found"));
	        List<OrderDTO> order = ordersService.getOrderDTOsByUserId(user.getId());
//	        List<Orders> order2 = ordersService.findByUsername(username);
	        System.out.println(order);
	        
	        return ResponseEntity.ok(order);
	    }
	    
	}
	

