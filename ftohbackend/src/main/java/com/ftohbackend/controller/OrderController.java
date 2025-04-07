package com.ftohbackend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ftohbackend.dto.OrderDTO;
import com.ftohbackend.model.Order;

public interface OrderController {

	List<Order> getAllOrders();

	Order getOrderById(Integer orderId);

	ResponseEntity<List<Order>> getOrdersByCustomerId(Integer customerId);

	public String addOrder(OrderDTO orderDTO);

}
