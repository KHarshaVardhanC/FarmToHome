package com.ftohbackend.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ftohbackend.dto.CustomerProductDTO;
import com.ftohbackend.dto.ProductCity;
import com.ftohbackend.dto.ProductDTO;
import com.ftohbackend.dto.ProductRequest;
import com.ftohbackend.exception.ProductException;
import com.ftohbackend.model.Product;

import jakarta.transaction.Transactional;

@Service
@Transactional
public interface ProductService {

	public List<Product> getAllProduct()throws ProductException;

	public Product getProductByTitle(String name) throws ProductException;

	public String updateProduct(Integer productId, Product updatedDetails)throws ProductException, Exception;

	public String deleteProduct(Integer productId) throws ProductException;
	
	public Product getProduct(Integer productId)throws ProductException;
	
	public List<Product> getProductsBySellerId(Integer sellerId)throws ProductException;

	List<Product> getAllProduct(Integer sellerId) throws ProductException;

	List<Product> searchProductsWithSellerDetails(ProductCity productCity) throws Exception;

	public ProductDTO addProduct(ProductRequest productRequest) throws IOException, Exception;

	List<Product> getCategoryProducts(String productCategory) throws ProductException;

	List<Product> searchProductsWithSellerDetails(String productName) throws ProductException;





}
