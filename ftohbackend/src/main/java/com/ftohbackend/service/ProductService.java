package com.ftohbackend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ftohbackend.model.ProductModel;

import jakarta.transaction.Transactional;

@Service
@Transactional
public interface ProductService {
	public String addProduct(ProductModel product) ;

	public List<ProductModel> getAllProduct();

	public ProductModel getProductByTitle(String name) ;

	public String updateProduct(Integer productId, ProductModel updatedDetails);

	public String deleteProduct(Integer productId) ;
	
	public List<ProductModel> getAllProductBySellerId(Integer sellerid);







}
