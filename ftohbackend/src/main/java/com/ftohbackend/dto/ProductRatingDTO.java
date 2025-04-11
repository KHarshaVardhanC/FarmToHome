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
	Double ratingValue;
	String feedback;
	String productName;
	String ImageUrl;
	LocalDateTime createdAt;
//	public ProductRatingDTO() {
//		super();
//		// TODO Auto-generated constructor stub
//	}
//	
//	public ProductRatingDTO(Integer ratingId, Double ratingValue, String feedback, String productName, String imageUrl,
//			LocalDateTime createdAt) {
//		super();
//		this.ratingId = ratingId;
//		this.ratingValue = ratingValue;
//		this.feedback = feedback;
//		this.productName = productName;
//		ImageUrl = imageUrl;
//		this.createdAt = createdAt;
//	}
//
//	public Integer getRatingId() {
//		return ratingId;
//	}
//	public void setRatingId(Integer ratingId) {
//		this.ratingId = ratingId;
//	}
//	public Double getRatingValue() {
//		return ratingValue;
//	}
//	public void setRatingValue(Double ratingValue) {
//		this.ratingValue = ratingValue;
//	}
//	public String getFeedback() {
//		return feedback;
//	}
//	public void setFeedback(String feedback) {
//		this.feedback = feedback;
//	}
//	public String getProductName() {
//		return productName;
//	}
//	public void setProductName(String productName) {
//		this.productName = productName;
//	}
//	public LocalDateTime getCreatedAt() {
//		return createdAt;
//	}
//	public void setCreatedAt(LocalDateTime createdAt) {
//		this.createdAt = createdAt;
//	}
//
//	public String getImageUrl() {
//		return ImageUrl;
//	}
//
//	public void setImageUrl(String imageUrl) {
//		ImageUrl = imageUrl;
//	}
//
//	@Override
//	public String toString() {
//		return "ProductRatingDTO [ratingId=" + ratingId + ", ratingValue=" + ratingValue + ", feedback=" + feedback
//				+ ", productName=" + productName + ", ImageUrl=" + ImageUrl + ", createdAt=" + createdAt + "]";
//	}
//	
//	
	
	
}
