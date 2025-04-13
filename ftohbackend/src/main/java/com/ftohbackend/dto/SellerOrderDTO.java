package com.ftohbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerOrderDTO {

	Integer orderId;
	String productName;
	String ImageUrl;
	String productDescription;
	Double orderQuantity;
	String productQuantityType;
	Double productPrice;
	String customerName;
	String orderStatus;

}
