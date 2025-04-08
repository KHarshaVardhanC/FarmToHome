package com.ftohbackend.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ftohbackend.dto.CustomerProductDTO;
import com.ftohbackend.dto.ProductDTO;
import com.ftohbackend.dto.ProductRequest;
import com.ftohbackend.dto.SellerProductDTO;
import com.ftohbackend.model.Product;
import com.ftohbackend.service.ProductService;
import com.ftohbackend.service.ProductServiceImpl;

import jakarta.validation.Valid;

@RestController
public class ProductControllerImpl implements ProductController {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	ProductService productService;

	@Autowired
	ProductServiceImpl productServiceImpl;
//	@PostMapping("/product")
//	@Override
//	public String addProduct(@Valid @RequestBody ProductDTO productDTO) throws Exception {
//		Product product = modelMapper.map(productDTO, Product.class);
//		return productService.addProduct(product);
//	}

//	@PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//	@Override
//	public String addProduct(@Valid @ModelAttribute ProductRequest productRequest) throws Exception {
//	    
//	    // Convert DTO to Entity
//	    Product product = modelMapper.map(productRequest, Product.class);
//	    
//	    // Handle image upload if present
//	    if (productRequest.getImage() != null && !productRequest.getImage().isEmpty()) {
//	        String imageUrl = productServiceImpl.uploadImage(productRequest.getImage());
//	        product.setImageUrl(imageUrl);
//	    }
//	    
//	    // Save product
//	    return productService.addProduct(productRequest);
//	    
//	}
//	
//	
//	
	
	
	@PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDTO> addProduct(
            @Valid @ModelAttribute ProductRequest productRequest) throws Exception {
        
        ProductDTO productDTO = productService.addProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
    }
	
	
	
	
//	@GetMapping("/product/{productName}")
//	@Override
//	public List<ProductWithSellerDetailsDTO> getProduct(@PathVariable String productName) {
//		return modelMapper.map(productService.searchProductsWithSellerDetails(productName), ProductWithSellerDetailsDTO.class);
//	}

	@GetMapping("/product1/{productName}")
	@Override
	public List<CustomerProductDTO> getProductByName(@PathVariable String productName) throws Exception {
		List<CustomerProductDTO> products = productService.searchProductsWithSellerDetails(productName);
		return products;
	}

	@PutMapping("/product/{productId}")
	@Override
	public String updateProduct(@PathVariable Integer productId, @RequestBody ProductDTO updatedDetails)
			throws Exception {
		Product prod = modelMapper.map(updatedDetails, Product.class);
		return productService.updateProduct(productId, prod);
	}

	@DeleteMapping("/product/{productId}")
	@Override
	public String deleteProduct(@PathVariable Integer productId) throws Exception {
		return productService.deleteProduct(productId);
	}

	@GetMapping("/product/{sellerId}")
	@Override
	public List<SellerProductDTO> getProducts(@PathVariable Integer sellerId) throws Exception {
		// TODO Auto-generated method stub

		List<Product> products = productService.getAllProduct(sellerId);

		List<SellerProductDTO> sellerproductdtos = new ArrayList<>();
		for (Product product : products) {
			SellerProductDTO sellerproductDTO = new SellerProductDTO();
			sellerproductDTO.setProductId(product.getProductId());
			sellerproductDTO.setProductName(product.getProductName());
			sellerproductDTO.setProductQuantity(product.getProductQuantity());
			sellerproductDTO.setProductPrice(product.getProductPrice());

			sellerproductdtos.add(sellerproductDTO);
		}

		return sellerproductdtos;

	}

	@GetMapping("/{productId}")
	@Override
	public ProductDTO getProduct(@PathVariable Integer productId) throws Exception {
		return modelMapper.map(productService.getProduct(productId), ProductDTO.class);
	}

}
