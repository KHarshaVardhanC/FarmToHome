package com.ftohbackend.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllOrdersDTO {
    private Integer orderId;
    private Integer productId;
    private String productName;
    private Double productPrice;
    private Double productQuantity;
    private Integer sellerId;
    private Integer customerId;
}

