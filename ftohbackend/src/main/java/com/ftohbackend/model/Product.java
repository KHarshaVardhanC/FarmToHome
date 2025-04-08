package com.ftohbackend.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

@Entity
@Table(name = "product")
public class Product {

//    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
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
	
	

	@OneToMany(mappedBy = "product" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<Order> orders=new ArrayList<>();
	
	
	@OneToMany(mappedBy = "product" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<Rating> ratings=new ArrayList<>();
	
//	@NotNull(message = "Product Description cannot be null")
//	@Column(name = "productDescription")
//	String productDescription;

//    @NotBlank(message = "Seller place cannot be blank")
//    @Column(name = "sellerPlace")
//    String sellerPlace;

	// Constructors
	public Product() {
		super();
	}



	public Product(Integer productId, Seller seller,
		@NotNull(message = "Product price cannot be null") @Positive(message = "Product price must be positive") Double productPrice,
		@NotBlank(message = "Product name cannot be blank") @Size(max = 100, message = "Product name cannot exceed 100 characters") String productName,
		String imageUrl, String productDescription,
		@NotNull(message = "Product quantity cannot be null") @PositiveOrZero(message = "Product quantity must be zero or positive") Double productQuantity,
		List<Order> orders, List<Rating> ratings) {
	super();
	this.productId = productId;
	this.seller = seller;
	this.productPrice = productPrice;
	this.productName = productName;
	ImageUrl = imageUrl;
	this.productDescription = productDescription;
	this.productQuantity = productQuantity;
	this.orders = orders;
	this.ratings = ratings;
}



	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Seller getSeller() {
		return seller;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
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



	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}



	public List<Order> getOrders() {
		return orders;
	}



	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}



	public List<Rating> getRatings() {
		return ratings;
	}



	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}



	@Override
	public String toString() {
		return "Product [productId=" + productId + ", seller=" + seller + ", productPrice=" + productPrice
				+ ", productName=" + productName + ", ImageUrl=" + ImageUrl + ", productDescription="
				+ productDescription + ", productQuantity=" + productQuantity + ", orders=" + orders + ", ratings="
				+ ratings + "]";
	}
	
	

	

}
