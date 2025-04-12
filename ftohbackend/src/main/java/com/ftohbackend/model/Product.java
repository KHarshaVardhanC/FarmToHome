package com.ftohbackend.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@Data
public class Product {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "productId")
	Integer productId;

	@ManyToOne(fetch = FetchType.LAZY) 
	@JoinColumn(name = "sellerId", nullable = false)
	private Seller seller;

	@NotNull(message = "Product price cannot be null")
	@Positive(message = "Product price must be positive")
	@Column(name = "productPrice")
	Double productPrice;

	@NotBlank(message = "Product name cannot be blank")
	@Size(max = 100, message = "Product name cannot exceed 100 characters")
	@Column(name = "productName")
	String productName;
	
	
	@Column(name = "ImageUrl", nullable = false)
	String ImageUrl;
	
	@Column(name = "productDescription", nullable = false)
	String productDescription;

	@NotNull(message = "Product quantity cannot be null")
	@PositiveOrZero(message = "Product quantity must be zero or positive")
	@Column(name = "productQuantity")
	Double productQuantity;
	
	@NotNull(message ="Select Category")
	@Column(name = "productCategory")
	String productCategory;
	
	String productQuantityType;
	
	Double productRatingValue;
	Integer productRatingCount;
	
	

	@OneToMany(mappedBy = "product" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<Order> orders=new ArrayList<>();
	
	
	@OneToMany(mappedBy = "product" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<Rating> ratings=new ArrayList<>();
	


	// Constructors
//	public Product() {
//		super();
//	}
//
//
//
//	public Product(Integer productId, Seller seller,
//		@NotNull(message = "Product price cannot be null") @Positive(message = "Product price must be positive") Double productPrice,
//		@NotBlank(message = "Product name cannot be blank") @Size(max = 100, message = "Product name cannot exceed 100 characters") String productName,
//		String imageUrl, String productDescription,
//		@NotNull(message = "Product quantity cannot be null") @PositiveOrZero(message = "Product quantity must be zero or positive") Double productQuantity,
//		List<Order> orders, List<Rating> ratings, String productCategory, Integer productRatingCount) {
//	super();
//	this.productId = productId;
//	this.seller = seller;
//	this.productPrice = productPrice;
//	this.productName = productName;
//	ImageUrl = imageUrl;
//	this.productDescription = productDescription;
//	this.productQuantity = productQuantity;
//	this.orders = orders;
//	this.ratings = ratings;
//	this.productCategory=productCategory;
//	this.productRatingValue=0.0;
//	this.productRatingCount=0;
//}
//
//
//
//	public Integer getProductId() {
//		return productId;
//	}
//
//	public void setProductId(Integer productId) {
//		this.productId = productId;
//	}
//
//	public Seller getSeller() {
//		return seller;
//	}
//
//	public void setSeller(Seller seller) {
//		this.seller = seller;
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
//	public String getProductName() {
//		return productName;
//	}
//
//	public void setProductName(String productName) {
//		this.productName = productName;
//	}
//
//	public Double getProductQuantity() {
//		return productQuantity;
//	}
//
//	public void setProductQuantity(Double productQuantity) {
//		this.productQuantity = productQuantity;
//	}
//
//
//
//	public String getImageUrl() {
//		return ImageUrl;
//	}
//
//
//
//	public void setImageUrl(String imageUrl) {
//		ImageUrl = imageUrl;
//	}
//
//
//
//	public String getProductDescription() {
//		return productDescription;
//	}
//
//
//
//	public void setProductDescription(String productDescription) {
//		this.productDescription = productDescription;
//	}
//
//
//
//	public List<Order> getOrders() {
//		return orders;
//	}
//
//
//
//	public void setOrders(List<Order> orders) {
//		this.orders = orders;
//	}
//
//
//
//	public List<Rating> getRatings() {
//		return ratings;
//	}
//
//
//
//	public void setRatings(List<Rating> ratings) {
//		this.ratings = ratings;
//	}
//
//
//
//	public String getProductCategory() {
//		return productCategory;
//	}
//
//
//
//	public void setProductCategory(String productCategory) {
//		this.productCategory = productCategory;
//	}
//
//
//	
//
//	public Double getProductRatingValue() {
//		return productRatingValue;
//	}
//
//
//
//	public void setProductRatingValue() {
//		this.productRatingValue = 0.0;
//	}
//	public void setProductRatingValue(Double productRatingValue) {
//		this.productRatingValue = productRatingValue;
//	}
//
//	
//
//
//	public Integer getProductRatingCount() {
//		return productRatingCount;
//	}
//
//
//
//	public void setProductRatingCount(Integer productRatingCount) {
//		this.productRatingCount = productRatingCount;
//	}
//	public void setProductRatingCount() {
//		this.productRatingCount = 0;
//	}
//
//
//
//	@Override
//	public String toString() {
//		return "Product [productId=" + productId + ", seller=" + seller + ", productPrice=" + productPrice
//				+ ", productName=" + productName + ", ImageUrl=" + ImageUrl + ", productDescription="
//				+ productDescription + ", productQuantity=" + productQuantity + ", productCategory=" + productCategory
//				+ ", productRatingValue=" + productRatingValue + ", productRatingCount=" + productRatingCount
//				+ ", orders=" + orders + ", ratings=" + ratings + "]";
//	}




	



	


	 

	
	
	

	

}
