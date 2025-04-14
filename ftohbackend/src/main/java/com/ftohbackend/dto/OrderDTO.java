package com.ftohbackend.dto;




public class OrderDTO {
    private Integer orderId;
    private Integer productId;
    private Double orderQuantity;
    private Integer customerId;
 // this will be a { In cart, Ordered, Success, Deleted, Failed }
    public String orderStatus;
	public OrderDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OrderDTO(Integer orderId, Integer productId, Double orderQuantity, Integer customerId) {
		super();
		this.orderId = orderId;
		this.productId = productId;
		this.orderQuantity = orderQuantity;
		this.customerId = customerId;
		this.orderStatus="In cart";
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
	public Double getOrderQuantity() {
		return orderQuantity;
	}
	public void setOrderQuantity(Double orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	
	
	
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus() {
		this.orderStatus = "In Cart1";
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	@Override
	public String toString() {
		return "OrderDTO [orderId=" + orderId + ", productId=" + productId + ", orderQuantity=" + orderQuantity
				+ ", customerId=" + customerId + ", orderStatus=" + orderStatus + "]";
	}
	
	
    
    
}

