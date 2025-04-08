package com.ftohbackend.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftohbackend.dto.ProductWithSellerDetailsDTO;
import com.ftohbackend.exception.ProductException;
import com.ftohbackend.model.Product;
import com.ftohbackend.model.Seller;
import com.ftohbackend.repository.ProductRepository;
import com.ftohbackend.repository.SellerRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	SellerRepository sellerRepository;
	
	@Autowired
	ModelMapper modelMapper;

	@Override
	public String addProduct(Product product) throws ProductException {
		if (product == null) {
			throw new ProductException("Product object is null.");
		}
		productRepository.save(product);
		return "Product added successfully";
	}

	@Override
	public List<Product> getAllProduct() throws ProductException {
		List<Product> products = productRepository.findAll();
		if (products.isEmpty()) {
			throw new ProductException("No products found.");
		}
		return products;
	}
	
	@Override
	public List<Product> getAllProduct(Integer sellerId) throws ProductException {
		if (sellerId == null) {
			throw new ProductException("Seller ID cannot be null.");
		}
		List<Product> products = productRepository.findBySellerSellerId(sellerId);
		if (products.isEmpty()) {
			throw new ProductException("No products found for seller ID: " + sellerId);
		}
		return products;
	}

//	public List<Product> getAllProductBySellerId(Integer sellerid) {
//		return productRepository.findAll().stream()
//				.filter(product -> product.getSeller().getSellerId().equals(sellerid)).toList();
//	}

	@Override
	public Product getProductByTitle(String name) throws ProductException {
		if (name == null || name.trim().isEmpty()) {
			throw new ProductException("Product name cannot be null or empty.");
		}
		List<Product> allProducts = productRepository.findAll();
		Product product = new Product();
		for (Product prod : allProducts) {
			if (prod.getProductName().equalsIgnoreCase(name)) {
				return prod;
			}

		}
		throw new ProductException("Product with name '" + name + "' not found.");	}

	@Override
	public String updateProduct(Integer productId, Product updatedDetails) throws ProductException {
		if (productId == null || updatedDetails == null) {
			throw new ProductException("Product ID and updated details cannot be null.");
		}
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ProductException("Product not found with ID: " + productId));

		if (updatedDetails.getProductPrice() != null) {
			product.setProductPrice(updatedDetails.getProductPrice());
		}
		if (updatedDetails.getProductName() != null) {
			product.setProductName(updatedDetails.getProductName());
		}
		if (updatedDetails.getProductQuantity() != null) {
			product.setProductQuantity(updatedDetails.getProductQuantity());
		}

		productRepository.save(product);
		return "Product updated successfully";
	}

	@Override
	public String deleteProduct(Integer productId) throws ProductException {
		if (productId == null) {
			throw new ProductException("Product ID cannot be null.");
		}
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ProductException("Product not found with ID: " + productId));
		productRepository.delete(product);
		return "Product deleted successfully";
	}

	@Override
	public List<Product> getProductsBySellerId(Integer sellerId)throws ProductException  {
		if (sellerId == null) {
			throw new ProductException("Seller ID cannot be null.");
		}
		List<Product> products = productRepository.findBySellerSellerId(sellerId);
		if (products.isEmpty()) {
			throw new ProductException("No products found for seller ID: " + sellerId);
		}
		return products;	}

//	@Override
//	public Product createProduct(Integer sellerId, Product product)throws ProductException  {
//		Seller seller = sellerRepository.findById(sellerId).orElseThrow(() -> new RuntimeException("Seller not found"));
//		product.setSeller(seller);
//		return productRepository.save(product);
//	}

//	@Override
//	public List<ProductWithSellerDetailsDTO> searchProductsWithSellerDetails(String productName) {
//		List<Product> products = productRepository.findProductsByNameWithSeller(productName);
//		return products.stream().map(ProductWithSellerDetailsDTO::new).collect(Collectors.toList());
//	}

//	@Override
//	public List<ProductWithSellerDetailsDTO> searchProductsWithSellerDetails(String productName)throws ProductException  {
//		// Input validation
//		if (productName == null || productName.trim().isEmpty()) {
//			throw new IllegalArgumentException("Product name cannot be null or empty");
//		}
//
//		// Search products with seller details
//		List<Product> products = productRepository.findProductsByNameWithSeller(productName.trim());
//
//		// Handle null case
//		if (products == null) {
//			return Collections.emptyList();
//		}
//
//		// Convert to DTOs
//		return products.stream().filter(Objects::nonNull) // Filter out null products
//				.map(product -> {
//					ProductWithSellerDetailsDTO dto = new ProductWithSellerDetailsDTO(product);
//
//					// Set seller details
//
//					return dto;
//				}).collect(Collectors.toList());
//	}

	@Override
	public Product getProduct(Integer productId) throws ProductException {
		if (productId == null) {
			throw new ProductException("Product ID cannot be null.");
		}
		return productRepository.findById(productId)
				.orElseThrow(() -> new ProductException("Product not found with ID: " + productId));
	}
}


