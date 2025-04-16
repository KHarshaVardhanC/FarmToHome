package com.ftohbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ftohbackend.exception.CustomerException;
import com.ftohbackend.model.Customer;
import com.ftohbackend.repository.CustomerRepository;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	public String addCustomer(Customer customer) throws CustomerException {
		if (customer == null) {
			throw new CustomerException("Customer object cannot be null.");
		}

		customerRepository.save(customer);
		return "customer added successfully";
	}

	public Customer getCustomer(Integer customerId) throws CustomerException {
		if (customerId == null) {
			throw new CustomerException("Customer ID cannot be null.");
		}

		return customerRepository.findById(customerId)
				.orElseThrow(() -> new CustomerException("Customer not found with ID: " + customerId));
	}

	public List<Customer> getAllCustomers() throws CustomerException {
		List<Customer> customers = customerRepository.findAll();
		if (customers.isEmpty()) {
			throw new CustomerException("No customers found.");
		}
		return customers;
	}

	public String updateCustomer(Integer customerId, Customer customer) throws CustomerException {
		if (customerId == null || customer == null) {
			throw new CustomerException("Customer ID and customer data cannot be null.");
		}
		Customer existingCustomer = customerRepository.findById(customerId)
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
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

			existingCustomer.setCustomerPassword(passwordEncoder.encode(customer.getCustomerPassword()));
			
			System.out.println( passwordEncoder.encode(customer.getCustomerPassword()));
		}
		if (customer.getCustomerCity() != null) {
			existingCustomer.setCustomerCity(customer.getCustomerCity());
		}
		if (customer.getCustomerPlace() != null) {
			existingCustomer.setCustomerPlace(customer.getCustomerPlace());
		}
		if (customer.getCustomerPincode() != null) {
			existingCustomer.setCustomerPincode(customer.getCustomerPincode());
		}
		if (customer.getCustomerState() != null) {
			existingCustomer.setCustomerState(customer.getCustomerState());
		}
		if (customer.getCustomerPhoneNumber() != null) {
			existingCustomer.setCustomerPhoneNumber(customer.getCustomerPhoneNumber());
		}
		if (customer.getCustomerIsActive() != null) {
			existingCustomer.setCustomerIsActive(customer.getCustomerIsActive());
		}

		System.out.println("Hello");
		customerRepository.save(existingCustomer);
		return "Customer updated successfully";
	}
	
	@Override
	public Customer authenticateCustomer(String email, String password) throws Exception, CustomerException  {
		if (email == null || password == null) {
			throw new CustomerException("Email and password must not be null.");
		}

		Customer customer = customerRepository.findByCustomerEmail(email);
		if (customer == null) {
			throw new CustomerException("Customer not found with email: " + email);
		}

		if (!customer.verifyPassword(password)) {
			throw new CustomerException("Incorrect password.");
		}

		return customer;
	}
	
	
}
