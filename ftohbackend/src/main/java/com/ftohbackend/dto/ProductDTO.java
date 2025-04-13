package com.ftohbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
	Integer productId;
	Integer sellerId;
	Double productPrice;
	String productName;
	Double productQuantity;
	String productQuantityType;
	String ImageUrl;
	String productDescription;
	String productCategory;
	Double productRatingValue;
	Integer productRatingCount;


}
