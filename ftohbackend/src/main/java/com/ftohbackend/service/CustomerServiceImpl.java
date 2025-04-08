package com.ftohbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ftohbackend.model.Customer;
import com.ftohbackend.repository.CustomerRepository;

//service
@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	
	public String addCustomer(Customer customer) {
		customerRepository.save(customer);
		return "customer added successfully";
	}
	
//	@Transactional(readOnly = true)
	public Customer getCustomer(Integer customerId) {
		return customerRepository.findById(customerId).get();
	}
	
	public List<Customer> getAllCustomers(){
		return customerRepository.findAll();
	}
	
 	  public String updateCustomer(Integer customerId, Customer customer) {
        if (customerRepository.existsById(customerId)) {
            Customer existingCustomer = customerRepository.findById(customerId).get();

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

            customerRepository.save(existingCustomer);
            return "Customer updated successfully";
        } else {
            return "Customer not found";
        }
    }
}


