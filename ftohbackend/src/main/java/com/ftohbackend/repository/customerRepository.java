package com.ftohbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ftohbackend.model.Customer;

@Repository
public interface customerRepository extends JpaRepository<Customer, Integer>{

}


//import org.springframework.data.jpa.repository.JpaRepository;
