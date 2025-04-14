package com.ftohbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftohbackend.exception.SellerException;
import com.ftohbackend.model.Seller;
import com.ftohbackend.repository.SellerRepository;

import jakarta.validation.Valid;

@Service
public class SellerServiceimpl implements SellerService {

	@Autowired
	SellerRepository sellerRepository;

	@Override
	public String addSeller(@Valid Seller seller) throws SellerException {
		// TODO Auto-generated method stub
		if (seller == null) {
			throw new SellerException("Seller object cannot be null.");
		}
		sellerRepository.save(seller);
		return "Seller Added Successfully";
	}

	@Override
	public Seller getSeller(Integer sellerId) throws SellerException {

		if (sellerId == null) {
			throw new SellerException("Seller ID cannot be null.");
		}

		return sellerRepository.findById(sellerId)
				.orElseThrow(() -> new SellerException("Seller not found with ID: " + sellerId));
	}

	@Override
	public String deleteSeller(Integer sellerId) throws SellerException {
		// TODO Auto-generated method stub

		if (sellerId == null) {
			throw new SellerException("Seller ID cannot be null.");
		}

		if (!sellerRepository.existsById(sellerId)) {
			throw new SellerException("Seller not found with ID: " + sellerId);
		}
		sellerRepository.deleteById(sellerId);
		return "Seller Deleted Successfully";
	}
//	public String deleteSeller() throws SellerException{
//		// TODO Auto-generated method stub
//		
////		sellerRepository.deleteAll();
//		
//		return "Seller Deleted Successfully";
//	}

	@Override
	public String updateSeller(Integer sellerId, Seller seller) throws SellerException {
		if (sellerId == null || seller == null) {
			throw new SellerException("Seller ID and updated seller data cannot be null.");
		}

		if (sellerId == null || seller == null) {
			throw new SellerException("Seller ID and updated seller data cannot be null.");
		}

		Seller sl = sellerRepository.findById(sellerId)
				.orElseThrow(() -> new SellerException("Seller not found with ID: " + sellerId));

		sl.setSellerFirstName(seller.getSellerFirstName());
		sl.setSellerLastName(seller.getSellerLastName());
		sl.setSellerEmail(seller.getSellerEmail());
		sl.setSellerMobileNumber(seller.getSellerMobileNumber());
		sl.setSellerPassword(seller.getSellerPassword());
		sl.setSellerStatus(seller.getSellerStatus());
		sellerRepository.save(sl);
		return "Updated Seller Details Successfully";

	}

	@Override
	public String updateSeller(Integer sellerId, String sellerStatus) throws SellerException {
		if (sellerId == null || sellerStatus == null) {
			throw new SellerException("Seller ID and status cannot be null.");
		}
		Seller sl = sellerRepository.findById(sellerId)
				.orElseThrow(() -> new SellerException("Seller not found with ID: " + sellerId));

		sl.setSellerStatus(sellerStatus);
		sellerRepository.save(sl);
		if (sellerStatus.equals("Active")) {
			return "Seller Account is Activated Successfully";

		} else if (sellerStatus.equals("Inactive")) {
			return "Seller Account is Activated Successfully";

		}

		return "Seller Account status Activated";
	}

	@Override
	public List<Seller> getSeller() throws SellerException {
		List<Seller> sellers = sellerRepository.findAll();
		if (sellers.isEmpty()) {
			throw new SellerException("No sellers found.");
		}
		return sellers;
	}

	@Override
	public Seller authenticateSeller(String email, String password) throws SellerException {
		if (email == null || password == null) {
			throw new SellerException("Email and password must not be null.");
		}

		Seller seller = sellerRepository.findBySellerEmail(email);
		if (seller == null) {
			throw new SellerException("Seller not found with email: " + email);
		}

		if (!seller.verifyPassword(password)) {
			throw new SellerException("Incorrect password.");
		}

		return seller;
	}
}
