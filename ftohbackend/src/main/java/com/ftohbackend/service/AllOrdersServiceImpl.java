package com.ftohbackend.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftohbackend.model.AllOrders;
import com.ftohbackend.model.Customer;
import com.ftohbackend.model.Seller;
import com.ftohbackend.repository.AllOrdersRepository;
import com.ftohbackend.repository.SellerRepository;
import com.ftohbackend.repository.customerRepository;

@Service
public class AllOrdersServiceImpl implements AllOrdersService {

    @Autowired
    private AllOrdersRepository allOrdersRepository;
    @Autowired
    private customerRepository customerRepository;
    
    @Autowired
    private SellerRepository sellerRepository;

    @Override
    public List<AllOrders> getAllOrders() {
        return allOrdersRepository.findAll();
    }

    @Override
    public AllOrders getOrderById(Integer orderId) {
        return allOrdersRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<AllOrders> getOrdersByCustomerId(Integer customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        return allOrdersRepository.findByCustomer(customer);
    }

    @Override
    public List<AllOrders> getOrdersBySellerId(Integer sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));
        
        return allOrdersRepository.findByProduct_Seller(seller);
    }

    @Override
    public AllOrders saveOrder(AllOrders order) {
        return allOrdersRepository.save(order);
    }

    @Override
    public void deleteOrder(Integer orderId) {
        allOrdersRepository.deleteById(orderId);
    }
}
