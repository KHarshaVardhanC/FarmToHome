package com.ftohbackend.dto;

import java.sql.Date;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
