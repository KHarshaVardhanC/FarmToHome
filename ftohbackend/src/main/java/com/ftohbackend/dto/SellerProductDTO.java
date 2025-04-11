package com.ftohbackend.dto;

public class SellerProductDTO {
	Integer productId;
	Double productPrice;
	String ImageUrl;
	String productDescription;
	String productName;
	Double productQuantity;
	Double productRatingValue;
	Integer productRatingCount;

	public SellerProductDTO(Integer productId, Double productPrice, String imageUrl, String productDescription,
			String productName, Double productQuantity, Double productRatingValue, Integer productRatingCount) {
		super();
		this.productId = productId;
		this.productPrice = productPrice;
		this.ImageUrl = imageUrl;
		this.productDescription = productDescription;
		this.productName = productName;
		this.productQuantity = productQuantity;
		this.productRatingValue=productRatingValue;
		this.productRatingCount=productRatingCount;

	}

	public SellerProductDTO() {
		// TODO Auto-generated constructor stub
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
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

	
	public Double getProductRatingValue() {
		return productRatingValue;
	}

	public void setProductRatingValue(Double productRatingValue) {
		this.productRatingValue = productRatingValue;
	}
	
	
	public Integer getProductRatingCount() {
		return productRatingCount;
	}

	public void setProductRatingCount(Integer productRatingCount) {
		this.productRatingCount = productRatingCount;
	}

	@Override
	public String toString() {
		return "SellerProductDTO [productId=" + productId + ", productPrice=" + productPrice + ", ImageUrl=" + ImageUrl
				+ ", productDescription=" + productDescription + ", productName=" + productName + ", productQuantity="
				+ productQuantity + ", productRatingValue=" + productRatingValue + ", productRatingCount="
				+ productRatingCount + "]";
	}

	
}
