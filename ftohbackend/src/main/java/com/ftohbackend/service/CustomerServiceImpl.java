package com.ftohbackend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ftohbackend.model.Customer;
import com.ftohbackend.repository.customerRepository;

@Service
public class CustomerServiceImpl {
	customerRepository customerrepository;
	public String addCustomer(Customer customer) {
		customerrepository.save(customer);
		return "customer added successfully";
	}
	
	public Customer getCustomer(Integer customerId) {
		return customerrepository.findById(customerId).get();
	}
	
	public List<Customer> getCustomer(){
		return customerrepository.findAll();
	}
	
	public String deleteCustomer(Integer customerId) {
		customerrepository.deleteById(customerId);
		return "customer deleted succesfully";
	}
	
	public String deleteCustomer() {
		customerrepository.deleteAll();
		return "all csutomers deleted successfully";
	}
	
	public String updateCustomer(Integer customerId, Customer customer) {
		if (customerrepository.existsById(customerId)) {
	        Customer existingCustomer = customerrepository.findById(customerId).get();

	        // Update only non-null fields
	        if (customer.getFirstname() != null) {
	            existingCustomer.setFirstname(customer.getFirstname());
	        }
	        if (customer.getLastname() != null) {
	            existingCustomer.setLastname(customer.getLastname());
	        }
	        if (customer.getEmail() != null) {
	            existingCustomer.setEmail(customer.getEmail());
	        }
	        if (customer.getPassword() != null) {
	            existingCustomer.setPassword(customer.getPassword());
	        }
	        if (customer.getLocation() != null) {
	            existingCustomer.setLocation(customer.getLocation());
	        }	
	        if (customer.getPhonenumber() != null) {
	            existingCustomer.setPhonenumber(customer.getPhonenumber());
	        }
	        if (customer.getIsActive() != null) {
	            existingCustomer.setIsActive(customer.getIsActive());
	        }

	        customerrepository.save(existingCustomer);
	        return "Customer updated successfully";
	    } else {
	        return "Customer not found";
	    }
	}
}
