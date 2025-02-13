package com.example.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.dto.OrderDTO;
import com.example.dto.ProductDTO;
import com.example.model.OrderStatus;
import com.example.model.Orders;
import com.example.model.Product;
import com.example.model.ProductImage;
import com.example.model.UserInfo;
import com.example.serviceInterface.OrdersService;
import com.example.serviceInterface.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    private ProductService productService;
    @Autowired
    private OrdersService orderService;

//    @Autowired
//    private OrdersService orderService;
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

  
//    @PostMapping("/addProduct")
//    public ResponseEntity<String> addProduct(@RequestParam("productname") String productname,
//                                             @RequestParam("category") String category,
//                                             @RequestParam("description") String description,
//                                             @RequestParam("price") double price,
//                                             @RequestParam("quantity") int quantity,
//                                             @RequestParam("imageFile") MultipartFile imageFile) {
//        try {
//            Product product = new Product();
//            product.setProductname(productname);
//            product.setCategory(category);
//            product.setDescription(description);
//            product.setPrice(price);
//            product.setQuantity(quantity);
//            product.setImageData(imageFile.getBytes()); // Convert image file to byte array
//            
//            productService.addProduct(product);
//            return ResponseEntity.ok("Product added successfully!");
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                                 .body("Failed to add product: " + e.getMessage());
//        }
//    }
//    @PostMapping("/addProductsWithImages")
//    public ResponseEntity<String> addProductsWithImages(
//            @RequestParam("products") String productsJson,  // JSON array of products
//            @RequestParam("images") List<MultipartFile> imageFiles) {  // Multiple image files
//        try {
//            // Parse JSON to a list of Product objects
//            ObjectMapper objectMapper = new ObjectMapper();
//            List<Product> products = Arrays.asList(objectMapper.readValue(productsJson, Product[].class));
//
//            // Ensure the number of products and images match
//            if (products.size() != imageFiles.size()) {
//                return ResponseEntity.badRequest().body("The number of products and images must be equal");
//            }
//
//            // Process each product and its corresponding image
//            for (int i = 0; i < products.size(); i++) {
//                Product product = products.get(i);
//                MultipartFile imageFile = imageFiles.get(i);
//
//                // Set the image data for the product
//                product.setImageData(imageFile.getBytes());  // Convert image file to byte array
//
//                // Save the product (with image) to the database
//                productService.addProduct(product);
//            }
//
//            return ResponseEntity.ok("Products added successfully!");
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                                 .body("Failed to add products: " + e.getMessage());
//        }
//    }
    @PostMapping("/addSingleProductWithMultipleImages")
    public ResponseEntity<String> addSingleProductWithMultipleImages(
            @RequestParam("product") String productJson,  // JSON object for product
            @RequestParam("images") List<MultipartFile> imageFiles) {
        try {
            // Parse JSON to a Product object
            ObjectMapper objectMapper = new ObjectMapper();
            Product product = objectMapper.readValue(productJson, Product.class);

            // Loop through images and add them to the product
            for (MultipartFile imageFile : imageFiles) {
                if (!isImage(imageFile.getContentType())) {
                    return ResponseEntity.badRequest().body("Invalid file type: " + imageFile.getContentType());
                }

                // Create a ProductImage entity
                ProductImage productImage = new ProductImage();
                productImage.setImageData(imageFile.getBytes());
                productImage.setImageName(imageFile.getOriginalFilename());
                productImage.setContentType(imageFile.getContentType());
                productImage.setSize(imageFile.getSize());

                // Add the image to the product
                product.addImage(productImage);
            }

            // Save the product (with images) to the database
            productService.addProduct(product);

            return ResponseEntity.ok("Product with multiple images added successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to add product: " + e.getMessage());
        }
    }

    @PostMapping("/addMultipleProductsWithMultipleImages")
    public ResponseEntity<String> addMultipleProductsWithMultipleImages(
            @RequestParam("products") String productsJson,  // JSON array of products
            @RequestParam("images") List<MultipartFile> imageFiles) {  // List of all image files
        try {
            // Parse JSON array to a list of Product objects
            ObjectMapper objectMapper = new ObjectMapper();
            List<Product> products = Arrays.asList(objectMapper.readValue(productsJson, Product[].class));

            // You need to map the images to products
            // Assuming the productsJson includes an array and for each product,
            // the images are in the same order as they appear in the images list, and separated by unique product.
            
            // Create a List of Lists to store images for each product
            List<List<MultipartFile>> productImages = new ArrayList<>();
            int imageIndex = 0;

            // Iterate over the products and assign images to each product
            for (Product product : products) {
                List<MultipartFile> imagesForProduct = new ArrayList<>();

                // Example: assuming 2 images for each product, modify according to your logic
                for (int i = 0; i < 2; i++) {  // Adjust the number as needed
                    if (imageIndex < imageFiles.size()) {
                        imagesForProduct.add(imageFiles.get(imageIndex));
                        imageIndex++;
                    }
                }

                productImages.add(imagesForProduct);  // Add the images list for this product
            }

            // Ensure that each product has at least one image
            if (products.size() != productImages.size()) {
                return ResponseEntity.badRequest().body("The number of products and image sets must match");
            }

            // Process each product and its corresponding images
            for (int i = 0; i < products.size(); i++) {
                Product product = products.get(i);
                List<MultipartFile> imagesForProduct = productImages.get(i);

                // Add each image to the product
                for (MultipartFile imageFile : imagesForProduct) {
                    if (!isImage(imageFile.getContentType())) {
                        return ResponseEntity.badRequest().body("Invalid file type for product: " + product.getProductname());
                    }

                    // Create a new ProductImage entity for each image
                    ProductImage productImage = new ProductImage();
                    productImage.setImageData(imageFile.getBytes());
                    productImage.setImageName(imageFile.getOriginalFilename());
                    productImage.setContentType(imageFile.getContentType());
                    productImage.setSize(imageFile.getSize());

                    // Associate the image with the product
                    product.addImage(productImage);
                }

                // Save the product (with images) to the database
                productService.addProduct(product);
            }

            return ResponseEntity.ok("Multiple products with multiple images added successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to add products: " + e.getMessage());
        }
    }
    private boolean isImage(String contentType) {
        return contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/png"));
    }
