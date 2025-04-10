package com.ftohbackend.controller;

import java.util.List;

import com.ftohbackend.dto.CustomerOrderDTO;
import com.ftohbackend.dto.OrderDTO;
import com.ftohbackend.dto.SellerOrderDTO;
import com.ftohbackend.exception.OrderException;
import com.ftohbackend.model.Order;

public interface OrderController {

	List<Order> getAllOrders() throws Exception;

	Order getOrderById(Integer orderId) throws Exception;

	List<CustomerOrderDTO> getOrdersByCustomerId(Integer customerId) throws OrderException;

	public String addOrder(OrderDTO orderDTO) throws OrderException, Exception;

	List<SellerOrderDTO> getOrdersBySellerId(Integer sellerId) throws OrderException, Exception;

	String deleteOrder(Integer orderId) throws OrderException, Exception;

	String updateOrderStatus(Integer orderId, String orderStatus) throws OrderException, Exception;


	CustomerOrderDTO getOrderInvoice(Integer orderId) throws Exception, OrderException;

	
}
