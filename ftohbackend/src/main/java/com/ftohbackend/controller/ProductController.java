package com.ftohbackend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ftohbackend.dto.ProductDTO;
import com.ftohbackend.dto.ProductWithSellerDetailsDTO;
import com.ftohbackend.dto.SellerProductDTO;

import jakarta.validation.Valid;

public interface ProductController {

	String addProduct(@Valid ProductDTO productDto);

	String updateProduct(Integer productId, ProductDTO updatedDetails);

	String deleteProduct(Integer productId);

	ProductDTO getProduct(Integer productId);

	ResponseEntity<List<ProductWithSellerDetailsDTO>> getProductByName(String productName);

	List<SellerProductDTO> getProducts(Integer sellerId);

}
