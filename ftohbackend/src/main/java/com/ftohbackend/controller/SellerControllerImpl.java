package com.ftohbackend.controller;



import java.util.List;
import java.util.stream.Collectors;

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

import com.ftohbackend.dto.SellerDTO;
import com.ftohbackend.model.Seller;
import com.ftohbackend.service.SellerService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/seller")
public class SellerControllerImpl implements SellerController {

	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	SellerService sellerService;
	
	
	@Override
	@PostMapping("")
	public String addSeller(@Valid @RequestBody SellerDTO sellerdto) {
		// TODO Auto-generated method stub
		Seller seller=modelMapper.map(sellerdto, Seller.class);
		return sellerService.addSeller(seller);
		
	}

	@Override
	@GetMapping("/{sellerId}")
	public SellerDTO getSeller(@PathVariable Integer sellerId) {
		// TODO Auto-generated method stub
		
		
		return modelMapper.map(sellerService.getSeller(sellerId), SellerDTO.class);
	}

	@Override
	@DeleteMapping("/{sellerId}")
	public String deleteSeller(@PathVariable Integer sellerId) {
		// TODO Auto-generated method stub
		return sellerService.deleteSeller(sellerId);
	}

	@Override
	@DeleteMapping("")
	public String deleteSeller() {
		// TODO Auto-generated method stub
		return sellerService.deleteSeller();
	}
	
	@Override
	@PutMapping("/{sellerId}")
	public String updateSeller(@PathVariable Integer sellerId, @RequestBody SellerDTO sellerdto) {
		// TODO Auto-generated method stub
		Seller seller=modelMapper.map(sellerdto, Seller.class);
		return sellerService.updateSeller(sellerId, seller);
	}

	@Override
	@GetMapping("")
	public List<SellerDTO> getSeller() {
		// TODO Auto-generated method stub
		
		return  sellerService.getSeller().stream().map(DTO -> modelMapper.map(DTO, SellerDTO.class))
                .collect(Collectors.toList());
	}

	
}
