package com.ftohbackend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerDTO {

	Integer sellerId;
	@NotNull(message = "Email is required")
	@Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email format")
	String sellerEmail;
	@NotNull(message = "FirstName is required")
	String sellerFirstName;
	@NotNull(message = "Lastname is required")
	String sellerLastName;
	@NotNull(message = "Mobile Number is required")
	@Pattern(regexp = "^[0-9]{10,10}$", message = "Phone number must be 10 digits")
	String sellerMobileNumber;
	@NotNull(message = "Seller place is required")
	String sellerPlace;
	@NotNull(message = "City Name is required")
	String sellerCity;
	@NotNull(message = "State Name is required")
	String sellerState;
	
	@NotNull(message = "Pincode is required")
	@Pattern(regexp = "^[0-9]{6,6}$", message = "Pincode must be 6 digits")
	String sellerPincode;

	@NotNull(message = "Password is required")
	String sellerPassword;
	String sellerStatus;
}
