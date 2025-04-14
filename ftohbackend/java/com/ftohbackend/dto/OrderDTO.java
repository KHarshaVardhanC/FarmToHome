package com.ftohbackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
	
	
	private Integer orderId;
	@NotNull(message = "Product Id is required ")
	private Integer productId;
	@NotNull(message = "Invalid Product Quantity")
	private Double orderQuantity;
	@NotNull(message = "Customer Id is required ")
	private Integer customerId;
	public String orderStatus;

}
