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

//	public Seller( String sellerEmail, String sellerFirstName, String sellerLastName, Date sellerDOB,
//			String sellerMobileNumber, String sellerPlace, String sellerCity, String sellerState, String sellerPincode,
//			String sellerPassword, String sellerStatus) {
//		super();
////		this.sellerId = sellerId;
//		this.sellerEmail = sellerEmail;
//		this.sellerFirstName = sellerFirstName;
//		this.sellerLastName = sellerLastName;
//		this.sellerDOB = sellerDOB;
//		this.sellerMobileNumber = sellerMobileNumber;
//		this.sellerCity = sellerCity;
//		this.sellerState = sellerState;
//		this.sellerPlace = sellerPlace;
//		this.sellerPincode = sellerPincode;
//		this.setSellerPassword(sellerPassword);
//		this.sellerStatus = sellerStatus;
//	}
//	
//
//	public Seller() {
//		super();
//	}
//
//	public Integer getSellerId() {
//		return sellerId;
//	}
//
//	public String getSellerEmail() {
//		return sellerEmail;
//	}
//
//	public void setSellerEmail(String sellerEmail) {
//		this.sellerEmail = sellerEmail;
//	}
//
//	public String getSellerFirstName() {
//		return sellerFirstName;
//	}
//
//	public void setSellerFirstName(String sellerFirstName) {
//		this.sellerFirstName = sellerFirstName;
//	}
//
//	public String getSellerLastName() {
//		return sellerLastName;
//	}
//
//	public void setSellerLastName(String sellerLastName) {
//		this.sellerLastName = sellerLastName;
//	}
//
//	public Date getSellerDOB() {
//		return sellerDOB;
//	}
//
//	public void setSellerDOB(Date sellerDOB) {
//		this.sellerDOB = sellerDOB;
//	}
//
//	public String getSellerMobileNumber() {
//		return sellerMobileNumber;
//	}
//
//	public void setSellerMobileNumber(String sellerMobileNumber) {
//		this.sellerMobileNumber = sellerMobileNumber;
//	}
//
//	public String getSellerPassword() {
//		return sellerPassword;
//	}
//
	public void setSellerPassword(String sellerPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		this.sellerPassword = passwordEncoder.encode(sellerPassword);
	}

	public boolean verifyPassword(String rawPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.matches(rawPassword, this.sellerPassword);
	}
//
//	public String getSellerStatus() {
//		return sellerStatus;
//	}
//
//	public void setSellerStatus(String sellerStatus) {
//		this.sellerStatus = sellerStatus;
//	}
//
//
//	public String getSellerPlace() {
//		return sellerPlace;
//	}
//
//	public void setSellerPlace(String sellerPlace) {
//		this.sellerPlace = sellerPlace;
//	}
//
//	public String getSellerCity() {
//		return sellerCity;
//	}
//
//	public void setSellerCity(String sellerCity) {
//		this.sellerCity = sellerCity;
//	}
//
//	public String getSellerState() {
//		return sellerState;
//	}
//
//	public void setSellerState(String sellerState) {
//		this.sellerState = sellerState;
//	}
//
//	public String getSellerPincode() {
//		return sellerPincode;
//	}
//
//	public void setSellerPincode(String sellerPincode) {
//		this.sellerPincode = sellerPincode;
//	}
//
//	public List<Product> getProducts() {
//		return products;
//	}
//
//	public void setProducts(List<Product> products) {
//		this.products = products;
//	}
//
//	@Override
//	public String toString() {
//		return "Seller [sellerId=" + sellerId + ", sellerEmail=" + sellerEmail + ", sellerFirstName=" + sellerFirstName
//				+ ", sellerLastName=" + sellerLastName + ", sellerDOB=" + sellerDOB + ", sellerMobileNumber="
//				+ sellerMobileNumber + ", sellerPlace=" + sellerPlace + ", sellerCity=" + sellerCity + ", sellerState="
//				+ sellerState + ", sellerPincode=" + sellerPincode + ", sellerPassword=" + sellerPassword
//				+ ", sellerStatus=" + sellerStatus + "]";
//	}

}
