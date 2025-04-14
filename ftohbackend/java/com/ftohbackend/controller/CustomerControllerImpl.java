package com.ftohbackend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftohbackend.dto.CustomerDTO;
import com.ftohbackend.dto.PasswordResetRequest;
import com.ftohbackend.exception.CustomerException;
import com.ftohbackend.model.Customer;
import com.ftohbackend.model.Mails;
import com.ftohbackend.repository.CustomerRepository;
import com.ftohbackend.service.CustomerService;
import com.ftohbackend.service.MailServiceImpl;
import com.ftohbackend.service.PasswordResetService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/customer")
@Validated
public class CustomerControllerImpl implements CustomerController {

	@Autowired
	private ModelMapper modelmapper;

	@Autowired
	private CustomerService customerService;

	@Autowired
	MailServiceImpl mailServiceImpl;
	
	
	
	@Autowired
	private PasswordResetService passwordResetService;

	@Autowired
	private CustomerRepository customerRepository;

	@PostMapping("/resetPassword")
	public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request) {
	    // Assuming OTP is verified here, now proceed with updating password

	    Customer customer = customerRepository.findByCustomerEmail(request.getEmail());

	    if (customer != null) {
	        passwordResetService.updatePassword(customer, request.getNewPassword());
	        customerRepository.save(customer);  // Don't forget to save after updating the password
	        return ResponseEntity.ok("Password reset successfully.");
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found.");
	    }
	}

	
	

	@PostMapping("")
	public ResponseEntity<String> addCustomer(@Valid @RequestBody CustomerDTO customerdto) throws CustomerException {
		String result = "";

		if (!mailServiceImpl.isMailExists(customerdto.getCustomerEmail())) {
			Customer customer = modelmapper.map(customerdto, Customer.class);
			customerService.addCustomer(customer);
			mailServiceImpl.addMail(new Mails(customerdto.getCustomerEmail()));

			result = "You Registered Successfully";
		} else {
			result = "Provided Email All ready Exists";
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(result);
	}

	@GetMapping("/{customerId}")
	public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Integer customerId) throws CustomerException {
		Customer customer = customerService.getCustomer(customerId);
		CustomerDTO customerDTO = modelmapper.map(customer, CustomerDTO.class);
		return ResponseEntity.ok(customerDTO); // Return HTTP 200 with customer data
	}

	@GetMapping("/")
	public ResponseEntity<List<CustomerDTO>> getAllCustomers() throws CustomerException {
		List<Customer> customers = customerService.getAllCustomers();

		List<CustomerDTO> customerDTOs = customers.stream()
				.map(customer -> modelmapper.map(customer, CustomerDTO.class)).collect(Collectors.toList()); // ✅ Using
																												// Collectors.toList()
																												// for
																												// Java
																												// 8+

		return ResponseEntity.ok(customerDTOs); // Return HTTP 200 with the list of customers
	}

	@Override
	@PostMapping("/login")
	public ResponseEntity<?> loginCustomer(@RequestBody LoginRequest loginRequest) throws CustomerException, Exception {
		Customer customer = customerService.authenticateCustomer(loginRequest.getEmail(), loginRequest.getPassword());

		if (customer != null) {
			// Create and return a response with seller data (excluding password)
			return ResponseEntity.ok(new CustomerResponse(customer.getCustomerId(), customer.getCustomerEmail()));
		} else {
			return ResponseEntity.status(401).body("Invalid email or password");
		}
	}

	public static class LoginRequest {
		private String email;
		private String password;

		// Getters and setters
		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}

	// Inner class for seller response (without sensitive data)
	public static class CustomerResponse {
		private Integer customerId;
		private String customerEmail;

		// ... other non-sensitive fields ...

		public CustomerResponse(Integer customerId, String customerEmail) {
			this.customerId = customerId;
			this.customerEmail = customerEmail;
		}

		// Getters
		public Integer getCustomerId() {
			return customerId;
		}

		public String getCustomerEmail() {
			return customerEmail;
		}

	}

	@PutMapping("/{customerId}")
	public ResponseEntity<String> updateCustomer(@PathVariable Integer customerId, @RequestBody CustomerDTO customerDTO)
			throws CustomerException {
		// Convert DTO to Entity using ModelMapper
		Customer customer = modelmapper.map(customerDTO, Customer.class);

		// Pass the entity to the service
		String response = customerService.updateCustomer(customerId, customer);

		return ResponseEntity.ok(response);
	}
}
