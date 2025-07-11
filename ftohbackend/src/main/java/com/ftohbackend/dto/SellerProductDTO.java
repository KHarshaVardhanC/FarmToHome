package com.ftohbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerProductDTO {
	Integer productId;
	Double productPrice;
	String ImageUrl;
	String productDescription;
	String productQuantityType;
	String productName;
	Double productQuantity;
	Double productRatingValue;
	Integer productRatingCount;
	Double discountPercentage;
	Double minOrderQuantity;

}
