package com.ftohbackend.service;

import java.util.List;

import com.ftohbackend.exception.SellerException;
import com.ftohbackend.model.Seller;

//@Service
public interface SellerService {

	public String addSeller(Seller seller) throws SellerException;

	public Seller getSeller(Integer sellerId) throws SellerException;

	public List<Seller> getSeller() throws SellerException;

	public String deleteSeller(Integer sellerId) throws SellerException;

//	public String deleteSeller() throws SellerException;

	public String updateSeller(Integer sellerId, Seller seller) throws SellerException;

	public String updateSeller(Integer sellerId, String sellerStatus) throws SellerException;

	Seller authenticateSeller(String email, String password) throws SellerException;

}
