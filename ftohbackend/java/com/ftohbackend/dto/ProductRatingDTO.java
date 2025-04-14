package com.ftohbackend.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRatingDTO {

	Integer ratingId;
	Integer ratingValue;
	String feedback;
	String productName;
	String ImageUrl;
	LocalDateTime createdAt;
	
}
