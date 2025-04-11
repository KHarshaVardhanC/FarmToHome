package com.ftohbackend.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "rating")
@Data
public class Rating {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ratingId;

	@ManyToOne
	@JoinColumn(name = "customerId", nullable = false)
	private Customer customer;

	@ManyToOne
	@JoinColumn(name = "productId", nullable = false)
	private Product product;

	Double ratingValue;
	private String feedback;

	@CreationTimestamp
	private LocalDateTime createdAt;

//	public Rating() {
//	}
//
//	public Rating(Integer ratingId, Customer customer, Product product, Double ratingValue, String feedback) {
//		super();
//		this.ratingId = ratingId;
//		this.customer = customer;
//		this.product = product;
//		this.ratingValue = ratingValue;
//		this.feedback = feedback;
//		this.createdAt = LocalDateTime.now();
//	}
//
//	public Integer getRatingId() {
//		return ratingId;
//	}
//
//	public void setRatingId(Integer ratingId) {
//		this.ratingId = ratingId;
//	}
//
//	public Customer getCustomer() {
//		return customer;
//	}
//
//	public void setCustomer(Customer customer) {
//		this.customer = customer;
//	}
//
//	public Product getProduct() {
//		return product;
//	}
//
//	public void setProduct(Product product) {
//		this.product = product;
//	}
//
//	public Double getRatingValue() {
//		return ratingValue;
//	}
//
//	public void setRatingValue(Double ratingValue) {
//		this.ratingValue = ratingValue;
//	}
//
//	public String getFeedback() {
//		return feedback;
//	}
//
//	public void setFeedback(String feedback) {
//		this.feedback = feedback;
//	}
//
//	public LocalDateTime getCreatedAt() {
//		return createdAt;
//	}
//
//	public void setCreatedAt() {
//		this.createdAt = LocalDateTime.now();
//	}
//
//	
//
//	@Override
//	public String toString() {
//		return "Rating [ratingId=" + ratingId + ", customer=" + customer + ", product=" + product + ", rating=" + ratingValue
//				+ ", feedback=" + feedback + ", createdAt=" + createdAt +  "]";
//	}

}
