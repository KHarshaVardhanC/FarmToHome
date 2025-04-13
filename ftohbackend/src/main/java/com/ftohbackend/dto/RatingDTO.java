package com.ftohbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {

	private Integer ratingId;
	private Integer customerId;
	private Integer productId;
	Double ratingValue;
	private String feedback;

}
