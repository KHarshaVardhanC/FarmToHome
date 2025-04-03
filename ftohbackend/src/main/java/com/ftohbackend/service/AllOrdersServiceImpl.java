package com.ftohbackend.service;


import com.ftohbackend.model.AllOrders;
import com.ftohbackend.repository.AllOrdersRepository;
import com.ftohbackend.service.AllOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AllOrdersServiceImpl implements AllOrdersService {

    @Autowired
    private AllOrdersRepository allOrdersRepository;

    @Override
    public AllOrders saveOrder(AllOrders order) {
        return allOrdersRepository.save(order);
    }

    @Override
    public List<AllOrders> getAllOrders() {
        return allOrdersRepository.findAll();
    }

    @Override
    public List<AllOrders> getOrdersByCustomerId(Integer customerId) {
        return allOrdersRepository.findByCustomerCustomerId(customerId);
    }

    @Override
    public List<AllOrders> getOrdersBySellerId(Integer sellerId) {
        return allOrdersRepository.findByProductSellerSellerId(sellerId);
    }

    @Override
    public AllOrders getOrderById(Integer orderId) {
        Optional<AllOrders> order = allOrdersRepository.findById(orderId);
        return order.orElse(null);
    }

    @Override
    public void deleteOrder(Integer orderId) {
        allOrdersRepository.deleteById(orderId);
    }
}

