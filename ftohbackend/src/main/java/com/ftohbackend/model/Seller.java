package com.ftohbackend.model;

import java.util.Date;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name="Seller")
@Entity
public class Seller {
  
	
	@Id
	@Column(nullable = false)
	Integer sellerId;
	@Column(nullable = false)
//	@Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email format")
	String sellerEmail;
	@Column(nullable  = false)
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
	String sellerCity;
	@Column(nullable = false)
	String sellerState;
	@Column(nullable = false)
	String sellerPincode;
	@Column(nullable = false)
	String sellerPassword;
	String sellerStatus;
	
	public Seller(Integer sellerId, String sellerEmail, String sellerFirstName, String sellerLastName, Date sellerDOB,
			String sellerMobileNumber, String sellerCity, String sellerState, String sellerPincode,
			String sellerPassword, String sellerStatus) {
		super();
		this.sellerId = sellerId;
		this.sellerEmail = sellerEmail;
		this.sellerFirstName = sellerFirstName;
		this.sellerLastName = sellerLastName;
		this.sellerDOB = sellerDOB;
		this.sellerMobileNumber = sellerMobileNumber;
		this.sellerCity = sellerCity;
		this.sellerState = sellerState;
		this.sellerPincode = sellerPincode;
		this.setSellerPassword(sellerPassword); 
		this.sellerStatus = sellerStatus;
	}
	public Seller() {
		super();
	}
	public Integer getSellerId() {
		return sellerId;
	}
	public String getSellerEmail() {
		return sellerEmail;
	}
	public void setSellerEmail(String sellerEmail) {
		this.sellerEmail = sellerEmail;
	}
	public String getSellerFirstName() {
		return sellerFirstName;
	}
	public void setSellerFirstName(String sellerFirstName) {
		this.sellerFirstName = sellerFirstName;
	}
	public String getSellerLastName() {
		return sellerLastName;
	}
	public void setSellerLastName(String sellerLastName) {
		this.sellerLastName = sellerLastName;
	}
	public Date getSellerDOB() {
		return sellerDOB;
	}
	public void setSellerDOB(Date sellerDOB) {
		this.sellerDOB = sellerDOB;
	}
	public String getSellerMobileNumber() {
		return sellerMobileNumber;
	}
	public void setSellerMobileNumber(String sellerMobileNumber) {
		this.sellerMobileNumber = sellerMobileNumber;
	}
	public String getSellerPassword() {
		return sellerPassword;
	}
	public void setSellerPassword(String sellerPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.sellerPassword = passwordEncoder.encode(sellerPassword);
    }
    
    public boolean verifyPassword(String rawPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, this.sellerPassword);
    }
	public String getSellerStatus() {
		return sellerStatus;
	}
	public void setSellerStatus(String sellerStatus) {
		this.sellerStatus = sellerStatus;
	}
	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}
	public String getSellerCity() {
		return sellerCity;
	}
	public void setSellerCity(String sellerCity) {
		this.sellerCity = sellerCity;
	}
	public String getSellerState() {
		return sellerState;
	}
	public void setSellerState(String sellerState) {
		this.sellerState = sellerState;
	}
	public String getSellerPincode() {
		return sellerPincode;
	}
	public void setSellerPincode(String sellerPincode) {
		this.sellerPincode = sellerPincode;
	}
	@Override
	public String toString() {
		return "Seller [sellerId=" + sellerId + ", sellerEmail=" + sellerEmail + ", sellerFirstName=" + sellerFirstName
				+ ", sellerLastName=" + sellerLastName + ", sellerDOB=" + sellerDOB + ", sellerMobileNumber="
				+ sellerMobileNumber + ", sellerCity=" + sellerCity + ", sellerState=" + sellerState
				+ ", sellerPincode=" + sellerPincode + ", sellerPassword=" + sellerPassword + ", sellerStatus="
				+ sellerStatus + "]";
	}
	
	
}
