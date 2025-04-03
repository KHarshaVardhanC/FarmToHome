package com.ftohbackend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CustomerDTO {
	Integer customer_id;
	
	@NotNull(message = "First name cannot be null")
	String firstname;
	
	@NotNull(message = "First name cannot be null")
	String lastname;
	
	 @NotNull(message = "Email cannot be null")
	    @Pattern(
	        regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
	        message = "Invalid email format"
	    )
	String email;
	 
	 @NotNull(message = "Password cannot be null")
	    @Size(min = 8, message = "Password must be at least 8 characters long")
	String password;
	 
	String location;
	
	@Pattern(
	        regexp = "^[6-9]\\d{9}$",
	        message = "Invalid phone number format"
	    )
	String phonenumber;
	Boolean isActive;
	public CustomerDTO(Integer customer_id, String firstname, String lastname, String email, String password,
			String location, String phonenumber, Boolean isActive) {
		super();
		this.customer_id = customer_id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
		this.location = location;
		this.phonenumber = phonenumber;
		this.isActive = isActive;
	}
	public Integer getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(Integer customer_id) {
		this.customer_id = customer_id;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	@Override
	public String toString() {
		return "CustomerDTO [customer_id=" + customer_id + ", firstname=" + firstname + ", lastname=" + lastname
				+ ", email=" + email + ", password=" + password + ", location=" + location + ", phonenumber="
				+ phonenumber + ", isActive=" + isActive + "]";
	}
	
}


//add pro to cart
//place an order
//view orders
//view cart
//add to cart