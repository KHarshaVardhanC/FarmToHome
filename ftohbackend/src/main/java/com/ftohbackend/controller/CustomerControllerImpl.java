package com.ftohbackend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import com.ftohbackend.dto.LoginRequest;
import com.ftohbackend.exception.CustomerException;
import com.ftohbackend.model.Customer;
import com.ftohbackend.model.Mails;
import com.ftohbackend.service.CustomerService;
import com.ftohbackend.service.MailServiceImpl;

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

//	@PostMapping("")
//	public ResponseEntity<String> addCustomer(@Valid @RequestBody CustomerDTO customerdto) throws CustomerException {
//		String result = "";
//
//		if (!mailServiceImpl.isMailExists(customerdto.getCustomerEmail())) {
//			Customer customer = modelmapper.map(customerdto, Customer.class);
//			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//			customer.setCustomerPassword(passwordEncoder.encode( customerdto.getCustomerPassword()));
//			customerService.addCustomer(customer);
//			mailServiceImpl.addMail(new Mails(customerdto.getCustomerEmail()));
//
//			result = "You Registered Successfully";
//		} else {
//			result = "Provided Email All ready Exists";
//		}
//
//		return ResponseEntity.status(HttpStatus.CREATED).body(result);
//	}

	
	
	@PostMapping("")
	public ResponseEntity<String> addCustomer(@Valid @RequestBody CustomerDTO customerdto) throws CustomerException {
	    if (mailServiceImpl.isMailExists(customerdto.getCustomerEmail())) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body("Provided Email Already Exists");
	    }

	    Customer customer = modelmapper.map(customerdto, Customer.class);
	    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    customer.setCustomerPassword(passwordEncoder.encode(customerdto.getCustomerPassword()));
	    customerService.addCustomer(customer);
	    mailServiceImpl.addMail(new Mails(customerdto.getCustomerEmail()));

	    return ResponseEntity.status(HttpStatus.CREATED).body("You Registered Successfully");
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
