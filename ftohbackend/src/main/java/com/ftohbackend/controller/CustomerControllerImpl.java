package com.ftohbackend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftohbackend.dto.CustomerDTO;
import com.ftohbackend.model.Customer;
import com.ftohbackend.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer")
@Validated
public class CustomerControllerImpl implements CustomerController{
	
	@Autowired 
	private ModelMapper modelmapper;
	
	@Autowired
	private CustomerService customerservice;
	
	@PostMapping("")
    public String addCustomer(@Valid @RequestBody CustomerDTO customerdto) {
		Customer customer=modelmapper.map(customerdto, Customer.class);
		return customerservice.addCustomer(customer);
    }
	
	@GetMapping("/{customerId}")
	public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Integer customerId) {
	    Customer customer = customerservice.getCustomer(customerId);
	    CustomerDTO customerDTO=modelmapper.map(customer,CustomerDTO.class);
	    return ResponseEntity.ok(customerDTO); // Return HTTP 200 with customer data
	}

	@GetMapping("/")
	public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
	    List<Customer> customers= customerservice.getAllCustomers(); 
	    
	    List<CustomerDTO> customerDTOs = customers.stream().map(customer -> modelmapper.map(customer, CustomerDTO.class)).collect(Collectors.toList()); // ✅ Using Collectors.toList() for Java 8+

	    return ResponseEntity.ok(customerDTOs); // Return HTTP 200 with the list of customers
	}

	/*@DeleteMapping("/{customerId}")
	public String deleteCustomer(@PathVariable Integer customerId) {
		return customerservice.deleteCustomer(customerId);
	}
	
	@DeleteMapping("/")
	public String deleteAllCustomer() {
		return customerservice.deleteCustomer();
	}
	*/
	
	@PutMapping("/{customerId}")
	public String updateCustomer(@PathVariable Integer customerId,@Valid @RequestBody CustomerDTO customerdto) {
		Customer customer=modelmapper.map(customerdto, Customer.class);
		return customerservice.updateCustomer(customerId, customer);
	}
}

