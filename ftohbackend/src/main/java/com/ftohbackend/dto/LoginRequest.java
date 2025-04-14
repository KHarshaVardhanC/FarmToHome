package com.ftohbackend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public  class LoginRequest {
	
	@NotNull(message = "Email cannot be null")
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
	private String email;
	
	@NotNull(message = "Password cannot be null")
//	@Size(min = 4, message = "Password must be at least 8 characters long")
	private String password;

}