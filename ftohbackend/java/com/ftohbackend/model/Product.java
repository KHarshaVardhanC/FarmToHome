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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
	
	Double productRatingValue;
	Integer productRatingCount;
	
	String productQuantityType;
	

	@OneToMany(mappedBy = "product" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<Order> orders=new ArrayList<>();
	
	
	@OneToMany(mappedBy = "product" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<Rating> ratings=new ArrayList<>();
	

}
