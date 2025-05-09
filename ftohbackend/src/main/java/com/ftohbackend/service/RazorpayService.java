package com.ftohbackend.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Service
public class RazorpayService {

    @Value("${razorpay.api.key}")
    private String razorpayKeyId;

    @Value("${razorpay.api.secret}")
    private String razorpayKeySecret;

    public String createOrder(int amount, String currency, String receipt) throws Exception {
        try {
            RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

            // Create the order request
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount);  // Amount in paise (100 paise = 1 INR)
            orderRequest.put("currency", currency);
            orderRequest.put("receipt", receipt);
            orderRequest.put("payment_capture", 1);  // automatic payment capture

            // Create the order in Razorpay
            com.razorpay.Order razorpayOrder = razorpayClient.orders.create(orderRequest);

            // Extract Razorpay Order ID from the created order
            String razorpayOrderId = razorpayOrder.get("id");

            return razorpayOrderId;
        } catch (RazorpayException e) {
            throw new Exception("Error creating Razorpay order: " + e.getMessage());
        }
    }
}
