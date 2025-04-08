package com.ftohbackend.dto;

public class CustomerOrderDTO {

	Integer orderId;
	String productName;
	String ImageUrl;
	String ProductDescription;
	Double orderQuantity;
	Double productPrice;
	String sellerName;


	public CustomerOrderDTO(Integer orderId, String productName, String imageUrl, String productDescription,
			Double orderQuantity, Double productPrice, String sellerName) {
		super();
		this.orderId = orderId;
		this.productName = productName;
		ImageUrl = imageUrl;
		ProductDescription = productDescription;
		this.orderQuantity = orderQuantity;
		this.productPrice = productPrice;
		this.sellerName = sellerName;
	}

	public CustomerOrderDTO() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	
	

	public String getImageUrl() {
		return ImageUrl;
	}

	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}

	public String getProductDescription() {
		return ProductDescription;
	}

	public void setProductDescription(String productDescription) {
		ProductDescription = productDescription;
	}

	@Override
	public String toString() {
		return "CustomerOrderDTO [orderId=" + orderId + ", productName=" + productName + ", ImageUrl=" + ImageUrl
				+ ", ProductDescription=" + ProductDescription + ", orderQuantity=" + orderQuantity + ", productPrice="
				+ productPrice + ", sellerName=" + sellerName + "]";
	}

	
}
