package com.ftohbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="AllProduct")
public class allProductModel {

	@Id
	@Column(name="product_id")
	Integer product_id;

	@Column(name="seller_id")
	String seller_id;

	@Column(name="product_price")
	Double product_price;

	@Column(name="product_name")
	String product_name;

	@Column(name="product_quantity")
	Double product_quantity;

	@Column(name="product_location")
	String product_location;

	public Integer getProduct_id() {
		return product_id;
	}

	public void setProduct_id(Integer product_id) {
		this.product_id = product_id;
	}

	public String getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}

	public Double getProduct_price() {
		return product_price;
	}

	public void setProduct_price(Double product_price) {
		this.product_price = product_price;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public Double getProduct_quantity() {
		return product_quantity;
	}

	public void setProduct_quantity(Double product_quantity) {
		this.product_quantity = product_quantity;
	}

	public String getProduct_location() {
		return product_location;
	}

	public void setProduct_location(String product_location) {
		this.product_location = product_location;
	}

	public allProductModel(Integer product_id, String seller_id, Double product_price, String product_name,
			Double product_quantity, String product_location) {
		super();
		this.product_id = product_id;
		this.seller_id = seller_id;
		this.product_price = product_price;
		this.product_name = product_name;
		this.product_quantity = product_quantity;
		this.product_location = product_location;
	}

	public allProductModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "allProductModel [product_id=" + product_id + ", seller_id=" + seller_id + ", product_price="
				+ product_price + ", product_name=" + product_name + ", product_quantity=" + product_quantity
				+ ", product_location=" + product_location + "]";
	}



}
