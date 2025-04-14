package com.ftohbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
	private Integer orderId;
	private Integer productId;
	private Double orderQuantity;
	private Integer customerId;
	public String orderStatus;

}
