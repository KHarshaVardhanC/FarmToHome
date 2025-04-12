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
import lombok.Data;

@Entity
@Table(name = "customer")
@Data
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

//	public Customer() {
//		super();
//	}
//
//	public Customer(Integer customerId, String customerFirstName, String customerLastName, String customerEmail,
//			String customerPassword, String customerPlace, String customerCity, String customerPincode,
//			String customerState, String customerPhoneNumber, Boolean customerIsActive, List<Order> orders,
//			List<Rating> ratings) {
//		super();
//		this.customerId = customerId;
//		this.customerFirstName = customerFirstName;
//		this.customerLastName = customerLastName;
//		this.customerEmail = customerEmail;
//		this.setCustomerPassword(customerPassword);
//		this.customerPlace = customerPlace;
//		this.customerCity = customerCity;
//		this.customerPincode = customerPincode;
//		this.customerState = customerState;
//		this.customerPhoneNumber = customerPhoneNumber;
//		this.customerIsActive = customerIsActive;
//		this.orders = orders;
//		this.ratings = ratings;
//	}
//
//	public List<Order> getOrders() {
//		return orders;
//	}
//
//	public void setOrders(List<Order> orders) {
//		this.orders = orders;
//	}
//
//	public Integer getCustomerId() {
//		return customerId;
//	}
//
//	public void setCustomerId(Integer customerId) {
//		this.customerId = customerId;
//	}
//
//	public String getCustomerFirstName() {
//		return customerFirstName;
//	}
//
//	public void setCustomerFirstName(String customerFirstName) {
//		this.customerFirstName = customerFirstName;
//	}
//
//	public String getCustomerLastName() {
//		return customerLastName;
//	}
//
//	public void setCustomerLastName(String customerLastName) {
//		this.customerLastName = customerLastName;
//	}
//
//	public String getCustomerEmail() {
//		return customerEmail;
//	}
//
//	public void setCustomerEmail(String customerEmail) {
//		this.customerEmail = customerEmail;
//	}
//
//	public String getCustomerPassword() {
//		return customerPassword;
//	}
//
	public void setCustomerPassword(String customerPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		this.customerPassword = passwordEncoder.encode(customerPassword);
	}

	public boolean verifyPassword(String rawPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.matches(rawPassword, this.customerPassword);
	}
//
//
//	public String getCustomerPhoneNumber() {
//		return customerPhoneNumber;
//	}
//
//	public void setCustomerPhoneNumber(String customerPhoneNumber) {
//		this.customerPhoneNumber = customerPhoneNumber;
//	}
//
//	public Boolean getCustomerIsActive() {
//		return customerIsActive;
//	}
//
//	public void setCustomerIsActive(Boolean customerIsActive) {
//		this.customerIsActive = customerIsActive;
//	}
//
//	public String getCustomerPlace() {
//		return customerPlace;
//	}
//
//	public void setCustomerPlace(String customerPlace) {
//		this.customerPlace = customerPlace;
//	}
//
//	public String getCustomerCity() {
//		return customerCity;
//	}
//
//	public void setCustomerCity(String customerCity) {
//		this.customerCity = customerCity;
//	}
//
//	public String getCustomerPincode() {
//		return customerPincode;
//	}
//
//	public void setCustomerPincode(String customerPincode) {
//		this.customerPincode = customerPincode;
//	}
//
//	public String getCustomerState() {
//		return customerState;
//	}
//
//	public void setCustomerState(String customerState) {
//		this.customerState = customerState;
//	}
//
//	public List<Rating> getRatings() {
//		return ratings;
//	}
//
//	public void setRatings(List<Rating> ratings) {
//		this.ratings = ratings;
//	}
//
//	@Override
//	public String toString() {
//		return "Customer [customerId=" + customerId + ", customerFirstName=" + customerFirstName + ", customerLastName="
//				+ customerLastName + ", customerEmail=" + customerEmail + ", customerPassword=" + customerPassword
//				+ ", customerPlace=" + customerPlace + ", customerCity=" + customerCity + ", customerPincode="
//				+ customerPincode + ", customerState=" + customerState + ", customerPhoneNumber=" + customerPhoneNumber
//				+ ", customerIsActive=" + customerIsActive + ", orders=" + orders + ", ratings=" + ratings + "]";
//	}

}
