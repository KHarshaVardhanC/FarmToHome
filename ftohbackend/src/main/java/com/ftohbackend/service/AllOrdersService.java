package com.ftohbackend.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.ftohbackend.model.AllOrders;

import jakarta.transaction.Transactional;
@Service
public interface AllOrdersService {
    
    // Save a new order
    AllOrders saveOrder(AllOrders order);

    // Retrieve all orders
    List<AllOrders> getAllOrders();

    // Get orders by customer ID
    List<AllOrders> getOrdersByCustomerId(Integer customer_Id);

    // Get orders by seller ID
    List<AllOrders> getOrdersBySellerId(Integer seller_Id);

    // Get an order by order ID
    AllOrders getOrderById(Integer orderId);

    // Delete an order by order ID
    void deleteOrder(Integer orderId);
    
    
    

}
