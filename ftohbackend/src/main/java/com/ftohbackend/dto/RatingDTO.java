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
	@NotNull(message = "Order Id is Required")
	Integer orderId;
	Double ratingValue;
	private String feedback;


}
