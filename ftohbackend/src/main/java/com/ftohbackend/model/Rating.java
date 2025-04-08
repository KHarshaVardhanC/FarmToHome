package com.ftohbackend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "rating")
public class Rating {

	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ratingId;

	@ManyToOne
	@JoinColumn(name = "customerId", nullable = false)
	private Customer customer;

	@ManyToOne
	@JoinColumn(name = "productId", nullable = false)
	private Product product;
	private Integer ratingValue;
	private String feedback;

	@CreationTimestamp
	private LocalDateTime createdAt;

	public Rating() {
	}

	public Rating(Integer ratingId, Customer customer, Product product, Integer ratingValue, String feedback) {
		super();
		this.ratingId = ratingId;
		this.customer = customer;
		this.product = product;
		this.ratingValue = ratingValue;
		this.feedback = feedback;
		this.createdAt = LocalDateTime.now();
	}

	public Integer getRatingId() {
		return ratingId;
	}

	public void setRatingId(Integer ratingId) {
		this.ratingId = ratingId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt() {
		this.createdAt = LocalDateTime.now();
	}

	

	@Override
	public String toString() {
		return "Rating [ratingId=" + ratingId + ", customer=" + customer + ", product=" + product + ", rating=" + ratingValue
				+ ", feedback=" + feedback + ", createdAt=" + createdAt +  "]";
	}

}
