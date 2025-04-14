package com.ftohbackend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ftohbackend.controller.SellerControllerImpl.LoginRequest;
import com.ftohbackend.dto.SellerDTO;
import com.ftohbackend.exception.SellerException;

public interface SellerController {

	public String addSeller(SellerDTO sellerdto) throws SellerException;
	
	public SellerDTO getSeller(Integer sellerId) throws SellerException;
	
	public List<SellerDTO> getSeller() throws SellerException;
	
	public String deleteSeller(Integer sellerId) throws SellerException;
	
	public String deleteSeller() throws SellerException;

	public String updateSeller(Integer sellerId, SellerDTO sellerdto) throws SellerException;

	public String updateSeller(Integer sellerId, String sellerStatus) throws SellerException;

	public ResponseEntity<?> loginSeller(LoginRequest loginRequest) throws SellerException;

}

