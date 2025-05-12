package com.ftohbackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ftohbackend.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

	List<Order> findByCustomerCustomerId(Integer customerId);
    
	@Query("SELECT o FROM Order o WHERE o.product.seller.sellerId = :sellerId")
    List<Order> findByProductSellerSellerId(@Param("sellerId") Integer sellerId);
	
	
	Optional<Order> findByRazorpayOrderId(String razorpayOrderId);
	
	List<Order> findByProductProductIdAndCustomerCustomerId(Integer productId, Integer customerId);
}
