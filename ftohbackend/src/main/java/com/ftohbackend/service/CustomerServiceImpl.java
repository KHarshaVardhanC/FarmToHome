package com.ftohbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ftohbackend.model.Customer;
import com.ftohbackend.repository.customerRepository;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private customerRepository customerrepository;
	
	
	public String addCustomer(Customer customer) {
		customerrepository.save(customer);
		return "customer added successfully";
	}
	
	@Transactional(readOnly = true)
	public Customer getCustomer(Integer customerId) {
		return customerrepository.findById(customerId).get();
	}
	
	@Transactional(readOnly = true)
	public List<Customer> getAllCustomers(){
		return customerrepository.findAll();
	}
	
	@Transactional
	public String updateCustomer(Integer customerId, Customer customer) {
//		if (customerrepository.existsById(customerId)) {
//	        Customer existingCustomer = customerrepository.findById(customerId).get();
//
//	        // Update only non-null fields
//	        if (customer.Firstname() != null) {
//	            existingCustomer.setFirstname(customer.getFirstname());
//	        }
//	        if (customer.getLastname() != null) {
//	            existingCustomer.setLastname(customer.getLastname());
//	        }
//	        if (customer.getEmail() != null) {
//	            existingCustomer.setEmail(customer.getEmail());
//	        }
//	        if (customer.getPassword() != null) {
//	            existingCustomer.setPassword(customer.getPassword());
//	        }
//	        if (customer.getLocation() != null) {
//	            existingCustomer.setLocation(customer.getLocation());
//	        }	
//	        if (customer.getPhonenumber() != null) {
//	            existingCustomer.setPhonenumber(customer.getPhonenumber());
//	        }
//	        if (customer.getIsActive() != null) {
//	            existingCustomer.setIsActive(customer.getIsActive());
//	        }
//
//	        customerrepository.save(existingCustomer);
//	        return "Customer updated successfully";
//	    } else {
//	    }
	return "Customer not found";
	}
}


