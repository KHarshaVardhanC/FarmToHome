package com.ftohbackend.service;

import java.util.List;

import com.ftohbackend.dto.CustomerOrderDTO;
import com.ftohbackend.exception.OrderException;
import com.ftohbackend.exception.ProductException;
import com.ftohbackend.model.Order;

public interface OrderService {

	List<Order> getAllOrders() throws OrderException;

	Order getOrderById(Integer orderId)throws OrderException;

	List<Order> getOrderByCustomerId(Integer customerId)throws OrderException;

	String addOrder(Order order)throws OrderException, ProductException, Exception;

	List<Order> getOrdersBySellerId(Integer sellerId) throws OrderException;

	String deleteOrder(Integer orderId) throws OrderException;

	String updateOrderStatus(Integer orderId, String orderStatus) throws Exception;

	CustomerOrderDTO getOrderInvoice(Integer orderId) throws Exception;


}
