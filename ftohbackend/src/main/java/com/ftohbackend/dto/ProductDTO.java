package com.ftohbackend.dto;

public class ProductDTO {
	Integer productId;
	Integer sellerId;
	Double productPrice;
	String productName;
	Double productQuantity;
	public ProductDTO(Integer productId, Integer sellerId, Double productPrice, String productName,
			Double productQuantity) {
		super();
		this.productId = productId;
		this.sellerId = sellerId;
		this.productPrice = productPrice;
		this.productName = productName;
		this.productQuantity = productQuantity;
	}
	public ProductDTO() {
		// TODO Auto-generated constructor stub
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
	@Override
	public String toString() {
		return "ProductDTO [productId=" + productId + ", sellerId=" + sellerId + ", productPrice=" + productPrice
				+ ", productName=" + productName + ", productQuantity=" + productQuantity + "]";
	}

	

}
