package com.example.serviceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dto.CartDTO;
import com.example.dto.CartProductDTO;
import com.example.dto.OrderDTO;
import com.example.dto.OrderProductDTO;
import com.example.exceptions.ProductNotFoundException;
import com.example.model.OrderProduct;
import com.example.model.OrderProductKey;
import com.example.model.OrderStatus;
import com.example.model.Orders;
import com.example.model.Product;
import com.example.model.User;
import com.example.repository.OrdersRepository;
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;
import com.example.serviceInterface.OrdersService;

import jakarta.persistence.EntityNotFoundException;


@Service
@Transactional
public class OrdersServiceImpl implements OrdersService {

	@Autowired
	private CartServiceImpl cartService;

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private OrdersRepository orderRepo;

	@Autowired
	private UserRepository userRepo;

	public void checkout(String username, String paymentMethod, String shippingAddress) {
		// Get the user
		System.out.println("killlllsdfg");
		System.out.println("hi" + paymentMethod);
		System.out.println("de" + shippingAddress);

		User user = userRepo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

		// Fetch the user's cart
		CartDTO cart = cartService.getCartForUser(username);
		if (cart == null || cart.getCartProducts().isEmpty()) {
			throw new IllegalStateException("Cart is empty");
		}

		// Calculate total amount, discount, and shipping cost (assuming these values
		// are in the CartDTO)
		double totalAmount = cart.getTotal();
		double discount = cart.getDiscount(); // Apply any discount from the cart
		double shippingCost = cart.getShippingcost(); // Calculate or get shipping cost from cart

		// Create a new order
		Orders newOrder = new Orders();
		newOrder.setUser(user);
		newOrder.setStatus(OrderStatus.PENDING);
		newOrder.setPaymentMethod(paymentMethod); // Set payment method provided by frontend
		newOrder.setShippingAddress(shippingAddress); // Set shipping address provided by frontend
		newOrder.setDiscount(discount); // Apply discount from cart
		newOrder.setTotalAmount(totalAmount); // Set total amount from cart
		newOrder.setShippingCost(shippingCost); // Set shipping cost from cart
		newOrder.setPaymentStatus("Paid"); // Set default payment status as Pending for now
		newOrder.setTrackingNumber(generateTrackingNumber()); // Generate a random tracking number

		// Calculate delivery date (4 days from the order date)
		Date orderDate = new Date(); // Current date as order date
		newOrder.setOrderDate(orderDate);

		// Add 4 days to the order date for delivery date
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(orderDate);
		calendar.add(Calendar.DATE, 4); // Add 4 days to current date
		Date deliveryDate = calendar.getTime();
		newOrder.setDeliveryDate(deliveryDate); // Set calculated delivery date

		// Save the order to generate the orderID
		newOrder = orderRepo.save(newOrder);
		System.out.println("Order saved with ID: " + newOrder.getOrderID());

		// Prepare the order products
		List<OrderProduct> orderProducts = new ArrayList<>();
		for (CartProductDTO cartProduct : cart.getCartProducts()) {
			Product product = productRepo.findById(cartProduct.getProductId())
					.orElseThrow(() -> new ProductNotFoundException("Product not found"));

			if (product.getQuantity() < cartProduct.getQuantity()) {
				throw new IllegalStateException("Not enough stock for product: " + product.getProductname());
			}

			// Update product stock
			product.setQuantity(product.getQuantity() - cartProduct.getQuantity());
			productRepo.save(product);

			// Create an OrderProduct
			OrderProduct orderProduct = new OrderProduct();
			orderProduct.setProduct(product);
			orderProduct.setQuantity(cartProduct.getQuantity());
			orderProduct.setProductPrice(product.getPrice());
			orderProduct.setTotalPrice(product.getPrice() * cartProduct.getQuantity());
			orderProduct.setCategory(product.getCategory());

			// Set the composite key
			OrderProductKey key = new OrderProductKey();
			key.setOrderId(newOrder.getOrderID());
			key.setProductId(product.getProductId());
			orderProduct.setId(key);

			// Link the order
			orderProduct.setOrder(newOrder);
			orderProducts.add(orderProduct);
		}

		// Link the products to the order and save the order again
		newOrder.setOrderProducts(orderProducts);
		orderRepo.save(newOrder);

		// Clear the user's cart
		cartService.clearCart(username);
		System.out.println("Checkout complete for user: " + username);
	}

	// Helper method to generate a random tracking number
	private String generateTrackingNumber() {
		// You can use UUID or any other method to generate a unique tracking number
		return "TN" + System.currentTimeMillis(); // Simple example using current time in milliseconds
	}
	public List<Orders> getOrdersbyUserId(int id) {
		return  orderRepo.findByUser_Id(id);
	}
	public List<OrderDTO> getAllOrders() {
	    return orderRepo.findAll().stream().map(order -> {
	        List<OrderProductDTO> products = order.getOrderProducts().stream()
	            .map(op -> new OrderProductDTO(
	                    op.getProduct().getProductId(),
	                    op.getProduct().getProductname(),
	                    op.getQuantity(),
	                    op.getProductPrice()
	            ))
	            .collect(Collectors.toList());

	        return new OrderDTO(
	            order.getOrderID(),
	            order.getStatus(),
	            order.getOrderDate(),
	            order.getTotalAmount(),
	            products
	        );
	    }).collect(Collectors.toList());
	}

	public List<OrderDTO> getOrderDTOsByUserId(Integer userId) {
	    // Fetch orders
	    List<Orders> orders = orderRepo.findByUser_Id(userId);
	    System.out.println("Orders fetched: " + orders); // Debugging

	    return orders.stream().map(order -> {
	        // Map each OrderProduct to ProductDTO
	        List<OrderProductDTO> products = order.getOrderProducts().stream()
	                .map(op -> {
	                    // Check for null product to avoid NullPointerException
	                    if (op.getProduct() != null) {
	                        return new OrderProductDTO(
	                                op.getProduct().getProductId(),
	                                op.getProduct().getProductname(),
	                                op.getQuantity(),
	                                op.getProductPrice());
	                    }
	                    return null; // Handle or log appropriately if product is null
	                })
	                .filter(Objects::nonNull) // Remove null entries
	                .collect(Collectors.toList());

	        // Map Orders to OrderDTO
	        return new OrderDTO(
	                order.getOrderID(),
	                order.getStatus(),
	                order.getOrderDate(),
	                order.getTotalAmount(),
	                products);
	    }).collect(Collectors.toList());
	}

	@Override
	public Optional<Orders> getOrderById(int orderId) {
		// TODO Auto-generated method stub
		return  orderRepo.findById(orderId);
	}

	

	public void updateOrderStatus(Integer orderId, OrderStatus status) {
		Orders order = orderRepo.findById(orderId)
	            .orElseThrow(() -> new EntityNotFoundException("Order with ID " + orderId + " not found"));
        
	    order.setStatus(status); // Update the status
	    orderRepo.save(order); // Save the updated orde
    }
//
//	public ArrayList<Orders> getOrdersByUserName(String username) {
//		return (ArrayList<Orders>) orderRepo.findByUsername(username);
//	}
//
//	public Map<String, Double> generateTotalSalesReport() {
//		return orderRepo.generateTotalSalesReport();
//	}


}
