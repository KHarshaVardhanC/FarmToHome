 package com.ftohbackend.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ftohbackend.dto.CustomerProductDTO;
import com.ftohbackend.dto.ProductCity;
import com.ftohbackend.dto.ProductDTO;
import com.ftohbackend.dto.ProductRequest;
import com.ftohbackend.dto.SellerProductDTO;
import com.ftohbackend.exception.ProductException;
import com.ftohbackend.exception.RatingException;

import jakarta.validation.Valid;

public interface ProductController {

//	String addProduct(@Valid ProductDTO productDto) throws Exception;

	String updateProduct(Integer productId, ProductDTO updatedDetails) throws ProductException, RatingException, Exception;

	String deleteProduct(Integer productId) throws ProductException;

	ProductDTO getProduct(Integer productId) throws ProductException;

	List<CustomerProductDTO> getProductByName(String productName) throws ProductException, Exception;

	List<SellerProductDTO> getProducts(Integer sellerId) throws ProductException;

	ResponseEntity<ProductDTO> addProduct(@Valid ProductRequest productRequest) throws ProductException, IOException, Exception;

	List<ProductDTO> getAllProducts() throws ProductException;

	CustomerProductDTO getCustomerProductByProductId(Integer productId) throws ProductException;

	List<CustomerProductDTO> getProductByNameAndCity(ProductCity productCity) throws Exception;

	
}
