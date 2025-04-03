package com.ftohbackend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ftohbackend.controller.SellerControllerImpl.LoginRequest;
import com.ftohbackend.dto.SellerDTO;

public interface SellerController {

	public String addSeller(SellerDTO sellerdto);
	
	public SellerDTO getSeller(Integer sellerId);
	
	public List<SellerDTO> getSeller();
	
	public String deleteSeller(Integer sellerId);
	
	public String deleteSeller();

	public String updateSeller(Integer sellerId, SellerDTO sellerdto);

	public String updateSeller(Integer sellerId, String sellerStatus);

	public ResponseEntity<?> loginSeller(LoginRequest loginRequest);

}

