package com.ftohbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ftohbackend.model.Seller;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Integer> {
	Seller findBySellerEmail(String sellerEmail);
	
//	Optional<Seller> findBySellerEmail(String email);

	
}
