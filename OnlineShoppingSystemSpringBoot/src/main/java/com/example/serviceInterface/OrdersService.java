package com.example.serviceInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.dto.OrderDTO;
import com.example.model.OrderStatus;
import com.example.model.Orders;

@Service
public interface OrdersService {
//	public void checkout(String username);
	public void checkout(String username, String paymentMethod, String shippingAddress);
	public List<Orders> getOrdersbyUserId(int id);
//	public ArrayList<Orders> getOrdersByUserName(String username) ;
//	public List<Orders> findByUsername(String username);
	public List<OrderDTO> getOrderDTOsByUserId(Integer id);
	public List<OrderDTO> getAllOrders() ;
	public Optional<Orders> getOrderById(int orderId);
	public void updateOrderStatus(Integer orderId, OrderStatus status);
}
