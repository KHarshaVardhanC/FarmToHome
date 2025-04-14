package com.ftohbackend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
	@NotNull(message = "Place cannot be null")
	String customerPlace;
	@NotNull(message = "City Name cannot be null")
	String customerCity;
	@NotNull(message = "Pincode required")
	String customerPincode;
	@NotNull(message = "State Name is required")
	String customerState;

	@NotNull(message = "Modbile Number required")
	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number format")
	String customerPhoneNumber;
	
	Boolean customerIsActive;

}
