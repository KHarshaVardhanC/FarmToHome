package com.ftohbackend.dto;

import org.springframework.web.multipart.MultipartFile;

public class ProductRequest {
    
    
    
	Integer sellerId;
	Double productPrice;
	String productName;
	Double productQuantity;
	MultipartFile image;
	String productDescription;
	String productCategory;
	
	
	public ProductRequest() {
		super();
		// TODO Auto-generated constructor stub
	}


	public ProductRequest(Integer sellerId, Double productPrice, String productName,
			Double productQuantity, MultipartFile image, String productDescription, String productCategory) {
		super();
//		this.productId = productId;
		this.sellerId = sellerId;
		this.productPrice = productPrice;
		this.productName = productName;
		this.productQuantity = productQuantity;
		this.image = image;
		this.productDescription = productDescription;
		this.productCategory=productCategory;
	}


	
	public void setImage(MultipartFile image)
	{
		this.image=image;
	}



	public Integer getSellerId() {
		return sellerId;
	}


	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}


	public Double getProductPrice() {
		return productPrice;
	}


	public void setProductPrice(Double productPrice) {
		this.productPrice = productPrice;
	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


	public Double getProductQuantity() {
		return productQuantity;
	}


	public void setProductQuantity(Double productQuantity) {
		this.productQuantity = productQuantity;
	}


	public MultipartFile getImage() {
		return image;
	}


	
	 


	public String getProductDescription() {
		return productDescription;
	}


	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}


	public String getProductCategory() {
		return productCategory;
	}


	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}


	@Override
	public String toString() {
		return "ProductRequest [sellerId=" + sellerId + ", productPrice=" + productPrice + ", productName="
				+ productName + ", productQuantity=" + productQuantity + ", image=" + image + ", productDescription="
				+ productDescription + ", productCategory=" + productCategory + "]";
	}


	
	
	
	
    
    
}