//    @PostMapping("/addProducts")
//    public String addProducts(@RequestBody List<Product> products) {
//    	for(Product p : products) {
//    		productService.addProduct(p);
//    	}
//        return "Products added successfully!";
//    }

    // Get All Products with Image in Base64
//    @GetMapping("/products")
//    public ResponseEntity<List<ProductDTO>> getAllProducts() {
//        List<Product> products = productService.getAllProducts();
//        List<ProductDTO> productDTOs = products.stream().map(product -> {
//            ProductDTO dto = new ProductDTO();
//            dto.setProductId(product.getProductId());
//            dto.setProductName(product.getProductname());
//            dto.setDescription(product.getDescription());
//            dto.setPrice(product.getPrice());
////           System.out.println(Base64.getEncoder().encodeToString(product.getImageData()));
//            // Convert image byte array to Base64 string if not null
//            if (product.getImageData() != null) {
//                dto.setImageData(Base64.getEncoder().encodeToString(product.getImageData()));
//                System.out.println(Base64.getEncoder().encodeToString(product.getImageData()));
//            }
//
//            return dto;
//        }).collect(Collectors.toList());
//
//        return new ResponseEntity<>(productDTOs, HttpStatus.OK);
//    }
    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        
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

        return new ResponseEntity<>(productDTOs, HttpStatus.OK);
    }


