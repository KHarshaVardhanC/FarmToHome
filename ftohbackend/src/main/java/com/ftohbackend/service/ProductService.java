package com.ftohbackend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ftohbackend.dto.ProductWithSellerDetailsDTO;
import com.ftohbackend.model.Product;

import jakarta.transaction.Transactional;

@Service
@Transactional
public interface ProductService {
	public String addProduct(Product product) ;

	public List<Product> getAllProduct();

	public Product getProductByTitle(String name) ;

	public String updateProduct(Integer productId, Product updatedDetails);

	public String deleteProduct(Integer productId) ;
	
	public Product getProduct(Integer productId);
	

	
	public List<Product> getProductsBySellerId(Integer sellerId);
	
	public Product createProduct(Integer sellerId, Product product);

	public	List<ProductWithSellerDetailsDTO> searchProductsWithSellerDetails(String productName);

	List<Product> getAllProduct(Integer sellerId); 




}
