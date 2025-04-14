

package com.ftohbackend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ftohbackend.controller.CustomerControllerImpl.LoginRequest;
import com.ftohbackend.dto.CustomerDTO;
import com.ftohbackend.exception.CustomerException;

import jakarta.validation.Valid;

public interface CustomerController {
    public ResponseEntity<String> addCustomer(@Valid CustomerDTO customer) throws CustomerException;
	
	public ResponseEntity<CustomerDTO>  getCustomer(Integer customerId) throws CustomerException;
	
	public ResponseEntity<List<CustomerDTO>> getAllCustomers() throws CustomerException;
	

	
	public ResponseEntity<String> updateCustomer(Integer customerId,@Valid CustomerDTO customerDTO) throws CustomerException;

	ResponseEntity<?> loginCustomer(LoginRequest loginRequest) throws Exception;
}
