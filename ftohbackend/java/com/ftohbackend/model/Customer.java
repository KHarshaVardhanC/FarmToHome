package com.ftohbackend.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer customerId;

	private String customerFirstName;
	private String customerLastName;
	private String customerEmail;
	private String customerPassword;
	String customerPlace;
	String customerCity;
	String customerPincode;
	String customerState;
	private String customerPhoneNumber;
	private Boolean customerIsActive;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Order> orders = new ArrayList<>();

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Rating> ratings = new ArrayList<>();


	public void setCustomerPassword(String customerPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		this.customerPassword = passwordEncoder.encode(customerPassword);
	}

	public boolean verifyPassword(String rawPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.matches(rawPassword, this.customerPassword);
	}


	
}
