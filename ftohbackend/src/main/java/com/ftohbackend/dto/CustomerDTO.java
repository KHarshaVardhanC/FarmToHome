package com.ftohbackend.dto;

import jakarta.persistence.Column;
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

}
