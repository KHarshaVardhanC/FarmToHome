 package com.ftohbackend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ftohbackend.dto.CustomerProductDTO;
import com.ftohbackend.dto.ProductDTO;
import com.ftohbackend.dto.ProductRequest;
import com.ftohbackend.dto.SellerProductDTO;

import jakarta.validation.Valid;

public interface ProductController {

//	String addProduct(@Valid ProductDTO productDto) throws Exception;

	String updateProduct(Integer productId, ProductDTO updatedDetails) throws Exception;

	String deleteProduct(Integer productId) throws Exception;

	ProductDTO getProduct(Integer productId) throws Exception;

	List<CustomerProductDTO> getProductByName(String productName) throws Exception;

	List<SellerProductDTO> getProducts(Integer sellerId) throws Exception;

	ResponseEntity<ProductDTO> addProduct(@Valid ProductRequest productRequest) throws Exception;

	List<ProductDTO> getAllProducts() throws Exception;

	
}
