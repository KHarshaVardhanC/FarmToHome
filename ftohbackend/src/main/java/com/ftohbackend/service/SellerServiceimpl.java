package com.ftohbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftohbackend.model.Seller;
import com.ftohbackend.repository.SellerRepository;

import jakarta.validation.Valid;


@Service
public class SellerServiceimpl implements SellerService{

	@Autowired
	SellerRepository sellerRepository;
	
	@Override
	public String addSeller(@Valid Seller seller) {
		// TODO Auto-generated method stub
		sellerRepository.save(seller);
		return "Seller Added Successfully";
	}

	@Override
	public Seller getSeller(Integer sellerId) {
		
		
		return sellerRepository.findById(sellerId).get();
	}

	@Override
	public String deleteSeller(Integer sellerId) {
		// TODO Auto-generated method stub
		
		sellerRepository.deleteById(sellerId);
		
		return "Seller Deleted Successfully";
	}
	public String deleteSeller() {
		// TODO Auto-generated method stub
		
//		sellerRepository.deleteAll();
		
		return "Seller Deleted Successfully";
	}

	@Override
	public String updateSeller(Integer sellerId, Seller seller) {
		
		Seller sl=sellerRepository.findById(sellerId).get();
		sl.setSellerFirstName(seller.getSellerFirstName());
		sl.setSellerLastName(seller.getSellerLastName());
		sl.setSellerDOB(seller.getSellerDOB());
		sl.setSellerEmail(seller.getSellerEmail());
		sl.setSellerMobileNumber(seller.getSellerMobileNumber());
		sl.setSellerPassword(seller.getSellerPassword());
		sl.setSellerStatus(seller.getSellerStatus());
		sellerRepository.save(sl);
		return "Updated Seller Details Successfully";
		 
	}
	
	@Override
	public String updateSeller(Integer sellerId,String sellerStatus)
	{
		Seller sl=sellerRepository.findById(sellerId).get();
		sl.setSellerStatus(sellerStatus);
		sellerRepository.save(sl);
		if(sellerStatus.equals("Active"))
		{
			return "Seller Account is Activated Successfully";
			 
		}
		else if(sellerStatus.equals("Inactive"))
		{
			return "Seller Account is Activated Successfully";
			
		}
		
		return "Seller Account status Activated";
	}

	@Override
	public List<Seller> getSeller() {
		// TODO Auto-generated method stub
//		System.out.println(sellerRepository.findAll());
		return sellerRepository.findAll();
	}

	
}
