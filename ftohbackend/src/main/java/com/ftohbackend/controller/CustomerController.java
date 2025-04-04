

package com.ftohbackend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ftohbackend.dto.CustomerDTO;

import jakarta.validation.Valid;

public interface CustomerController {
    public String addCustomer(@Valid CustomerDTO customer);
	
	public ResponseEntity<CustomerDTO>  getCustomer(Integer customerId);
	
	public ResponseEntity<List<CustomerDTO>> getAllCustomers();
	

	
	public String updateCustomer(Integer customerId,@Valid CustomerDTO customerDTO);
}
