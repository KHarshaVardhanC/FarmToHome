package com.ftohbackend.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftohbackend.dto.ProductWithSellerDetailsDTO;
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
	public String addProduct(Product product) {
		productRepository.save(product);
		return ("Added product");
	}

	@Override
	public List<Product> getAllProduct() {
		return productRepository.findAll();
	}
	
	@Override
	public List<Product> getAllProduct(Integer sellerId)
	{
		return productRepository.findBySellerSellerId(sellerId);
	}

//	public List<Product> getAllProductBySellerId(Integer sellerid) {
//		return productRepository.findAll().stream()
//				.filter(product -> product.getSeller().getSellerId().equals(sellerid)).toList();
//	}

	@Override
	public Product getProductByTitle(String name) {
		List<Product> allProducts = productRepository.findAll();
		Product product = new Product();
		for (Product prod : allProducts) {
			if (prod.getProductName().equalsIgnoreCase(name)) {
				return prod;
			}

		}
		return null;
	}

	@Override
	public String updateProduct(Integer productId, Product updatedDetails) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

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
	public String deleteProduct(Integer productId) {
		productRepository.deleteById(productId);
		return "Product is deleted";
	}

	@Override
	public List<Product> getProductsBySellerId(Integer sellerId) {
		return productRepository.findBySellerSellerId(sellerId);
	}

	@Override
	public Product createProduct(Integer sellerId, Product product) {
		Seller seller = sellerRepository.findById(sellerId).orElseThrow(() -> new RuntimeException("Seller not found"));
		product.setSeller(seller);
		return productRepository.save(product);
	}

//	@Override
//	public List<ProductWithSellerDetailsDTO> searchProductsWithSellerDetails(String productName) {
//		List<Product> products = productRepository.findProductsByNameWithSeller(productName);
//		return products.stream().map(ProductWithSellerDetailsDTO::new).collect(Collectors.toList());
//	}

	@Override
	public List<ProductWithSellerDetailsDTO> searchProductsWithSellerDetails(String productName) {
		// Input validation
		if (productName == null || productName.trim().isEmpty()) {
			throw new IllegalArgumentException("Product name cannot be null or empty");
		}

		// Search products with seller details
		List<Product> products = productRepository.findProductsByNameWithSeller(productName.trim());

		// Handle null case
		if (products == null) {
			return Collections.emptyList();
		}

		// Convert to DTOs
		return products.stream().filter(Objects::nonNull) // Filter out null products
				.map(product -> {
					ProductWithSellerDetailsDTO dto = new ProductWithSellerDetailsDTO(product);

					// Set seller details

					return dto;
				}).collect(Collectors.toList());
	}

	@Override
	public Product getProduct(Integer productId) {
		// TODO Auto-generated method stub
		
		return productRepository.findById(productId).get();
//		return null;
	}

}
