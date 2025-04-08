package com.ftohbackend.dto;

import org.springframework.web.multipart.MultipartFile;

public class ProductRequest {
    
    
    
    Integer productId;
	Integer sellerId;
	Double productPrice;
	String productName;
	Double productQuantity;
	MultipartFile image;
	String ProductDescription;
	
	
	public ProductRequest() {
		super();
		// TODO Auto-generated constructor stub
	}


	public ProductRequest(Integer productId, Integer sellerId, Double productPrice, String productName,
			Double productQuantity, MultipartFile image, String productDescription) {
		super();
		this.productId = productId;
		this.sellerId = sellerId;
		this.productPrice = productPrice;
		this.productName = productName;
		this.productQuantity = productQuantity;
		this.image = image;
		this.ProductDescription = productDescription;
	}


	public Integer getProductId() {
		return productId;
	}


	public void setProductId(Integer productId) {
		this.productId = productId;
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
		return ProductDescription;
	}


	public void setProductDescription(String productDescription) {
		ProductDescription = productDescription;
	}


	@Override
	public String toString() {
		return "ProductRequest [productId=" + productId + ", sellerId=" + sellerId + ", productPrice=" + productPrice
				+ ", productName=" + productName + ", productQuantity=" + productQuantity + ", image=" + image
				+ ", ProductDescription=" + ProductDescription + "]";
	}
	
	
	
    
    
}
