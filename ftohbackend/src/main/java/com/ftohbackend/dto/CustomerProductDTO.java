package com.ftohbackend.dto;

import com.ftohbackend.model.Product;
import com.ftohbackend.model.Seller;

public class CustomerProductDTO {
    private Integer productId;
    private String productName;
    private Double productPrice;
    public String ImageUrl;
    public String productDescription;
    private Double productQuantity;
    private String sellerName;
    private String sellerPlace;
    private String sellerCity;
    
    
    
    
    
    public CustomerProductDTO(Integer productId, String productName, Double productPrice, String imageUrl,
			String productDescription, Double productQuantity, String sellerName, String sellerPlace,
			String sellerCity) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.productPrice = productPrice;
		ImageUrl = imageUrl;
		this.productDescription = productDescription;
		this.productQuantity = productQuantity;
		this.sellerName = sellerName;
		this.sellerPlace = sellerPlace;
		this.sellerCity = sellerCity;
	}

	// Constructor to map from Product entity
    public CustomerProductDTO(Product product) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.productPrice = product.getProductPrice();
        this.ImageUrl=product.getImageUrl();
        this.productDescription=product.getProductDescription();
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
		return "CustomerProductDTO [productId=" + productId + ", productName=" + productName + ", productPrice="
				+ productPrice + ", ImageUrl=" + ImageUrl + ", productDescription=" + productDescription
				+ ", productQuantity=" + productQuantity + ", sellerName=" + sellerName + ", sellerPlace=" + sellerPlace
				+ ", sellerCity=" + sellerCity + "]";
	}

	
	
}