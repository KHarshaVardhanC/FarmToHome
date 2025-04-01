package com.ftohbackend.controller;

import java.util.List;

import com.ftohbackend.dto.SellerDTO;

public interface SellerController {

	public String addSeller(SellerDTO sellerdto);
	
	public SellerDTO getSeller(Integer sellerId);
	
	public List<SellerDTO> getSeller();
	
	public String deleteSeller(Integer sellerId);
	
	public String deleteSeller();

	public String updateSeller(Integer sellerId, SellerDTO sellerdto);

}

