  package com.ftohbackend.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

	@Autowired
	RatingServiceImpl ratingServiceImpl;

	@Override
	public ProductDTO addProduct(ProductRequest productRequest) throws ProductException {
		try {
			if (productRequest == null) {
				throw new ProductException("Product request cannot be null.");
			}

			Product product = new Product();
			product.setProductName(productRequest.getProductName());
			product.setProductPrice(productRequest.getProductPrice());
			product.setProductQuantity(productRequest.getProductQuantity());
			product.setProductDescription(productRequest.getProductDescription());
			product.setProductCategory(productRequest.getProductCategory());
			product.setProductQuantityType(productRequest.getProductQuantityType());

			try {
				product.setSeller(sellerService.getSeller(productRequest.getSellerId()));
			} catch (Exception e) {
				throw new ProductException("Invalid seller ID: " + productRequest.getSellerId());
			}

			if (productRequest.getImage() == null || productRequest.getImage().isEmpty()) {
				throw new ProductException("Product image is required.");
			}

			String imageUrl = uploadImage(productRequest.getImage());
			product.setImageUrl(imageUrl);

			Product savedProduct = productRepository.save(product);
			return modelMapper.map(savedProduct, ProductDTO.class);

		} catch (IOException e) {
			throw new ProductException("Error while uploading image.");
		} catch (Exception e) {
			throw new ProductException("Failed to add product.");
		}
	}

	private String uploadImage(MultipartFile file) throws IOException {
		try {
			Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
			return uploadResult.get("url").toString();
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
	public String updateProduct(Integer productId, Product updatedDetails) throws ProductException, Exception {
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

		if (updatedDetails.getProductDescription() != null) {
			product.setProductDescription(updatedDetails.getProductDescription());
		}

		if (updatedDetails.getProductCategory() != null) {
			product.setProductCategory(updatedDetails.getProductCategory());
		}
		
		if(updatedDetails.getProductQuantityType() != null)
		{
			product.setProductQuantityType(updatedDetails.getProductQuantityType());
		}

//		updatedDetails.getPro

		if (updatedDetails.getProductRatingValue() != null) {
			int len = 0;
			try {
				len = ratingServiceImpl.getRatingsByProductId(productId).size();
			} catch (Exception e) {
				len = 0;
			} finally {

				if (len == 0) {
					product.setProductRatingValue(updatedDetails.getProductRatingValue());

				} else {
					double ratingValue = (product.getProductRatingValue() * len
							+ updatedDetails.getProductRatingValue()) / (double) (len + 1);

					product.setProductRatingValue(Math.round(ratingValue * 10.0) / 10.0);

				}
			}
			product.setProductRatingCount(len + 1);
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

	@Override
	public List<CustomerProductDTO> searchProductsWithSellerDetails(String productName) throws ProductException {
		if (productName == null || productName.trim().isEmpty()) {
			throw new ProductException("Product name cannot be null or empty.");
		}

		List<Product> products = productRepository.findProductsByNameWithSeller(productName.trim());
		if (products == null || products.isEmpty()) {
			throw new ProductException("No products found with name: " + productName);
		}

		List<CustomerProductDTO> customerproductdto = new ArrayList<>();
		for (Product product : products) {
			if (product.getProductQuantity() == 0.0) {

				CustomerProductDTO customerProductDTO = new CustomerProductDTO();

				customerProductDTO.setImageUrl(product.getImageUrl());
				customerProductDTO.setProductDescription(product.getProductDescription());
				customerProductDTO.setProductId(product.getProductId());
				customerProductDTO.setProductPrice(product.getProductPrice());
				customerProductDTO.setProductName(product.getProductName());
				customerProductDTO.setProductQuantity(product.getProductQuantity());
				customerProductDTO.setProductQuantityType(product.getProductQuantityType());
				customerProductDTO.setProductRatingCount(product.getProductRatingCount());
				customerProductDTO.setProductRatingValue(product.getProductRatingValue());
				customerProductDTO.setSellerName(
						product.getSeller().getSellerFirstName() + " " + product.getSeller().getSellerLastName());
				customerProductDTO.setSellerCity(product.getSeller().getSellerCity());
				customerProductDTO.setSellerPlace(product.getSeller().getSellerPlace());

				customerproductdto.add(customerProductDTO);
			}
		}

		return customerproductdto;
	}

	@Override
	public Product getProduct(Integer productId) throws ProductException {
		if (productId == null) {
			throw new ProductException("Product ID cannot be null.");
		}
		return productRepository.findById(productId)
				.orElseThrow(() -> new ProductException("Product not found with ID: " + productId));
	}

	@Override
	public List<Product> getCategoryProducts(String productCategory) throws ProductException {

		return productRepository.findByProductCategory(productCategory);

	}

}


