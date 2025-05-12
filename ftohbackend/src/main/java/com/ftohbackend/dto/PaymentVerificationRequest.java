package com.ftohbackend.dto;


import lombok.Data;
import java.util.List;

@Data
public class PaymentVerificationRequest {
    private String orderId;
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
    private String productId;
    private int customerId;
    private int orderQuantity;
    private String orderStatus;
    private String paymentStatus;
    private int amount;
    private List<Item> items;

    @Data
    public static class Item {
        private String productId;
        private float quantity;
        private int price;
    }
}
