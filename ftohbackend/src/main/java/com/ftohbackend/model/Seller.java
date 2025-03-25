package com.ftohbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Table(name="Seller")
@Entity
public class Seller {

	
	
	Integer sellerId;
	String sellerFirstName;
	String sellerLastName;
	String sellerPassword;
	String sellerEmail;
	
}
