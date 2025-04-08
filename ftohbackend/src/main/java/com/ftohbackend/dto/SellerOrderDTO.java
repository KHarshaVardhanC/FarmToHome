package com.ftohbackend.dto;

public class SellerOrderDTO {

	Integer orderId;
	String productName;
	String ImageUrl;
	String productDescription;
	Double orderQuantity;
	Double productPrice;
	String CustomerName;
	
	public SellerOrderDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public SellerOrderDTO(Integer orderId, String productName, String imageUrl, String productDescription,
			Double orderQuantity, Double productPrice, String customerName) {
		super();
		this.orderId = orderId;
		this.productName = productName;
		this.ImageUrl = imageUrl;
		this.productDescription = productDescription;
		this.orderQuantity = orderQuantity;
		this.productPrice = productPrice;
		CustomerName = customerName;
	}


	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Double getOrderQuantity() {
		return orderQuantity;
	}
	public void setOrderQuantity(Double orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
	public Double getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(Double productPrice) {
		this.productPrice = productPrice;
	}
	public String getCustomerName() {
		return CustomerName;
	}
	public void setCustomerName(String customerName) {
		CustomerName = customerName;
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


	@Override
	public String toString() {
		return "SellerOrderDTO [orderId=" + orderId + ", productName=" + productName + ", ImageUrl=" + ImageUrl
				+ ", productDescription=" + productDescription + ", orderQuantity=" + orderQuantity + ", productPrice="
				+ productPrice + ", CustomerName=" + CustomerName + "]";
	}
	
	

}