//    @GetMapping("/updateStock")
//    public ResponseEntity<Product> updateStockForm(@RequestParam("productname") String productname) {
//        Product product = productService.getProductByName(productname);
//        if (product == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<>(product, HttpStatus.OK);
//    }

    @PutMapping("/updateStock/{productName}")
    public ResponseEntity<String> updateStockLevels(@PathVariable String productName, @RequestBody Product productDetails) {
        Product product = productService.getProductByName(productName);
        if (product == null) {
            return new ResponseEntity<>("Product not found.", HttpStatus.NOT_FOUND);
        }
        product.setPrice(productDetails.getPrice());
        product.setQuantity(productDetails.getQuantity());
        productService.updateProduct(product);
        return new ResponseEntity<>("Product updated successfully!", HttpStatus.OK);
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
    @GetMapping("/products/getbyName/{productname}")
	 public ResponseEntity<ProductDTO> getProductsByName(@PathVariable String productname) {
	    Product product = productService.getProductByName(productname);

	     // Map Product to ProductDTO
	     ProductDTO productDTO = new ProductDTO();
	     productDTO.setProductId(product.getProductId());
	     productDTO.setProductName(product.getProductname()); // Ensure this method exists in your Product class
	     productDTO.setDescription(product.getDescription());
	     productDTO.setPrice(product.getPrice());
	     productDTO.setCategory(product.getCategory());
	     productDTO.setQuantity(product.getQuantity());
	     

	     // Process multiple images for each product
	     List<String> imageBase64List = product.getImages().stream()
	             .map(productImage -> Base64.getEncoder().encodeToString(productImage.getImageData()))
	             .collect(Collectors.toList());

	     // Set the Base64 encoded image data in the DTO
	     productDTO.setImageData(imageBase64List);

	     return new ResponseEntity<>(productDTO, HttpStatus.OK);
	 }

    @DeleteMapping("/deleteProduct/{productname}")
    public ResponseEntity<String> deleteProduct(@PathVariable String productname) {
        productService.deleteProduct(productname);
        return new ResponseEntity<>("Product deleted successfully!", HttpStatus.OK);
    }
    
//    @GetMapping("/products/{id}")
//    public ResponseEntity<byte[]> getProductImage(@PathVariable("id") int productId) {
//        byte[] imageData = productService.getProductImageById(productId);
//
//        if (imageData == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "image/jpeg");  // or "image/png" depending on your image type
//
//        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
//    }
//    @GetMapping("/products/{id}")
//    public ResponseEntity<List<byte[]>> getProductImages(@PathVariable("id") int productId) {
//        List<byte[]> imageDataList = productService.getProductImagesById(productId);
//
//        if (imageDataList.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "image/jpeg");  // This assumes all images are JPEG; modify if needed.
//
//        // Return a list of byte arrays containing image data
//        return new ResponseEntity<>(imageDataList, headers, HttpStatus.OK);
//    } 
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
    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> ordersList = orderService.getAllOrders();
        return new ResponseEntity<>(ordersList, HttpStatus.OK);
    }

//    @PostMapping("/approveOrder")
//    public ResponseEntity<String> approveOrder(@RequestParam String action) {
//    	 List<OrderDTO> ordersList = orderService.getAllOrders();
////        Optional<Orders> order = orderService.getOrderById(orderId);
//        if (ordersList == null) {
//            return new ResponseEntity<>("Order not found.", HttpStatus.NOT_FOUND);
//        }
//        if ("approve".equalsIgnoreCase(action)) {
//            orderService.updateOrderStatus(orderId, "Order Delivered");
//            return new ResponseEntity<>("Order approved.", HttpStatus.OK);
//        } else {
//            orderService.updateOrderStatus(orderId, "Rejected");
//            return new ResponseEntity<>("Order rejected.", HttpStatus.OK);
//        }
//    }
    @PostMapping("/approveOrder")
    public ResponseEntity<String> approveOrder(@RequestParam Integer orderId, @RequestParam String action) {
        try {
            if ("approve".equalsIgnoreCase(action)) {
                orderService.updateOrderStatus(orderId, OrderStatus.DELIVERED);
                return new ResponseEntity<>("Order approved.", HttpStatus.OK);
            } else {
                orderService.updateOrderStatus(orderId, OrderStatus.REJECTED);
                return new ResponseEntity<>("Order rejected.", HttpStatus.OK);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateStatus")
    public ResponseEntity<String> updateOrderStatus(@RequestBody Map<String, Object> request) {
        Integer orderId = (Integer) request.get("orderId");
        String statusStr = (String) request.get("status");
        System.out.println(orderId);
        System.out.println(statusStr);
        try {
            // Parse the status
            OrderStatus status = OrderStatus.valueOf(statusStr.toUpperCase());

            // Call the service to update the status
            orderService.updateOrderStatus(orderId, status);

            return new ResponseEntity<>("Order status updated to " + status.name(), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid status value: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NullPointerException e) {
            return new ResponseEntity<>("Missing required fields: orderId or status", HttpStatus.BAD_REQUEST);
        }
    }

//
//    @GetMapping("/salesReport")
//    public ResponseEntity<Map<String, Double>> generateTotalSalesReport() {
//        Map<String, Double> salesReport = orderService.generateTotalSalesReport();
//        return new ResponseEntity<>(salesReport, HttpStatus.OK);
//    }
}
