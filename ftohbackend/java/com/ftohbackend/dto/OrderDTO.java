package com.ftohbackend.dto;

<<<<<<< HEAD:ftohbackend/src/main/java/com/ftohbackend/dto/OrderDTO.java
=======
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
>>>>>>> 1e50210611e8d75ba3e3df9cb8c21ab93144df7b:ftohbackend/java/com/ftohbackend/dto/OrderDTO.java



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

