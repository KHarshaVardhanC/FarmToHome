package com.ftohbackend.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftohbackend.dto.CustomerOrderDTO;
import com.ftohbackend.dto.OrderDTO;
import com.ftohbackend.dto.SellerOrderDTO;
import com.ftohbackend.model.Customer;
import com.ftohbackend.model.Order;
import com.ftohbackend.model.Product;
import com.ftohbackend.model.Seller;
import com.ftohbackend.service.CustomerService;
import com.ftohbackend.service.OrderService;
import com.ftohbackend.service.ProductService;
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

	@GetMapping("")
	@Override
	public List<Order> getAllOrders() throws Exception {
		return orderService.getAllOrders();
	}

	@GetMapping("/{orderId}")
	@Override
	public Order getOrderById(@PathVariable Integer orderId) throws Exception {
		return orderService.getOrderById(orderId);
	}

	@GetMapping("/customer/{customerId}")
	@Override
	public List<CustomerOrderDTO> getOrdersByCustomerId(@PathVariable Integer customerId) throws Exception {
		List<Order> orders = orderService.getOrderByCustomerId(customerId);
		List<CustomerOrderDTO> customerorders = new ArrayList<>();

		for (Order order : orders) {
			CustomerOrderDTO customerorderdto = new CustomerOrderDTO();
			customerorderdto.setOrderId(order.getOrderId());
			customerorderdto.setOrderQuantity(order.getOrderQuantity());

			Product product = order.getProduct();
			customerorderdto.setProductName(product.getProductName());
			customerorderdto.setProductPrice(product.getProductPrice());
			customerorderdto.setImageUrl(product.getImageUrl());
			customerorderdto.setProductDescription(product.getProductDescription());

			Seller seller = product.getSeller();
			customerorderdto.setSellerName(seller.getSellerFirstName() + " " + seller.getSellerLastName());
			customerorders.add(customerorderdto);
		}
		return customerorders;
	}
	
	@Override
	@PutMapping("/order/{orderId}/{orderStatus}")
	public Order updateOrderStatus(@PathVariable Integer orderId,@PathVariable String orderStatus) throws Exception
	{
		
		return orderService.updateOrderStatus(orderId,  orderStatus);
	}

	@GetMapping("/seller/{sellerId}")
	@Override
	public List<SellerOrderDTO> getOrdersBySellerId(@PathVariable Integer sellerId) throws Exception {
		List<SellerOrderDTO> sellerorders = new ArrayList<>();

		List<Order> orders = orderService.getOrdersBySellerId(sellerId);

		for (Order order : orders) {
			SellerOrderDTO sellerorderdto = new SellerOrderDTO();
			sellerorderdto.setOrderId(order.getOrderId());
			sellerorderdto.setOrderQuantity(order.getOrderQuantity());

			Product product = order.getProduct();

			sellerorderdto.setProductName(product.getProductName());
			sellerorderdto.setProductPrice(product.getProductPrice());
			sellerorderdto.setImageUrl(product.getImageUrl());
			sellerorderdto.setProductDescription(product.getProductDescription());

			Customer customer = order.getCustomer();
			sellerorderdto.setCustomerName(customer.getCustomerFirstName() + " " + customer.getCustomerLastName());

			sellerorders.add(sellerorderdto);

		}
		return sellerorders;
	}

	@PostMapping("/add")
	@Override
	public String addOrder(@RequestBody OrderDTO orderDTO) throws Exception {

		
		
		Order order = modelMapper.map(orderDTO, Order.class);
		order.setOrderStatus("In Cart");
		
		return orderService.addOrder(order);
	}

    @DeleteMapping("/delete/{orderId}")
    @Override
    public String deleteOrder(@PathVariable Integer orderId) throws Exception{
        orderService.deleteOrder(orderId);
        return "Order with ID " + orderId + " deleted successfully!";
    }

}
