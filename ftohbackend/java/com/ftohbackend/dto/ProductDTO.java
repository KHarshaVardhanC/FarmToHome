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


}
