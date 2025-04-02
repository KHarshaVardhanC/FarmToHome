package com.ftohbackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "all_orders")
public class AllOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;
    private Integer productId;
    private String productName;
    private Double productPrice;
    private Double productQuantity;
    private String sellerId;
    private Integer customerId;
    
    
    public AllOrders(Integer orderId, Integer productId, String productName, Double productPrice,
			Double productQuantity, String sellerId, Integer customerId) {
		super();
		this.orderId = orderId;
		this.productId = productId;
		this.productName = productName;
		this.productPrice = productPrice;
		this.productQuantity = productQuantity;
		this.sellerId = sellerId;
		this.customerId = customerId;
	}
	
    @Override
	public String toString() {
		return "AllOrders [orderId=" + orderId + ", productId=" + productId + ", productName=" + productName
				+ ", productPrice=" + productPrice + ", productQuantity=" + productQuantity + ", sellerId=" + sellerId
				+ ", customerId=" + customerId + "]";
	}
	
    public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Double getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(Double productPrice) {
		this.productPrice = productPrice;
	}
	public Double getProductQuantity() {
		return productQuantity;
	}
	public void setProductQuantity(Double productQuantity) {
		this.productQuantity = productQuantity;
	}
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	
}
