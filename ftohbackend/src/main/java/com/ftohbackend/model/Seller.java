package com.ftohbackend.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name="Seller")
@Entity
public class Seller {
  
	
	@Id
	@GeneratedValue()
	Integer sellerId;
	@Column(nullable = false)
	String sellerEmail;
	@Column(nullable  = false)
	String sellerFirstName;
	@Column(nullable = false)
	String sellerLastName;
	@Column(nullable = false)
	Date sellerDOB;
	@Column(nullable = false)
	String sellerPassword;
	@Column(nullable = false)
	String sellerStatus;
	
}
