package com.ftohbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ftohbackend.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>{
    
	Customer findByCustomerEmail(String email);

}


//import org.springframework.data.jpa.repository.JpaRepository;
