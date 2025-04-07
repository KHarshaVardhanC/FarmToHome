package com.ftohbackend.service;

import java.util.List;

import com.ftohbackend.dto.OrderDTO;
import com.ftohbackend.model.Order;

public interface OrderService {

	List<Order> getAllOrders();

	Order getOrderById(Integer orderId);

	List<Order> getOrderByCustomerId(Integer customerId);

	String addOrder(Order order);


}
