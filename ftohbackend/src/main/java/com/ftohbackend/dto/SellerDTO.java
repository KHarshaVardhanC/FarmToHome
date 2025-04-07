package com.ftohbackend.dto;

import java.util.Date;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

public class SellerDTO {

	Integer sellerId;
	@Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email format")
	String sellerEmail;
	String sellerFirstName;
	String sellerLastName;
	@Past(message = "Age after this date")
	Date sellerDOB;
	@Pattern(regexp = "^[0-9]{10,10}$", message = "Phone number must be 10 digits")
	String sellerMobileNumber;
	String sellerPlace;
	String sellerCity;
	String sellerState;
	@Pattern(regexp = "^[0-9]{6,6}$", message = "Pincode must be 6 digits")
	String sellerPincode;

	String sellerPassword;
	String sellerStatus;

	public SellerDTO(Integer sellerId, String sellerEmail, String sellerFirstName, String sellerLastName, Date sellerDOB, String sellerMobileNumber,
			String sellerPlace, String sellerCity, String sellerState, String sellerPincode, String sellerPassword,
			String sellerStatus) {
		super();
		this.sellerId = sellerId;
		this.sellerEmail = sellerEmail;
		this.sellerFirstName = sellerFirstName;
		this.sellerLastName = sellerLastName;
		this.sellerDOB = sellerDOB;
		this.sellerMobileNumber = sellerMobileNumber;
		this.sellerPlace = sellerPlace;
		this.sellerCity = sellerCity;
		this.sellerState = sellerState;
		this.sellerPincode = sellerPincode;
		this.sellerPassword = sellerPassword;
		this.sellerStatus = sellerStatus;
	}

	public SellerDTO() {
		super();
		// TODO Auto-generated constructor stub
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
		this.sellerPassword = sellerPassword;
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

	public String getSellerPlace() {
		return sellerPlace;
	}

	public void setSellerPlace(String sellerPlace) {
		this.sellerPlace = sellerPlace;
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
		return "SellerDTO [sellerId=" + sellerId + ", sellerEmail=" + sellerEmail + ", sellerFirstName="
				+ sellerFirstName + ", sellerLastName=" + sellerLastName + ", sellerDOB=" + sellerDOB
				+ ", sellerMobileNumber=" + sellerMobileNumber + ", sellerPlace=" + sellerPlace + ", sellerCity="
				+ sellerCity + ", sellerState=" + sellerState + ", sellerPincode=" + sellerPincode + ", sellerPassword="
				+ sellerPassword + ", sellerStatus=" + sellerStatus + "]";
	}

}
