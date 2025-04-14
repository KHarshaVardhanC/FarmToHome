package com.ftohbackend.dto;

public class SellerOrderDTO {

	Integer orderId;
	String productName;
	String ImageUrl;
	String productDescription;
	Double orderQuantity;
	Double productPrice;
	String customerName;
	String orderStatus;
	
	public SellerOrderDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public SellerOrderDTO(Integer orderId, String productName, String imageUrl, String productDescription,
			Double orderQuantity, Double productPrice, String customerName,String orderStatus) {
		super();
		this.orderId = orderId;
		this.productName = productName;
		this.ImageUrl = imageUrl;
		this.productDescription = productDescription;
		this.orderQuantity = orderQuantity;
		this.productPrice = productPrice;
		this.customerName = customerName;
		this.orderStatus=orderStatus;
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


	public String getOrderStatus() {
		return orderStatus;
	}


	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}


	public String getCustomerName() {
		return customerName;
	}


	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}


	@Override
	public String toString() {
		return "SellerOrderDTO [orderId=" + orderId + ", productName=" + productName + ", ImageUrl=" + ImageUrl
				+ ", productDescription=" + productDescription + ", orderQuantity=" + orderQuantity + ", productPrice="
				+ productPrice + ", customerName=" + customerName + ", orderStatus=" + orderStatus + "]";
	}

	
	

}
