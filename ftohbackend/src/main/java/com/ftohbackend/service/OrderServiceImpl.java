package com.ftohbackend.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftohbackend.exception.OrderException;
import com.ftohbackend.model.Order;
import com.ftohbackend.repository.OrderRepository;
import com.ftohbackend.repository.SellerRepository;
import com.ftohbackend.repository.customerRepository;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private customerRepository customerRepository;
    
    @Autowired
    private SellerRepository sellerRepository;

    @Override
    public List<Order> getAllOrders()throws OrderException {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Integer orderId)throws OrderException {
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<Order> getOrderByCustomerId(Integer customerId)throws OrderException {
//        Customer customer = customerRepository.findById(customerId)
//                .orElseThrow(() -> new RuntimeException("Customer not found"));
//        
//        return orderRepository.findByCustomerId(customer);
    	
    	return null;
    }
//
//    @Override
//    public List<AllOrders> getOrdersBySellerId(Integer sellerId) {
//        Seller seller = sellerRepository.findById(sellerId)
//                .orElseThrow(() -> new RuntimeException("Seller not found"));
//        
//        return allOrdersRepository.findByProduct_Seller(seller);
//    }

    @Override
    public String addOrder(Order order)throws OrderException {
         orderRepository.save(order);
         
         return "Order Successful";
    }

//    @Override
//    public void deleteOrder(Integer orderId) {
//        allOrdersRepository.deleteById(orderId);
//    }
}
