package com.ftohbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ftohbackend.dto.CustomerOrderDTO;
import com.ftohbackend.exception.OrderException;
import com.ftohbackend.exception.ProductException;
import com.ftohbackend.model.Customer;
import com.ftohbackend.model.Order;
import com.ftohbackend.model.Product;
import com.ftohbackend.model.Seller;
import com.ftohbackend.repository.CustomerRepository;
import com.ftohbackend.repository.OrderRepository;
import com.ftohbackend.repository.SellerRepository;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private SellerRepository sellerRepository;

	@Autowired
	ProductService productService;

	@Override
	public List<Order> getAllOrders() throws OrderException {
		List<Order> orders = orderRepository.findAll();
		if (orders.isEmpty()) {
			throw new OrderException("No orders found");
		}
		return orders;
	}

	@Override
	public Order getOrderById(Integer orderId) throws OrderException {
		if (orderId == null) {
			throw new OrderException("Order ID cannot be null");
		}
		return orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderException("Order not found with ID: " + orderId));
	}

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
	public String addOrder(Order order) throws OrderException, ProductException, Exception {
		if (order == null || order.getCustomer() == null || order.getProduct() == null) {
			throw new OrderException("Order, customer, or product details cannot be null");
		}
		Product product = productService.getProduct(order.getProduct().getProductId());
		
		List<Order> orders=orderRepository.findByProductProductIdAndCustomerCustomerId(order.getProduct().getProductId() , order.getCustomer().getCustomerId());
		
		if(orders.size() != 0 )
		{
			for(Order ord: orders)
			{
				if(ord.getOrderStatus().equalsIgnoreCase("incart") || ord.getOrderStatus().equalsIgnoreCase("in cart"))
				{
					if(order.getOrderQuantity() + ord.getOrderQuantity() <= product.getProductQuantity())
					{
						ord.setOrderQuantity(order.getOrderQuantity()+ord.getOrderQuantity());
						orderRepository.save(ord);
						return "Order added Successfully";
						
					}
					else
					{
						return "Order Quantity Exceeded";
					}
				}
			}
		}
		if (order.getOrderQuantity() <= product.getProductQuantity()) {
//    		 product.setProductQuantity( product.getProductQuantity() - order.getOrderQuantity());
			productService.updateProduct(product.getProductId(), product);
			orderRepository.save(order);
			return "Order Successful";
		} else {
			return "Order Unsuccessful and Not had Sufficient Quantity";
		}

	}

	@Override
	public List<Order> getOrdersBySellerId(Integer sellerId) throws OrderException {
		// TODO Auto-generated method stub
		if (sellerId == null) {
			throw new OrderException("Seller ID cannot be null");
		}

		
		List<Order> orders = orderRepository.findByProductSellerSellerId(sellerId);
		
		

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
	public String updateOrderStatus(Integer orderId, String newStatus)
			throws Exception, OrderException, ProductException {
		if (orderId == null) {
			throw new OrderException("Order ID cannot be null");
		}

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderException("Order not found with ID: " + orderId));

		// Validate the new status
		if (!isValidOrderStatus(newStatus)) {
			throw new OrderException("Invalid order status: " + newStatus);
		}
		if (newStatus.equalsIgnoreCase("ordered") || newStatus.equalsIgnoreCase("Ordered")) {

//        System.out.println(order.getOrderStatus());
			Product product = order.getProduct();

			if (product.getProductQuantity() >= order.getOrderQuantity()) {

				product.setProductQuantity(product.getProductQuantity() - order.getOrderQuantity());
				productService.updateProduct(product.getProductId(), product);
				order.setOrderStatus(newStatus);
				orderRepository.save(order);
				return "Order Status updated Succesfully " + newStatus;
			} else {
				order.setOrderStatus("failed");
				orderRepository.save(order);
				return "Quantity Exceeded! \n  Order failed \n try Again";
			}
		} else if (newStatus.equalsIgnoreCase("delivered") || newStatus.equalsIgnoreCase("Delivered")) {
			order.setOrderStatus(newStatus);
			orderRepository.save(order);
			return "Ordered Delivered Successfully";

		}

		return "success";

	}

	private boolean isValidOrderStatus(String status) {
		return status != null && (status.equalsIgnoreCase("Incart") || status.equalsIgnoreCase("Ordered")
				|| status.equalsIgnoreCase("Delivered") || status.equalsIgnoreCase("Deleted")
				|| status.equalsIgnoreCase("Failed"));
	}

	@Override
	public CustomerOrderDTO getOrderInvoice(Integer orderId) throws Exception {

		CustomerOrderDTO customerOrderDTO = new CustomerOrderDTO();
		Order order = orderRepository.findById(orderId).get();
		customerOrderDTO.setOrderId(order.getOrderId());
		customerOrderDTO.setOrderQuantity(order.getOrderQuantity());
		customerOrderDTO.setOrderStatus(order.getOrderStatus());

		Product product = order.getProduct();
		customerOrderDTO.setProductName(product.getProductName());
		customerOrderDTO.setProductDescription(product.getProductDescription());
		customerOrderDTO.setProductPrice(product.getProductPrice());
		customerOrderDTO.setProductQuantityType(product.getProductQuantityType());
		customerOrderDTO.setImageUrl(product.getImageUrl());

		Customer customer = order.getCustomer();
		customerOrderDTO.setCustomerName(customer.getCustomerFirstName() + " " + customer.getCustomerLastName());
		customerOrderDTO.setCustomerCity(customer.getCustomerCity());
		customerOrderDTO.setCustomerPlace(customer.getCustomerPlace());
		customerOrderDTO.setCustomerPincode(customer.getCustomerPincode());
		customerOrderDTO.setCustomerState(customer.getCustomerState());

		Seller seller = order.getProduct().getSeller();
		customerOrderDTO.setSellerName(seller.getSellerFirstName() + " " + seller.getSellerLastName());
		customerOrderDTO.setSellerCity(seller.getSellerCity());
		customerOrderDTO.setSellerPlace(seller.getSellerPlace());
		customerOrderDTO.setSellerPincode(seller.getSellerPincode());
		customerOrderDTO.setSellerState(seller.getSellerState());

		return customerOrderDTO;
	}
}
