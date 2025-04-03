package com.ftohbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Customer {
	
	@Id
	private Integer customer_id;
	
	@Column(nullable=false)
	private String firstname;
	
	@Column(nullable=false)
	private String lastname;
	
	 @Column(unique = true, nullable = false)
	 @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",message = "Invalid email format")
	private String email;
	 
	 @Column(nullable = false)
	 @Size(min = 8, message = "Password must be at least 8 characters long")
	 private String password;
	
	private String location;
	
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Invalid phone number format"
        )
	private String phonenumber;
    private Boolean isActive;
	public Customer(Integer customer_id, String firstname, String lastname, String email, String password,
			String location, @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number format") String phonenumber, Boolean isActive) {
		super();
		this.customer_id = customer_id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
		this.location = location;
		this.phonenumber=phonenumber;
		this.isActive=isActive;
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
	public Boolean getPhonenumber() {
		return isActive;
	}
	public void setPhonenumber(Boolean isActive) {
		this.isActive = isActive;
	}	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	@Override
	public String toString() {
		return "Customer [customer_id=" + customer_id + ", firstname=" + firstname + ", lastname=" + lastname
				+ ", email=" + email + ", password=" + password + ", location=" + location + "]";
	}

	
	
}
