package com.ftohbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerProductDTO {
    private Integer productId;
    private String productName;
    private Double productPrice;
    public String ImageUrl;
    public String productDescription;
    private Double productQuantity;
    private String sellerName;
    private String sellerPlace;
    private String sellerCity;
    Double productRatingValue;
    Integer productRatingCount;
    
    
    

	
}