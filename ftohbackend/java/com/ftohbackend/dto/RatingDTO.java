package com.ftohbackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {

	private Integer ratingId;
	@NotNull(message = "Customer Id is Required")
	private Integer customerId;
	@NotNull(message = "Product Id is Required")
	private Integer productId;
	@NotNull(message = "Rating Stars is Required")
	Double ratingValue;
	private String feedback;

   
}
