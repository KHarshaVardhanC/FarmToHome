package com.ftohbackend.service;

import java.util.List;

import com.ftohbackend.model.Customer;


public interface CustomerService {
	public String addCustomer(Customer customer);
	
	public Customer getCustomer(Integer customerId);
	
	public List<Customer> getCustomer();
	
	public String deleteCustomer(Integer customerId);
	
	public String deleteCustomer();
	
	public String updateCustomer(Integer customerId, Customer customer);
}
