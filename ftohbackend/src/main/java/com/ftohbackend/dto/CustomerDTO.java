package com.ftohbackend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CustomerDTO {
	
	
	Integer customerId;

	@NotNull(message = "First name cannot be null")
	String customerFirstName;

	@NotNull(message = "First name cannot be null")
	String customerLastName;

	@NotNull(message = "Email cannot be null")
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
	String customerEmail;

	@NotNull(message = "Password cannot be null")
	@Size(min = 8, message = "Password must be at least 8 characters long")
	String customerPassword;

	String customerLocation;

	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number format")
	String customerPhoneNumber;
	Boolean customerIsActive;
	
	public CustomerDTO() {
		super();
	}
	public CustomerDTO(Integer customerId,  String customerFirstName,
			 String customerLastName,
			 String customerEmail,
			 String customerPassword,
			String customerLocation,
			  String customerPhoneNumber,
			Boolean customerIsActive) {
		super();
		this.customerId = customerId;
		this.customerFirstName = customerFirstName;
		this.customerLastName = customerLastName;
		this.customerEmail = customerEmail;
		this.customerPassword = customerPassword;
		this.customerLocation = customerLocation;
		this.customerPhoneNumber = customerPhoneNumber;
		this.customerIsActive = customerIsActive;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public String getCustomerFirstName() {
		return customerFirstName;
	}
	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}
	public String getCustomerLastName() {
		return customerLastName;
	}
	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}
	public String getCustomerEmail() {
		return customerEmail;
	}
	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
	public String getCustomerPassword() {
		return customerPassword;
	}
	public void setCustomerPassword(String customerPassword) {
		this.customerPassword = customerPassword;
	}
	public String getCustomerLocation() {
		return customerLocation;
	}
	public void setCustomerLocation(String customerLocation) {
		this.customerLocation = customerLocation;
	}
	public String getCustomerPhoneNumber() {
		return customerPhoneNumber;
	}
	public void setCustomerPhoneNumber(String customerPhoneNumber) {
		this.customerPhoneNumber = customerPhoneNumber;
	}
	public Boolean getCustomerIsActive() {
		return customerIsActive;
	}
	public void setCustomerIsActive(Boolean customerIsActive) {
		this.customerIsActive = customerIsActive;
	}
	@Override
	public String toString() {
		return "CustomerDTO [customerId=" + customerId + ", customerFirstName=" + customerFirstName
				+ ", customerLastName=" + customerLastName + ", customerEmail=" + customerEmail + ", customerPassword="
				+ customerPassword + ", customerLocation=" + customerLocation + ", customerPhoneNumber="
				+ customerPhoneNumber + ", customerIsActive=" + customerIsActive + "]";
	}

	
}

