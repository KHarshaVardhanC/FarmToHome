package com.ftohbackend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ftohbackend.controller.SellerControllerImpl.LoginRequest;
import com.ftohbackend.dto.SellerDTO;

public interface SellerController {

	public String addSeller(SellerDTO sellerdto) throws Exception;
	
	public SellerDTO getSeller(Integer sellerId) throws Exception;
	
	public List<SellerDTO> getSeller() throws Exception;
	
	public String deleteSeller(Integer sellerId) throws Exception;
	
	public String deleteSeller() throws Exception;

	public String updateSeller(Integer sellerId, SellerDTO sellerdto) throws Exception;

	public String updateSeller(Integer sellerId, String sellerStatus) throws Exception;

	public ResponseEntity<?> loginSeller(LoginRequest loginRequest) throws Exception;

}

