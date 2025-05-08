package com.ftohbackend.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftohbackend.dto.CustomerOrderDTO;
import com.ftohbackend.dto.OrderDTO;
import com.ftohbackend.dto.OrderReport;
import com.ftohbackend.dto.SellerOrderDTO;
import com.ftohbackend.exception.OrderException;
import com.ftohbackend.model.Customer;
import com.ftohbackend.model.Order;
import com.ftohbackend.model.Product;
import com.ftohbackend.model.Seller;
import com.ftohbackend.service.CustomerService;
import com.ftohbackend.service.OrderService;
import com.ftohbackend.service.OrderServiceImpl;
import com.ftohbackend.service.ProductService;
import com.ftohbackend.service.RatingService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/order")
public class OrderControllerImpl implements OrderController {

	@Autowired
	OrderService orderService;

	@Autowired
	ProductService productService;

	@Autowired
	CustomerService customerService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	RatingService ratingService;

	@GetMapping("")
	@Override
	public List<Order> getAllOrders() throws Exception {
		return orderService.getAllOrders();
	}

//	@GetMapping("/{orderId}") 
	@Override
	public Order getOrderById(@PathVariable Integer orderId) throws Exception {
		return orderService.getOrderById(orderId);
	}

	
	@GetMapping("/orders/incart/{customerId}")
	@Override
	public List<CustomerOrderDTO> getCartOrdersByCustomerId(@PathVariable Integer customerId)
			throws OrderException, Exception {
		List<Order> orders = orderService.getOrderByCustomerId(customerId);
		List<CustomerOrderDTO> customerorders = new ArrayList<>();

		for (Order order : orders) {

			if (order.getOrderStatus().equalsIgnoreCase("Incart")
					|| order.getOrderStatus().equalsIgnoreCase("In cart")) {

				CustomerOrderDTO customerorderdto = new CustomerOrderDTO();
				customerorderdto.setOrderId(order.getOrderId());
				customerorderdto.setOrderQuantity(order.getOrderQuantity());
				customerorderdto.setOrderStatus(order.getOrderStatus());

				Product product = order.getProduct();
				customerorderdto.setProductName(product.getProductName());
				customerorderdto.setProductPrice(product.getProductPrice());
				customerorderdto.setImageUrl(product.getImageUrl());
				customerorderdto.setProductDescription(product.getProductDescription());
				customerorderdto.setProductQuantityType(product.getProductQuantityType());
				customerorderdto.setOrderRatingStatus(ratingService.getRatingByOrderId(customerId) + "");

				Seller seller = product.getSeller();
				customerorderdto.setSellerName(seller.getSellerFirstName() + " " + seller.getSellerLastName());
				customerorders.add(customerorderdto);
			}
		}
		return customerorders;
	}

	@GetMapping("/customer/{customerId}")
	@Override
	public List<CustomerOrderDTO> getOrdersByCustomerId(@PathVariable Integer customerId) throws OrderException {
		List<Order> orders = orderService.getOrderByCustomerId(customerId);
		List<CustomerOrderDTO> customerorders = new ArrayList<>();

		for (Order order : orders) {

			if (!order.getOrderStatus().equalsIgnoreCase("Incart")
					&& !order.getOrderStatus().equalsIgnoreCase("In cart")) {

				CustomerOrderDTO customerorderdto = new CustomerOrderDTO();
				customerorderdto.setOrderId(order.getOrderId());
				customerorderdto.setOrderQuantity(order.getOrderQuantity());

				Product product = order.getProduct();
				customerorderdto.setProductName(product.getProductName());
				customerorderdto.setProductPrice(product.getProductPrice());
				customerorderdto.setImageUrl(product.getImageUrl());
				customerorderdto.setProductDescription(product.getProductDescription());
				customerorderdto.setOrderStatus(order.getOrderStatus());
				customerorderdto.setProductQuantityType(product.getProductQuantityType());
				
				if(ratingService.getRatingByOrderId(order.getOrderId()))
				{
					customerorderdto.setOrderRatingStatus("Rated");
				}
				else
				{
					customerorderdto.setOrderRatingStatus("Not Rated");
					
				}
				
				Seller seller = product.getSeller();
				customerorderdto.setSellerName(seller.getSellerFirstName() + " " + seller.getSellerLastName());
				customerorders.add(customerorderdto);
			}
			
			
		}
		
		
		Collections.sort(customerorders, (order1, order2)->{
			if(order1.getOrderRatingStatus().equals(order2.getOrderRatingStatus()))
			{
				return order2.getOrderId()-order1.getOrderId();
				
			}
			return order1.getOrderRatingStatus().compareTo(order2.getOrderRatingStatus());
		});
		return customerorders;
	}

