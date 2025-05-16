package com.ftohbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrderDTO {

	Integer orderId;
	Integer productId;
	String productName;
	String ImageUrl;
	String ProductDescription;
	Double orderQuantity;
	Double productPrice;
	Double productQuantity;
	String orderStatus;
	String sellerName;
	String sellerPlace;
	String sellerCity;
	String sellerState;
	String sellerPincode;
	String customerName;
	String customerPlace;
	String customerCity;
	String customerState;
	String customerPincode;
	String productQuantityType;
	String orderRatingStatus;
	Double discountPercentage;
	Double minOrderQuantity;
	Double orderPrice;
	
	String orderReportStatus;
	
}
