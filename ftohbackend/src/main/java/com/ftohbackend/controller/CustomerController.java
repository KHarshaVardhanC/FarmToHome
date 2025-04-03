package com.ftohbackend.controller;

import java.util.List;

import com.ftohbackend.dto.CustomerDTO;
import com.ftohbackend.model.Customer;

public interface CustomerController {
    public String addCustomer(CustomerDTO customer);
	
	public Customer getCustomer(Integer customerId);
	
	public List<Customer> getAllCustomers();
	

	
	public String updateCustomer(Integer customerId, CustomerDTO customerDTO);
}
