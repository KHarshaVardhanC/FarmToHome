package com.ftohbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ftohbackend.exception.SellerException;
import com.ftohbackend.model.Seller;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Integer> {
	Seller findBySellerEmail(String sellerEmail) throws SellerException;
	
//	Optional<Seller> findBySellerEmail(String email);

	
}
