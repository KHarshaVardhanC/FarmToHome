

package com.ftohbackend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ftohbackend.dto.CustomerDTO;

import jakarta.validation.Valid;

public interface CustomerController {
    public ResponseEntity<String> addCustomer(@Valid CustomerDTO customer) throws Exception;
	
	public ResponseEntity<CustomerDTO>  getCustomer(Integer customerId) throws Exception;
	
	public ResponseEntity<List<CustomerDTO>> getAllCustomers() throws Exception;
	

	
	public ResponseEntity<String> updateCustomer(Integer customerId,@Valid CustomerDTO customerDTO) throws Exception;
}
