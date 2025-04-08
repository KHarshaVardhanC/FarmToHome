package com.ftohbackend.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ftohbackend.dto.CustomerProductDTO;
import com.ftohbackend.dto.ProductDTO;
import com.ftohbackend.dto.ProductRequest;
import com.ftohbackend.exception.ProductException;
import com.ftohbackend.model.Product;
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
	SellerService sellerService;

	@Autowired
	Cloudinary cloudinary;

	@Autowired
	ModelMapper modelMapper;

//	@Override
//	public String addProduct(Product product) throws ProductException {
//		if (product == null) {
//			throw new ProductException("Product object is null.");
//		}
//		productRepository.save(product);
//		return "Product added successfully";
//	}

//	public String addProduct(ProductRequest productRequest) throws IOException {
//		Product product = new Product();
//		product.setProductName(productRequest.getName());
//		product.setProductPrice(productRequest.getPrice());
//		product.setProductQuantity((double) productRequest.getQuantity());
//
//		if (productRequest.getImage() != null && !productRequest.getImage().isEmpty()) {
//			String imageUrl = uploadImage(productRequest.getImage());
//			product.setImageUrl(imageUrl);
//		}
//
//		Product savedProduct = productRepository.save(product);
//		return "Product Added Successfull";
//	}
//
//	
//	public String uploadImage(MultipartFile file) throws IOException {
//		Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
//		return uploadResult.get("url").toString();
//	}

	@Override
	public ProductDTO addProduct(ProductRequest productRequest) throws Exception {
		// Convert ProductRequest to Product entity
		Product product = new Product();
		
		product.setProductId(productRequest.getProductId());
		product.setProductName(productRequest.getProductName());
		product.setProductPrice(productRequest.getProductPrice());
		product.setProductQuantity(productRequest.getProductQuantity());
		product.setProductDescription(productRequest.getProductDescription());
		product.setSeller( sellerService.getSeller( productRequest.getSellerId()));
//		product.setRatings(null);

		// Handle image upload
//		if (productRequest.getImage() != null && !productRequest.getImage().isEmpty()) {
			String imageUrl = uploadImage(productRequest.getImage());
			product.setImageUrl(imageUrl);
//		}
		
//		product.setImageUrl("localhost");

		// Save to database
		Product savedProduct = productRepository.save(product);

		// Convert to DTO for response
		return modelMapper.map(savedProduct, ProductDTO.class);
	}

	private String uploadImage(MultipartFile file) throws IOException {
		try {
			Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
			return uploadResult.get("cloudinary://995583833121231:dL2BK4p1qKd2odkjZGtyjzlpv_c@dqarxeygt").toString();
		} catch (IOException e) {
			throw new IOException("Failed to upload image to Cloudinary", e);
		}
	}

	private ProductDTO convertToDTO(Product product) {
		ProductDTO dto = new ProductDTO();
		dto.setProductId(product.getProductId());
		dto.setProductName(product.getProductName());
		dto.setProductPrice(product.getProductPrice());
		dto.setProductQuantity(product.getProductQuantity());
		dto.setImageUrl(product.getImageUrl());
		return dto;
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
		throw new ProductException("Product with name '" + name + "' not found.");
	}

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
	public List<Product> getProductsBySellerId(Integer sellerId) throws ProductException {
		if (sellerId == null) {
			throw new ProductException("Seller ID cannot be null.");
		}
		List<Product> products = productRepository.findBySellerSellerId(sellerId);
		if (products.isEmpty()) {
			throw new ProductException("No products found for seller ID: " + sellerId);
		}
		return products;
	}

//	@Override
//	public Product createProduct(Integer sellerId, Product product)throws ProductException  {
//		Seller seller = sellerRepository.findById(sellerId).orElseThrow(() -> new RuntimeException("Seller not found"));
//		product.setSeller(seller);
//		return productRepository.save(product);
//	}

	@Override
	public List<CustomerProductDTO> searchProductsWithSellerDetails(String productName) throws Exception {
		List<Product> products = productRepository.findProductsByNameWithSeller(productName);
		return products.stream().map(CustomerProductDTO::new).collect(Collectors.toList());
	}

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
