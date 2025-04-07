package com.ftohbackend.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftohbackend.dto.OrderDTO;
import com.ftohbackend.model.Customer;
import com.ftohbackend.model.Order;
import com.ftohbackend.model.Product;
import com.ftohbackend.service.CustomerService;
import com.ftohbackend.service.OrderService;
import com.ftohbackend.service.ProductService;

@RestController
@RequestMapping("/order")
public class OrderControllerImpl implements OrderController {

	@Autowired
    private OrderService orderService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	ModelMapper modelMapper;

	@GetMapping("")
    @Override
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

	  @GetMapping("/{orderId}")
    @Override
    public Order getOrderById(@PathVariable Integer orderId) {
        return orderService.getOrderById(orderId);
    }
	  
	

	  
    @GetMapping("/customer/{customerId}")
    @Override
    public ResponseEntity<List<Order>> getOrdersByCustomerId(@PathVariable Integer customerId) {
//        List<Order> orders = orderService.getOrderByCustomerId(customerId);
//        return ResponseEntity.ok(orders);
    	return null;
    }
//
//    @GetMapping("/seller/{sellerId}")
//    @Override
//    public ResponseEntity<List<AllOrders>> getOrdersBySellerId(@PathVariable Integer sellerId) {
//        List<AllOrders> orders = allOrdersService.getOrdersBySellerId(sellerId);
//        return ResponseEntity.ok(orders);
//    }
//

    @PostMapping("/add")
    @Override
    public String addOrder(@RequestBody OrderDTO orderDTO) {
   	 Order order=new Order();
     order.setOrderId(orderDTO.getOrderId());
        order.setOrderQuantity(orderDTO.getOrderQuantity());
        
       Product product=productService.getProduct(orderDTO.getProductId());
       order.setProduct(product);
       
       Customer customer=customerService.getCustomer(orderDTO.getCustomerId());
       order.setCustomer(customer);
        
        		System.out.println("Hello1");
        System.out.println("Hello2");
        return orderService.addOrder(order);
    }
//
//    // ✅ Delete order by ID
//    @DeleteMapping("/delete/{orderId}")
//    @Override
//    public String deleteOrder(@PathVariable Integer orderId) {
//        allOrdersService.deleteOrder(orderId);
//        return "Order with ID " + orderId + " deleted successfully!";
//    }

	
}
