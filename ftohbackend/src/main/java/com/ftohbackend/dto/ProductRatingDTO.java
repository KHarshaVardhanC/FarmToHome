package com.ftohbackend.dto;

import java.time.LocalDateTime;

public class ProductRatingDTO {

	Integer ratingId;
	Integer ratingValue;
	String feedback;
	String productName;
	LocalDateTime createdAt;
	public ProductRatingDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ProductRatingDTO(Integer ratingId, Integer ratingValue, String feedback, String productName,
			LocalDateTime createdAt) {
		super();
		this.ratingId = ratingId;
		this.ratingValue = ratingValue;
		this.feedback = feedback;
		this.productName = productName;
		this.createdAt = createdAt;
	}
	public Integer getRatingId() {
		return ratingId;
	}
	public void setRatingId(Integer ratingId) {
		this.ratingId = ratingId;
	}
	public Integer getRatingValue() {
		return ratingValue;
	}
	public void setRatingValue(Integer ratingValue) {
		this.ratingValue = ratingValue;
	}
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	@Override
	public String toString() {
		return "ProductRatingDTO [ratingId=" + ratingId + ", ratingValue=" + ratingValue + ", feedback=" + feedback
				+ ", productName=" + productName + ", createdAt=" + createdAt +  "]";
	}
	
	
}
