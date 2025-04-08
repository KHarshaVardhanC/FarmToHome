package com.ftohbackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ftohbackend.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

	List<Order> findByCustomerCustomerId(Integer customerId);
    
//    List<Order> findByCustomer(Customer customer_id);
    
//    List<Order> findByProduct_Seller(Seller sellerId);

}


