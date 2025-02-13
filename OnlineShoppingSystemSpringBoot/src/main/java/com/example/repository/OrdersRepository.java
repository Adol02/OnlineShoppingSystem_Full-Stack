package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.OrderStatus;
import com.example.model.Orders;
@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer>{
	
//	List<Orders> findByUsername(String username);
//	public List<Orders> findByusername(String username);
	Optional<Orders> findByUserId(Integer id);
	List<Orders> findByUser_Id(Integer id);
	
	@Transactional
    @Modifying
    @Query("UPDATE Orders o SET o.status = :status WHERE o.orderID = :orderid")
    void updateOrderStatus(int orderid, OrderStatus status);


}
////	 // Update order status by order ID
////    @Transactional
////    @Modifying
////    @Query("UPDATE Orders o SET o.status = :status WHERE o.id = :orderId")
////    void updateOrderStatus(int orderId, String status);
////
////    // Get orders by username
////    @Query("SELECT o FROM Orders o WHERE o.username = :username")
//    
////
////    // Generate total sales report
////    @Query("SELECT o.productName AS productName, SUM(o.amount) AS totalSales FROM Orders o GROUP BY o.productName")
////    Map<String, Double> generateTotalSalesReport();
////    
////    // Optionally, add a method to find an order by its ID
////    Orders findById(int orderId);
//    
//    @Query("SELECT o FROM Orders o WHERE o.username = :username")
//    List<Orders> getOrdersByUsername(@Param("username") String userName);
//
//}
