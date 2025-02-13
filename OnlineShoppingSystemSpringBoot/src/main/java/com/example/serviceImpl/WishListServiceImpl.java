package com.example.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.ProductDTO;
import com.example.dto.WishListDTO;
import com.example.model.Product;
import com.example.model.User;
import com.example.model.WishList;
import com.example.serviceInterface.WishListService;
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;
import com.example.repository.WishListRepository;

@Service
public class WishListServiceImpl implements WishListService{

    @Autowired
    private WishListRepository wishlistRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ProductRepository productRepo;

    public WishListDTO addToWishlist(User user, Product product) {
        WishList wishlist = wishlistRepo.findByUserId(user.getId()).orElse(new WishList());
        wishlist.setUser(user);

        if (wishlist.getProduct() == null) {
        	wishlist.setProduct(new ArrayList<>());
        }
        boolean productExists = wishlist.getProduct().stream()
                .anyMatch(existingProduct -> existingProduct.getProductId() == product.getProductId());
        // If the product doesn't exist, add it to the wishlist
        if (!productExists) {
            wishlist.getProduct().add(product);
        }else {
        	removeFromWishlist(user, product.getProductId());
        }
        WishList savedWishlist = wishlistRepo.save(wishlist);
        
        return convertToDTO(savedWishlist);
    }   
    private WishListDTO convertToDTO(WishList wishlist) {
        WishListDTO dto = new WishListDTO();
        dto.setWishListID(wishlist.getWishListID());
        dto.setUserEmail(wishlist.getUser().getEmail());

        // Convert each product to ProductDTO
        List<ProductDTO> productDTOs = wishlist.getProduct()
        		                       .stream().map(this::convertProductToDTO).collect(Collectors.toList());
        dto.setProducts(productDTOs);

        return dto;
    }

    private ProductDTO convertProductToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(product.getProductId());
        productDTO.setProductName(product.getProductname());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setCategory(product.getCategory());
        return productDTO;
    }
    
  
    

    public WishListDTO removeFromWishlist(User user, int productId) {
        WishList wishlist = wishlistRepo.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("WishList not found"));

        // Remove the product and save only if found
        boolean removed = wishlist.getProduct().removeIf(product -> product.getProductId() == productId);
        if (removed) {
            WishList updatedWishlist = wishlistRepo.save(wishlist);
            return convertToDTO(updatedWishlist);
        } else {
            throw new IllegalArgumentException("Product not found in wishlist");
        }
    }

    public WishListDTO getWishlist(User user) {
        WishList wishlist = wishlistRepo.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Wishlist not found"));

        // Convert and return the wishlist as a DTO
        return convertToDTO(wishlist);
    }
	public boolean isProductInWishlist(User user, Product product) {
		  WishList wishlist = wishlistRepo.findByUserId(user.getId())
		            .orElseThrow(() -> new IllegalArgumentException("Wishlist not found"));

		    return wishlist.getProduct().stream()
		            .anyMatch(existingProduct -> existingProduct.getProductId() == product.getProductId());

	}
}
