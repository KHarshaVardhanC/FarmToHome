package com.ftohbackend.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftohbackend.model.AllOrders;
import com.ftohbackend.service.AllOrdersService;

@RestController
@RequestMapping("/orders")
public class AllOrdersController {

    @Autowired
    private AllOrdersService allOrdersService;

    // ✅ Get all orders
    @GetMapping("/all")
    public List<AllOrders> getAllOrders() {
        return allOrdersService.getAllOrders();
    }

    // ✅ Get order by ID
    @GetMapping("/{orderId}")
    public AllOrders getOrderById(@PathVariable Integer orderId) {
        return allOrdersService.getOrderById(orderId);
    }

    // ✅ Get orders by customer ID
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AllOrders>> getOrdersByCustomerId(@PathVariable Integer customerId) {
        List<AllOrders> orders = allOrdersService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<AllOrders>> getOrdersBySellerId(@PathVariable Integer sellerId) {
        List<AllOrders> orders = allOrdersService.getOrdersBySellerId(sellerId);
        return ResponseEntity.ok(orders);
    }

    // ✅ Create a new order
    @PostMapping("/add")
    public AllOrders addOrder(@RequestBody AllOrders order) {
        return allOrdersService.saveOrder(order);
    }

    // ✅ Delete order by ID
    @DeleteMapping("/delete/{orderId}")
    public String deleteOrder(@PathVariable Integer orderId) {
        allOrdersService.deleteOrder(orderId);
        return "Order with ID " + orderId + " deleted successfully!";
    }
}
