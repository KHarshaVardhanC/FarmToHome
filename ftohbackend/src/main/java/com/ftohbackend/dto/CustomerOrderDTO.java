package com.ftohbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrderDTO {

	Integer orderId;
	String productName;
	String ImageUrl;
	String ProductDescription;
	String productQuantityType;

	Double orderQuantity;
	Double productPrice;
	String orderStatus;
	String sellerName;
	String sellerPlace;
	String sellerCity;
	String sellerState;
	String sellerPincode;
	String customerName;
	String customerPlace;
	String customerCity;
	String customerState;
	String customerPincode;



//	
//	public CustomerOrderDTO(Integer orderId, String productName, String imageUrl, String productDescription,
//			Double orderQuantity, Double productPrice, String orderStatus, String sellerName, String sellerPlace,
//			String sellerCity, String sellerState, String sellerPincode, String customerName, String customerPlace,
//			String customerCity, String customerState, String customerPincode) {
//		super();
//		this.orderId = orderId;
//		this.productName = productName;
//		ImageUrl = imageUrl;
//		ProductDescription = productDescription;
//		this.orderQuantity = orderQuantity;
//		this.productPrice = productPrice;
//		this.orderStatus = orderStatus;
//		this.sellerName = sellerName;
//		this.sellerPlace = sellerPlace;
//		this.sellerCity = sellerCity;
//		this.sellerState = sellerState;
//		this.sellerPincode = sellerPincode;
//		this.customerName = customerName;
//		this.customerPlace = customerPlace;
//		this.customerCity = customerCity;
//		this.customerState = customerState;
//		this.customerPincode = customerPincode;
//	}
//
//	public CustomerOrderDTO() {
//		super();
//		// TODO Auto-generated constructor stub
//	}
//
//	public Integer getOrderId() {
//		return orderId;
//	}
//
//	public void setOrderId(Integer orderId) {
//		this.orderId = orderId;
//	}
//
//	public String getProductName() {
//		return productName;
//	}
//
//	public void setProductName(String productName) {
//		this.productName = productName;
//	}
//
//	public Double getOrderQuantity() {
//		return orderQuantity;
//	}
//
//	public void setOrderQuantity(Double orderQuantity) {
//		this.orderQuantity = orderQuantity;
//	}
//
//	public Double getProductPrice() {
//		return productPrice;
//	}
//
//	public void setProductPrice(Double productPrice) {
//		this.productPrice = productPrice;
//	}
//
//	public String getSellerName() {
//		return sellerName;
//	}
//
//	public void setSellerName(String sellerName) {
//		this.sellerName = sellerName;
//	}
//	
//	
//
//	public String getImageUrl() {
//		return ImageUrl;
//	}
//
//	public void setImageUrl(String imageUrl) {
//		ImageUrl = imageUrl;
//	}
//
//	public String getProductDescription() {
//		return ProductDescription;
//	}
//
//	public void setProductDescription(String productDescription) {
//		ProductDescription = productDescription;
//	}
//
//	
//	
//	
//	public String getSellerPlace() {
//		return sellerPlace;
//	}
//
//
//	public void setSellerPlace(String sellerPlace) {
//		this.sellerPlace = sellerPlace;
//	}
//
//
//	public String getSellerCity() {
//		return sellerCity;
//	}
//
//
//	public void setSellerCity(String sellerCity) {
//		this.sellerCity = sellerCity;
//	}
//
//
//	public String getSellerState() {
//		return sellerState;
//	}
//
//
//	public void setSellerState(String sellerState) {
//		this.sellerState = sellerState;
//	}
//
//
//	public String getSellerPincode() {
//		return sellerPincode;
//	}
//
//
//	public void setSellerPincode(String sellerPincode) {
//		this.sellerPincode = sellerPincode;
//	}
//
//
//	public String getCustomerName() {
//		return customerName;
//	}
//
//
//	public void setCustomerName(String customerName) {
//		this.customerName = customerName;
//	}
//
//
//	public String getCustomerPlace() {
//		return customerPlace;
//	}
//
//
//	public void setCustomerPlace(String customerPlace) {
//		this.customerPlace = customerPlace;
//	}
//
//
//	public String getCustomerCity() {
//		return customerCity;
//	}
//
//
//	public void setCustomerCity(String customerCity) {
//		this.customerCity = customerCity;
//	}
//
//
//	public String getCustomerState() {
//		return customerState;
//	}
//
//
//	public void setCustomerState(String customerState) {
//		this.customerState = customerState;
//	}
//
//
//	public String getCustomerPincode() {
//		return customerPincode;
//	}
//
//
//	public void setCustomerPincode(String customerPincode) {
//		this.customerPincode = customerPincode;
//	}
//
//	
//
//	public String getOrderStatus() {
//		return orderStatus;
//	}
//
//	public void setOrderStatus(String orderStatus) {
//		this.orderStatus = orderStatus;
//	}
//
//	@Override
//	public String toString() {
//		return "CustomerOrderDTO [orderId=" + orderId + ", productName=" + productName + ", ImageUrl=" + ImageUrl
//				+ ", ProductDescription=" + ProductDescription + ", orderQuantity=" + orderQuantity + ", productPrice="
//				+ productPrice + ", orderStatus=" + orderStatus + ", sellerName=" + sellerName + ", sellerPlace="
//				+ sellerPlace + ", sellerCity=" + sellerCity + ", sellerState=" + sellerState + ", sellerPincode="
//				+ sellerPincode + ", customerName=" + customerName + ", customerPlace=" + customerPlace
//				+ ", customerCity=" + customerCity + ", customerState=" + customerState + ", customerPincode="
//				+ customerPincode + "]";
//	}


	
	
}
