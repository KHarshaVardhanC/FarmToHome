package com.ftohbackend.dto;

public class SellerProductDTO {
	Integer productId;
	Double productPrice;
	String productName;
	Double productQuantity;
	public SellerProductDTO(Integer productId, Double productPrice, String productName,
			Double productQuantity) {
		super();
		this.productId = productId;
		this.productPrice = productPrice;
		this.productName = productName;
		this.productQuantity = productQuantity;
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
	@Override
	public String toString() {
		return "ProductDTO [productId=" + productId + ", productPrice=" + productPrice
				+ ", productName=" + productName + ", productQuantity=" + productQuantity + "]";
	}

	

}
