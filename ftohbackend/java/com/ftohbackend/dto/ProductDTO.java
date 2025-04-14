package com.ftohbackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
	
	Integer productId;
	@NotNull(message = "Seller Id required")
	Integer sellerId;
	@NotNull(message = "Product Price required")
	Double productPrice;
	@NotNull(message = "Product Name required")
	String productName;
	@NotNull(message = "Product Quantity required")
	Double productQuantity;
	@NotNull(message = "Product Quantity Type required")
	String productQuantityType;
	@NotNull(message = "Product Image required")
	String ImageUrl;
	@NotNull(message = "Product Description required")
	String productDescription;
	@NotNull(message = "Product Category required")
	String productCategory;
	Double productRatingValue;
	Integer productRatingCount;
	
	public ProductDTO() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	public ProductDTO(Integer productId, Integer sellerId, Double productPrice, String productName,
			Double productQuantity, String imageUrl, String productDescription, String productCategory,Double productRatingValue,Integer productRatingCount) {
		super();
		this.productId = productId;
		this.sellerId = sellerId;
		this.productPrice = productPrice;
		this.productName = productName;
		this.productQuantity = productQuantity;
		ImageUrl = imageUrl;
		this.productDescription = productDescription;
		this.productCategory=productCategory;
		this.productRatingValue=productRatingValue;
		this.productRatingCount=productRatingCount;


		
	}



	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getSellerId() {
		return sellerId;
	}
	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}
	public Double getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(Double productPrice) {
		this.productPrice = productPrice;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Double getProductQuantity() {
		return productQuantity;
	}
	public void setProductQuantity(Double productQuantity) {
		this.productQuantity = productQuantity;
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

	public Integer getProductRatingCount() {
		return productRatingCount;
	}

	public void setProductRatingCount(Integer productRatingCount) {
		this.productRatingCount = productRatingCount;
	}


	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	


	public String getProductCategory() {
		return productCategory;
	}



	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}



	public Double getProductRatingValue() {
		return productRatingValue;
	}



	public void setProductRatingValue(Double productRatingValue) {
		this.productRatingValue = productRatingValue;
	}



	@Override
	public String toString() {
		return "ProductDTO [productId=" + productId + ", sellerId=" + sellerId + ", productPrice=" + productPrice
				+ ", productName=" + productName + ", productQuantity=" + productQuantity + ", ImageUrl=" + ImageUrl
				+ ", productDescription=" + productDescription + ", productCategory=" + productCategory
				+ ", productRatingValue=" + productRatingValue + ", productRatingCount=" + productRatingCount + "]";
	}




	

	

}
