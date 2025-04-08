package com.ftohbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ftohbackend.exception.CustomerException;
import com.ftohbackend.model.Customer;
import com.ftohbackend.repository.customerRepository;

//service
@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private customerRepository customerrepository;
	
	
	public String addCustomer(Customer customer) throws CustomerException {
		if (customer == null) {
            throw new CustomerException("Customer object cannot be null.");
        }

		customerrepository.save(customer);
		return "customer added successfully";
	}
	
	@Transactional(readOnly = true)
	public Customer getCustomer(Integer customerId) throws CustomerException {
		if (customerId == null) {
            throw new CustomerException("Customer ID cannot be null.");
        }

        return customerrepository.findById(customerId)
                .orElseThrow(() -> new CustomerException("Customer not found with ID: " + customerId));	}
	
	@Transactional(readOnly = true)
	public List<Customer> getAllCustomers() throws CustomerException{
		 List<Customer> customers = customerrepository.findAll();
	        if (customers.isEmpty()) {
	            throw new CustomerException("No customers found.");
	        }
	        return customers;	}
	
	@Transactional
	  public String updateCustomer(Integer customerId, Customer customer) throws CustomerException{
		if (customerId == null || customer == null) {
            throw new CustomerException("Customer ID and customer data cannot be null.");
        }
		Customer existingCustomer = customerrepository.findById(customerId)
                .orElseThrow(() -> new CustomerException("Customer not found with ID: " + customerId));

            // Update only non-null fields
            if (customer.getCustomerFirstName() != null) {
                existingCustomer.setCustomerFirstName(customer.getCustomerFirstName());
            }
            if (customer.getCustomerLastName() != null) {
                existingCustomer.setCustomerLastName(customer.getCustomerLastName());
            }
            if (customer.getCustomerEmail() != null) {
                existingCustomer.setCustomerEmail(customer.getCustomerEmail());
            }
            if (customer.getCustomerPassword() != null) {
                existingCustomer.setCustomerPassword(customer.getCustomerPassword());
            }
            if (customer.getCustomerLocation() != null) {
                existingCustomer.setCustomerLocation(customer.getCustomerLocation());
            }
            if (customer.getCustomerPhoneNumber() != null) {
                existingCustomer.setCustomerPhoneNumber(customer.getCustomerPhoneNumber());
            }
            if (customer.getCustomerIsActive() != null) {
                existingCustomer.setCustomerIsActive(customer.getCustomerIsActive());
            }

            customerrepository.save(existingCustomer);
            return "Customer updated successfully";
        } 
    }



