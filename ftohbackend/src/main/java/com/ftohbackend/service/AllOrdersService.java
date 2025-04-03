package com.ftohbackend.service;


import com.ftohbackend.model.AllOrders;
import java.util.List;

public interface AllOrdersService {
    
    // Save a new order
    AllOrders saveOrder(AllOrders order);

    // Retrieve all orders
    List<AllOrders> getAllOrders();

    // Get orders by customer ID
    List<AllOrders> getOrdersByCustomerId(Integer customerId);

    // Get orders by seller ID
    List<AllOrders> getOrdersBySellerId(Integer sellerId);

    // Get an order by order ID
    AllOrders getOrderById(Integer orderId);

    // Delete an order by order ID
    void deleteOrder(Integer orderId);
}
