package com.ftohbackend.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ftohbackend.dto.ProductDTO;
import com.ftohbackend.dto.ProductWithSellerDetailsDTO;
import com.ftohbackend.dto.SellerProductDTO;
import com.ftohbackend.model.Product;
import com.ftohbackend.service.ProductService;

import jakarta.validation.Valid;

@RestController
public class ProductControllerImpl implements ProductController{

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	ProductService productService;

	@PostMapping("/product")
	@Override
	public String addProduct(@Valid @RequestBody ProductDTO productDTO) {
		Product product=modelMapper.map(productDTO,Product.class);
		return productService.addProduct(product);
	}

//	@GetMapping("/product/{productName}")
//	@Override
//	public List<ProductWithSellerDetailsDTO> getProduct(@PathVariable String productName) {
//		return modelMapper.map(productService.searchProductsWithSellerDetails(productName), ProductWithSellerDetailsDTO.class);
//	}

	
	@GetMapping("/product1/{productName}")
	@Override
	public ResponseEntity<List<ProductWithSellerDetailsDTO>> getProductByName(
	        @PathVariable String productName) {
	    List<ProductWithSellerDetailsDTO> products = productService.searchProductsWithSellerDetails(productName);
	    return ResponseEntity.ok(products);
	}
	
	@PutMapping("/product/{productId}")
	@Override
	public String updateProduct(@PathVariable Integer productId, @RequestBody ProductDTO updatedDetails) {
		Product prod=modelMapper.map(updatedDetails,Product.class);
		return productService.updateProduct(productId, prod);
	}

	@DeleteMapping("/product/{productId}")
	@Override
	public String deleteProduct(@PathVariable Integer productId){
		return productService.deleteProduct(productId);
	}

	@GetMapping("/product/{sellerId}")
	@Override
	public List<SellerProductDTO> getProducts(@PathVariable Integer sellerId) {
		// TODO Auto-generated method stub
		
		List<Product> products=productService.getAllProduct(sellerId);
		
		
		List<SellerProductDTO> sellerproductdtos=new ArrayList<>();
		for(Product product: products)
		{
			SellerProductDTO sellerproductDTO=new SellerProductDTO();
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
	public ProductDTO getProduct(@PathVariable Integer productId)
	{
		return modelMapper.map( productService.getProduct(productId), ProductDTO.class);
	}



}
