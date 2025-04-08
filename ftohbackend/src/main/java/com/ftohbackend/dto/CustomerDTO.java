package com.ftohbackend.dto;

import jakarta.persistence.Column;
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
	@Size(min = 4, message = "Password must be at least 8 characters long")
	String customerPassword;
	@Column(nullable = false)
	String customerPlace;
	@Column(nullable = false)
	String customerCity;
	@Column(nullable = false)
	String customerPincode;
	@Column(nullable = false)
	String customerState;

	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number format")
	String customerPhoneNumber;
	Boolean customerIsActive;
	
	public CustomerDTO() {
		super();
	}
	
	public CustomerDTO(Integer customerId, @NotNull(message = "First name cannot be null") String customerFirstName,
			@NotNull(message = "First name cannot be null") String customerLastName,
			@NotNull(message = "Email cannot be null") @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format") String customerEmail,
			@NotNull(message = "Password cannot be null") @Size(min = 8, message = "Password must be at least 8 characters long") String customerPassword,
			String customerPlace, String customerCity, String customerPincode, String customerState,
			@Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number format") String customerPhoneNumber,
			Boolean customerIsActive) {
		super();
		this.customerId = customerId;
		this.customerFirstName = customerFirstName;
		this.customerLastName = customerLastName;
		this.customerEmail = customerEmail;
		this.customerPassword = customerPassword;
		this.customerPlace = customerPlace;
		this.customerCity = customerCity;
		this.customerPincode = customerPincode;
		this.customerState = customerState;
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
	
	public String getCustomerPlace() {
		return customerPlace;
	}

	public void setCustomerPlace(String customerPlace) {
		this.customerPlace = customerPlace;
	}

	public String getCustomerCity() {
		return customerCity;
	}

	public void setCustomerCity(String customerCity) {
		this.customerCity = customerCity;
	}

	public String getCustomerPincode() {
		return customerPincode;
	}

	public void setCustomerPincode(String customerPincode) {
		this.customerPincode = customerPincode;
	}

	public String getCustomerState() {
		return customerState;
	}

	public void setCustomerState(String customerState) {
		this.customerState = customerState;
	}

	@Override
	public String toString() {
		return "CustomerDTO [customerId=" + customerId + ", customerFirstName=" + customerFirstName
				+ ", customerLastName=" + customerLastName + ", customerEmail=" + customerEmail + ", customerPassword="
				+ customerPassword + ", customerPlace=" + customerPlace + ", customerCity=" + customerCity
				+ ", customerPincode=" + customerPincode + ", customerState=" + customerState + ", customerPhoneNumber="
				+ customerPhoneNumber + ", customerIsActive=" + customerIsActive + "]";
	}

	
	
}

