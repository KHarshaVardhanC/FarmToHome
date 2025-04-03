package com.ftohbackend.repository;
import com.ftohbackend.model.AllOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllOrdersRepository extends JpaRepository<AllOrders, Integer> {
    
    // Custom query method to find orders by customer ID
    List<AllOrders> findByCustomerCustomerId(Integer customerId);
    
    // Custom query method to find orders by seller ID
    List<AllOrders> findByProductSellerSellerId(Integer sellerId);
}
