package com.ftohbackend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftohbackend.dto.LoginRequest;
import com.ftohbackend.dto.SellerDTO;
import com.ftohbackend.exception.SellerException;
import com.ftohbackend.model.Mails;
import com.ftohbackend.model.Seller;
import com.ftohbackend.service.MailServiceImpl;
import com.ftohbackend.service.SellerService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/seller")
public class SellerControllerImpl implements SellerController {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	SellerService sellerService;
	
	@Autowired
	MailServiceImpl mailServiceImpl;
	
	// Create a single instance of BCryptPasswordEncoder for consistent encoding
//	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	@PostMapping("")
	public String addSeller(@Valid @RequestBody SellerDTO sellerdto) throws SellerException {
		// TODO Auto-generated method stub
		
		if(!mailServiceImpl.isMailExists(sellerdto.getSellerEmail()))
		{
			Seller seller = modelMapper.map(sellerdto, Seller.class);
			// Don't encode the password here, as the Seller class already does it
			// Just set the password directly
//			seller.setSellerPassword(sellerdto.getSellerPassword()); 
			sellerService.addSeller(seller);
			mailServiceImpl.addMail(new Mails(sellerdto.getSellerEmail()));
			
			return "You Registered Successfully";
		}
		else
		{
			return "Provided Email All ready Exists";
		}
	}

	@Override
	@GetMapping("/{sellerId}")
	public SellerDTO getSeller(@PathVariable Integer sellerId) throws SellerException {
		// TODO Auto-generated method stub

		return modelMapper.map(sellerService.getSeller(sellerId), SellerDTO.class);
	}

	@Override
	@GetMapping("")
	public List<SellerDTO> getSeller() throws SellerException {

		return sellerService.getSeller().stream().map(DTO -> modelMapper.map(DTO, SellerDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	@DeleteMapping("/{sellerId}")
	public String deleteSeller(@PathVariable Integer sellerId) throws SellerException {
		return sellerService.deleteSeller(sellerId);
	}

	@Override
	@DeleteMapping("")
	public String deleteSeller() throws SellerException {
//		return sellerService.deleteSeller();
		return "delete";
	}

	@Override
	@PutMapping("/{sellerId}")
	public String updateSeller(@PathVariable Integer sellerId, @RequestBody SellerDTO sellerdto) throws SellerException {
		Seller seller = modelMapper.map(sellerdto, Seller.class);
		return sellerService.updateSeller(sellerId, seller);
	}

	@Override
	@PutMapping("/{sellerId}/{sellerStatus}")
	public String updateSeller(@PathVariable Integer sellerId, @PathVariable String sellerStatus) throws SellerException {
		return sellerService.updateSeller(sellerId, sellerStatus);
	}

	@Override
	@PostMapping("/login")
	public ResponseEntity<?> loginSeller(@RequestBody LoginRequest loginRequest) throws SellerException {
		try {
			Seller seller = sellerService.authenticateSeller(loginRequest.getEmail(), loginRequest.getPassword());

			// Create and return a response with seller data (excluding password)
			return ResponseEntity.ok(new SellerResponse(seller.getSellerId(), seller.getSellerEmail(),
					seller.getSellerFirstName(), seller.getSellerLastName()
			));
		} catch (SellerException e) {
			// Log the exception for debugging
			System.out.println("Login error: " + e.getMessage());
			return ResponseEntity.status(401).body("Invalid email or password");
		}
	}

	// Inner class for seller response (without sensitive data)
	public static class SellerResponse {
		private Integer sellerId;
		private String sellerEmail;
		private String sellerFirstName;
		private String sellerLastName;
		// ... other non-sensitive fields ...

		public SellerResponse(Integer sellerId, String sellerEmail, String sellerFirstName, String sellerLastName) {
			this.sellerId = sellerId;
			this.sellerEmail = sellerEmail;
			this.sellerFirstName = sellerFirstName;
			this.sellerLastName = sellerLastName;
		}

		// Getters
		public Integer getSellerId() {
			return sellerId;
		}

		public String getSellerEmail() {
			return sellerEmail;
		}

		public String getSellerFirstName() {
			return sellerFirstName;
		}

		public String getSellerLastName() {
			return sellerLastName;
		}
	}
}