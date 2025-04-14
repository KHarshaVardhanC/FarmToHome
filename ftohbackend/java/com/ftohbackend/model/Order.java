package com.ftohbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table(name = "Orders1")
@Entity
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer orderId;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    public Product product;

    @ManyToOne
    @JoinColumn(name = "customerId", nullable = false)
    public Customer customer;
    
    
    // this will be a { In cart, Ordered, Success, Deleted, Failed }
    @Column(nullable =false)
    public String orderStatus;
    
    @Column(nullable = false)
    public Double orderQuantity;
    

	public Order(Integer orderId, Product product, String orderStatus, Customer customer, Double orderQuantity) {
		super();
		this.orderId = orderId;
		this.product = product;
		this.customer = customer;
		this.orderQuantity=orderQuantity;
		this.orderStatus="Incart";
//		this.productPrice=productPrice;
	}

	public Order() {
		
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Double getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(Double orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", product=" + product + ", customer=" + customer + ", orderStatus="
				+ orderStatus + ", orderQuantity=" + orderQuantity + "]";
	}

	
    
    
    
    
}
