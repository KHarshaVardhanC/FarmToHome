package com.ftohbackend.service;

import java.util.List;

import com.ftohbackend.exception.CustomerException;
import com.ftohbackend.model.Customer;


public interface CustomerService {

	public String addCustomer(Customer customer) throws CustomerException;

	
	public Customer getCustomer(Integer customerId)throws CustomerException;
	
	public List<Customer> getAllCustomers()throws CustomerException;
	
	
	public String updateCustomer(Integer customerId, Customer customer)throws CustomerException;


	Customer authenticateCustomer(String email, String password) throws CustomerException, Exception;
	
	public Customer getCustomerById(Integer customerId) throws Exception;
}
