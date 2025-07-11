package com.ftohbackend.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

@Table(name = "Orders1")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer orderId;

	@ManyToOne
	@JoinColumn(name = "productId", nullable = false)
	public Product product;

	@ManyToOne
	@JoinColumn(name = "customerId", nullable = false)
	public Customer customer;

	@Column(nullable = false)
	public String orderStatus;

	@Column(nullable = false)
	public Double orderQuantity;
	
	public Double orderPrice;
	
	String orderReportImageUrl;
	String reportReason;
	
	
	@OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	Rating rating;

	
	@Column(name = "razorpay_order_id")
	private String razorpayOrderId;

	@Column(name = "payment_status")
	private String paymentStatus;

	@Column(name = "receipt_id")
	private String receiptId;       // For tracking
	
	@Column(name = "razorpay_payment_id")
	private String razorpayPaymentId;

	@Column(name = "razorpay_signature")
	private String razorpaySignature;

}
