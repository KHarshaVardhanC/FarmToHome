package com.ftohbackend.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftohbackend.exception.OrderException;
import com.ftohbackend.model.Customer;
import com.ftohbackend.model.Order;
import com.ftohbackend.repository.CustomerRepository;
import com.ftohbackend.repository.OrderRepository;
import com.ftohbackend.repository.SellerRepository;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private SellerRepository sellerRepository;

    @Override
    public List<Order> getAllOrders()throws OrderException {
    	List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            throw new OrderException("No orders found");
        }
        return orders;    }

    @Override
    public Order getOrderById(Integer orderId)throws OrderException {
    	if (orderId == null) {
            throw new OrderException("Order ID cannot be null");
        }
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Order not found with ID: " + orderId));    }

    @Override
    public List<Order> getOrderByCustomerId(Integer customerId) throws OrderException {
    	  if (customerId == null) {
              throw new OrderException("Customer ID cannot be null");
          }
          Customer customer = customerRepository.findById(customerId)
                  .orElseThrow(() -> new OrderException("Customer not found with ID: " + customerId));
          
          List<Order> orders = orderRepository.findByCustomerCustomerId(customerId);
          if (orders.isEmpty()) {
              throw new OrderException("No orders found for customer ID: " + customerId);
          }

          return orders;
    }


    @Override
    public String addOrder(Order order)throws OrderException {
    	 if (order == null || order.getCustomer() == null || order.getProduct() == null) {
             throw new OrderException("Order, customer, or product details cannot be null");
         }
         orderRepository.save(order);
         return "Order Successful";
    }

	@Override
	public List<Order> getOrdersBySellerId(Integer sellerId) throws OrderException {
		// TODO Auto-generated method stub
		 if (sellerId == null) {
	            throw new OrderException("Seller ID cannot be null");
	        }
		List<Order> orders= orderRepository.findAll().stream().filter(x->x.getProduct().getSeller().getSellerId()==sellerId).toList();

        if (orders.isEmpty()) {
            throw new OrderException("No orders found for seller ID: " + sellerId);
        }

        return orders;
	}

    @Override
    public String deleteOrder(Integer orderId) throws OrderException {
    	   if (!orderRepository.existsById(orderId)) {
               throw new OrderException("Order not found with ID: " + orderId);
           }
           orderRepository.deleteById(orderId);
           return "Order Deletion Successful";
    }

    @Override
    public Order updateOrderStatus(Integer orderId, String newStatus) throws OrderException {
        if (orderId == null) {
            throw new OrderException("Order ID cannot be null");
        }
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Order not found with ID: " + orderId));
        
        // Validate the new status
        if (!isValidOrderStatus(newStatus)) {
            throw new OrderException("Invalid order status: " + newStatus);
        }
        
        order.setOrderStatus(newStatus);
        return orderRepository.save(order);
    }

    private boolean isValidOrderStatus(String status) {
        return status != null && (
            status.equals("In cart") ||
            status.equals("Ordered") ||
            status.equals("Success") ||
            status.equals("Deleted") ||
            status.equals("Failed")
        );
    }
}
}
