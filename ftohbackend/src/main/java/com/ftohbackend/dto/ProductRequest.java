package com.ftohbackend.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductRequest {

	
	Integer sellerId;
	@NotNull(message = "Product Price Required")
	Double productPrice;
	@NotNull(message = "Product name Required")
	String productName;
	@NotNull(message = "Product Quantity Required")
	Double productQuantity;
	@NotNull(message = "Product Quantity Type Required")
	String productQuantityType;
	@NotNull(message = "Product Image Required")
	MultipartFile image;
	@NotNull(message = "Product Description Required")
	String productDescription;
	@NotNull(message = "Product Category Required")
	String productCategory;
	Double discountPercentage;
	Double minOrderQuantity;

}