	@Override
	@PutMapping("/order/{orderId}/{orderStatus}")
	public String updateOrderStatus(@PathVariable Integer orderId, @PathVariable String orderStatus)
			throws Exception, OrderException {

		return orderService.updateOrderStatus(orderId, orderStatus);

	}
	
	@Override
	@PutMapping("/update/{orderId}/{orderQuantity}")
	public String updateOrderQuantity(@PathVariable Integer orderId,@PathVariable Double orderQuantity)
	{
		return orderService.updateOrderQuantity(orderId, orderQuantity);
	}
	
	

	@GetMapping("/seller/{sellerId}")
	@Override
	public List<SellerOrderDTO> getOrdersBySellerId(@PathVariable Integer sellerId) throws Exception, OrderException {
		List<SellerOrderDTO> sellerorders = new ArrayList<>();

		List<Order> orders = orderService.getOrdersBySellerId(sellerId);

		for (Order order : orders) {
			if (!order.getOrderStatus().equalsIgnoreCase("Incart")
					&& !order.getOrderStatus().equalsIgnoreCase("In cart") ) 
			{
				

				SellerOrderDTO sellerorderdto = new SellerOrderDTO();
				sellerorderdto.setOrderId(order.getOrderId());
				sellerorderdto.setOrderQuantity(order.getOrderQuantity());
				sellerorderdto.setOrderStatus(order.getOrderStatus());

				Product product = order.getProduct();

				sellerorderdto.setProductName(product.getProductName());
				sellerorderdto.setProductPrice(product.getProductPrice());
				sellerorderdto.setImageUrl(product.getImageUrl());
				sellerorderdto.setProductDescription(product.getProductDescription());
				sellerorderdto.setProductQuantityType(product.getProductQuantityType());

				Customer customer = order.getCustomer();
				sellerorderdto.setCustomerName(customer.getCustomerFirstName() + " " + customer.getCustomerLastName());

				
				sellerorders.add(sellerorderdto);

			}
		}
		
		Collections.sort(sellerorders, (order1,order2)->{
			if(order1.getOrderStatus().toLowerCase().equals(order2.getOrderStatus().toLowerCase()))
			{
				return order1.getOrderId()-order2.getOrderId();
			}
			return order2.getOrderStatus().toLowerCase().compareTo(order1.getOrderStatus().toLowerCase());
		});
		
		
		
		return sellerorders;
	}

	@PostMapping("/add")
	@Override
	public String addOrder(@RequestBody OrderDTO orderDTO) throws Exception, OrderException {

		Order order = modelMapper.map(orderDTO, Order.class);
		order.setOrderStatus("Incart");

		
		return orderService.addOrder(order);
	}

	@DeleteMapping("/delete/{orderId}")
	@Override
	public String deleteOrder(@PathVariable Integer orderId) throws Exception, OrderException {
		orderService.deleteOrder(orderId);
		return "Order with ID " + orderId + " deleted successfully!";
	}

	@Override
	@GetMapping("/invoice/{orderId}")
	public CustomerOrderDTO getOrderInvoice(@PathVariable Integer orderId) throws Exception, OrderException {
		return orderService.getOrderInvoice(orderId);
	}
	
	@Override
	@PostMapping(value= "/report", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String addOrderReport(@ModelAttribute OrderReport orderReport) throws Exception
	{
		return orderService.addOrderReport(orderReport);
	}
}
