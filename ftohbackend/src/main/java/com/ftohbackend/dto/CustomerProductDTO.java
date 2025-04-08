package com.ftohbackend.dto;

import com.ftohbackend.model.Product;
import com.ftohbackend.model.Seller;

public class ProductWithSellerDetailsDTO {
    private Integer productId;
    private String productName;
    private Double productPrice;
    private Double productQuantity;
    private String sellerName;
    private String sellerPlace;
    private String sellerCity;
    
    // Constructor to map from Product entity
    public ProductWithSellerDetailsDTO(Product product) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.productPrice = product.getProductPrice();
        this.productQuantity = product.getProductQuantity();
        
        Seller seller = product.getSeller();
        this.sellerName = seller.getSellerFirstName() + " " + seller.getSellerLastName();
        this.sellerPlace = seller.getSellerPlace();
        this.sellerCity = seller.getSellerCity();
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

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getSellerPlace() {
		return sellerPlace;
	}

	public void setSellerPlace(String sellerPlace) {
		this.sellerPlace = sellerPlace;
	}

	public String getSellerCity() {
		return sellerCity;
	}

	public void setSellerCity(String sellerCity) {
		this.sellerCity = sellerCity;
	}

	@Override
	public String toString() {
		return "ProductWithSellerDetailsDTO [productId=" + productId + ", productName=" + productName
				+ ", productPrice=" + productPrice + ", productQuantity=" + productQuantity + ", sellerName="
				+ sellerName + ", sellerPlace=" + sellerPlace + ", sellerCity=" + sellerCity + "]";
	}
    
	
	
}