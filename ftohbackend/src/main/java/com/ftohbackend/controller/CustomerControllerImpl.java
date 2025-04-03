package com.ftohbackend.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
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

public class CustomerControllerImpl 
{
	
	@Autowired 
	ModelMapper modelmapper;
	
	@Autowired
	private CustomerService customerservice;
	
	@PostMapping("/")
    public String addCustomer(@Valid @RequestBody CustomerDTO customerdto) {
		Customer customer=modelmapper.map(customerdto, Customer.class);
		return customerservice.addCustomer(customer);
    }
	
	@GetMapping("/{customerId}")
	public Customer getCustomer(@PathVariable Integer customerId) {
		return customerservice.getCustomer(customerId);
	}
	
	@GetMapping("/")
	public List<Customer> getCustomer(){
		return customerservice.getCustomer();
	}
	
	@DeleteMapping("/{customerId}")
	public String deleteCustomer(@PathVariable Integer customerId) {
		return customerservice.deleteCustomer(customerId);
	}
	
	@DeleteMapping("/")
	public String deleteCustomer() {
		return customerservice.deleteCustomer();
	}
	
	@PutMapping("/{customerId}")
	public String updateCustomer(@PathVariable Integer customerId, @RequestBody CustomerDTO customerdto) {
		Customer customer=modelmapper.map(customerdto, Customer.class);
		return customerservice.updateCustomer(customerId, customer);
	}
}
