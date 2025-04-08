package com.ftohbackend.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftohbackend.model.Customer;
import com.ftohbackend.model.Order;
import com.ftohbackend.repository.OrderRepository;
import com.ftohbackend.repository.SellerRepository;
import com.ftohbackend.repository.customerRepository;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private SellerRepository sellerRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Integer orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<Order> getOrderByCustomerId(Integer customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        List<Order> orders= orderRepository.findByCustomerCustomerId(customerId);
    	
    	return orders;
    }


    @Override
    public String addOrder(Order order) {
         orderRepository.save(order);
         
         return "Order Successful";
    }

	@Override
	public List<Order> getOrdersBySellerId(Integer sellerId) {
		// TODO Auto-generated method stub
		
		return orderRepository.findAll().stream().filter(x->x.getProduct().getSeller().getSellerId()==sellerId).toList();
	}

    @Override
    public String deleteOrder(Integer orderId) {
        orderRepository.deleteById(orderId);
        return "Order Deletion Successful";
    }
}
