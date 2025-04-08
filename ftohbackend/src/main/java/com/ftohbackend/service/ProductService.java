package com.ftohbackend.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ftohbackend.dto.CustomerProductDTO;
import com.ftohbackend.dto.ProductDTO;
import com.ftohbackend.dto.ProductRequest;
import com.ftohbackend.exception.ProductException;
import com.ftohbackend.model.Product;

import jakarta.transaction.Transactional;

@Service
@Transactional
public interface ProductService {
//	public String addProduct(Product product) throws ProductException ;

	public List<Product> getAllProduct()throws ProductException;

	public Product getProductByTitle(String name) throws ProductException;

	public String updateProduct(Integer productId, Product updatedDetails)throws ProductException;

	public String deleteProduct(Integer productId) throws ProductException;
	
	public Product getProduct(Integer productId)throws ProductException;
	
	public List<Product> getProductsBySellerId(Integer sellerId)throws ProductException;
	
//	public Product createProduct(Integer sellerId, Product product)throws ProductException;

//	public	List<ProductWithSellerDetailsDTO> searchProductsWithSellerDetails(String productName)throws ProductException;

	List<Product> getAllProduct(Integer sellerId) throws ProductException;

	List<CustomerProductDTO> searchProductsWithSellerDetails(String productName) throws Exception;

	public ProductDTO addProduct(ProductRequest productRequest) throws IOException, Exception;



}
