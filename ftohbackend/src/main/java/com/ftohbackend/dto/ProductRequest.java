package com.ftohbackend.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ProductRequest {

	Integer sellerId;
	Double productPrice;
	String productName;
	Double productQuantity;
	String productQuantityType;
	MultipartFile image;
	String productDescription;
	String productCategory;

}
