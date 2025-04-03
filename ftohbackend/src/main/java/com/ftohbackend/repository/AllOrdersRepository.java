package com.ftohbackend.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ftohbackend.model.AllOrders;
import com.ftohbackend.model.Customer;
import com.ftohbackend.model.Seller;

public interface AllOrdersRepository extends JpaRepository<AllOrders, Integer> {
    
    List<AllOrders> findByCustomer(Customer customer_id);
    
    List<AllOrders> findByProduct_Seller(Seller sellerId);

}


