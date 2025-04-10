package com.ftohbackend.dto;

public class ProductDTO {
	Integer productId;
	Integer sellerId;
	Double productPrice;
	String productName;
	Double productQuantity;
	String ImageUrl;
	String productDescription;
	String productCategory;
	
	public ProductDTO() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	public ProductDTO(Integer productId, Integer sellerId, Double productPrice, String productName,
			Double productQuantity, String imageUrl, String productDescription, String productCategory) {
		super();
		this.productId = productId;
		this.sellerId = sellerId;
		this.productPrice = productPrice;
		this.productName = productName;
		this.productQuantity = productQuantity;
		ImageUrl = imageUrl;
		this.productDescription = productDescription;
		this.productCategory=productCategory;
		
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



	public String getImageUrl() {
		return ImageUrl;
	}



	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
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
		return "ProductDTO [productId=" + productId + ", sellerId=" + sellerId + ", productPrice=" + productPrice
				+ ", productName=" + productName + ", productQuantity=" + productQuantity + ", ImageUrl=" + ImageUrl
				+ ", productDescription=" + productDescription + ", productCategory=" + productCategory + "]";
	}



	

	

}
