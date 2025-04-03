 package com.ftohbackend.service;

import java.util.List;

import com.ftohbackend.model.Seller;

//@Service
public interface SellerService {

	public String addSeller(Seller seller);

	public Seller getSeller(Integer sellerId);

	public List<Seller> getSeller();

	public String deleteSeller(Integer sellerId);

	public String deleteSeller();

	public String updateSeller(Integer sellerId, Seller seller);

	public String updateSeller(Integer sellerId, String sellerStatus);

}
