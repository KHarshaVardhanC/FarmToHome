package com.ftohbackend.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "Seller")
@Entity
@Data
public class Seller {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer sellerId;
	@Column(nullable = false, unique = true)
//	@Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email format")
	String sellerEmail;
	@Column(nullable = false)
	String sellerFirstName;
	@Column(nullable = false)
	String sellerLastName;
	@Column(nullable = false)
//	@Past(message = "Age after this date")
	Date sellerDOB;
	@Column(nullable = false)
//	@Pattern(regexp = "^[0-9]{10,10}$", message = "Phone number must be 10 digits")
	String sellerMobileNumber;
	@Column(nullable = false)
	String sellerPlace;
	@Column(nullable = false)
	String sellerCity;
	@Column(nullable = false)
	String sellerState;
	@Column(nullable = false)
	String sellerPincode;
	@Column(nullable = false)
	String sellerPassword;
	String sellerStatus;

	@OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Product> products = new ArrayList<>();

	public void setSellerPassword(String sellerPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		this.sellerPassword = passwordEncoder.encode(sellerPassword);
	}

	public boolean verifyPassword(String rawPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.matches(rawPassword, this.sellerPassword);
	}

}
