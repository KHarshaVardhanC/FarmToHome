package com.ftohbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftohbackend.model.allProductModel;
import com.ftohbackend.repository.allProductRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class allProductService {

	@Autowired
	allProductRepository allProductRepo;

	public String addProduct(allProductModel product) {
		allProductRepo.save(product);
		return("Added product");
	}

	public List<allProductModel> getAllProduct(){
		return allProductRepo.findAll();
	}

	public allProductModel getProductByTitle(String name) {
		List<allProductModel> allProducts=allProductRepo.findAll();
		allProductModel pro=new allProductModel();
		for(allProductModel prod: allProducts) {
			if(prod.getProduct_name().equalsIgnoreCase(name)) {
				return prod;
			}

		}
		return null;
	}




}
