package com.ftohbackend.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rating")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
	
	@OneToOne
	@JoinColumn(name ="orderId", nullable=false)
	Order order;
	


	private String feedback;

	Double ratingValue;

	@CreationTimestamp
	private LocalDateTime createdAt;

}
