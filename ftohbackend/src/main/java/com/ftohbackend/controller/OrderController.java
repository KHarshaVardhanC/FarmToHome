package com.ftohbackend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ftohbackend.dto.CustomerOrderDTO;
import com.ftohbackend.dto.OrderDTO;
import com.ftohbackend.dto.SellerOrderDTO;
import com.ftohbackend.model.Order;

public interface OrderController {

	List<Order> getAllOrders() throws Exception;

	Order getOrderById(Integer orderId) throws Exception;

	List<CustomerOrderDTO> getOrdersByCustomerId(Integer customerId) throws Exception;

	public String addOrder(OrderDTO orderDTO) throws Exception;

	List<SellerOrderDTO> getOrdersBySellerId(Integer sellerId) throws Exception;

	String deleteOrder(Integer orderId) throws Exception;

	Order updateOrderStatus(Integer orderId, String orderStatus) throws Exception;

	
}
