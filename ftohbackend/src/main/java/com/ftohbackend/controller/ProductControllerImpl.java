package com.ftohbackend.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ftohbackend.dto.ProductDto;
import com.ftohbackend.model.ProductModel;
import com.ftohbackend.service.ProductService;

import jakarta.validation.Valid;

@RestController
public class ProductControllerImpl {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	ProductService prodservice;

	@PostMapping("/product")
	public String addProduct(@Valid @RequestBody ProductDto productDto) {
		ProductModel product=modelMapper.map(productDto,ProductModel.class);
		return prodservice.addProduct(product);
	}

	@GetMapping("/product/{productTitle}")
	public ProductDto getProduct(@PathVariable String productTitle) {
		return modelMapper.map(prodservice.getProductByTitle(productTitle), ProductDto.class);
	}

	@PutMapping("/product/{productid}")
	public String updateProduct(@PathVariable Integer productid, @RequestBody ProductDto updatedDetails) {
		ProductModel prod=modelMapper.map(updatedDetails,ProductModel.class);
		return prodservice.updateProduct(productid, prod);
	}

	@DeleteMapping("/product/{productid}")
	public String deleteProduct(@PathVariable Integer productid){
		return prodservice.deleteProduct(productid);
	}



}
