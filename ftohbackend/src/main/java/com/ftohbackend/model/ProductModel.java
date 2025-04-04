package com.ftohbackend.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "product")
public class ProductModel {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productid")
    Integer productid;
    
    @ManyToOne(fetch=FetchType.LAZY,cascade=CascadeType.ALL)
    @JoinColumn(name = "sellerId")
    Seller seller;

    @NotNull(message = "Product price cannot be null")
    @Positive(message = "Product price must be positive")
    @Column(name = "productprice")
    Double productprice;

    @NotBlank(message = "Product name cannot be blank")
    @Size(max = 100, message = "Product name cannot exceed 100 characters")
    @Column(name = "productname")
    String productname;

    @NotNull(message = "Product quantity cannot be null")
    @PositiveOrZero(message = "Product quantity must be zero or positive")
    @Column(name = "productquantity")
    Double productquantity;

    @NotBlank(message = "Seller area cannot be blank")
    @Column(name = "sellerarea")
    String sellerarea;

    @NotBlank(message = "Seller place cannot be blank")
    @Column(name = "sellerplace")
    String sellerplace;

    // Constructors
    public ProductModel() {
        super();
    }

  

    public ProductModel(Integer productid, Seller seller,
			@NotNull(message = "Product price cannot be null") @Positive(message = "Product price must be positive") Double productprice,
			@NotBlank(message = "Product name cannot be blank") @Size(max = 100, message = "Product name cannot exceed 100 characters") String productname,
			@NotNull(message = "Product quantity cannot be null") @PositiveOrZero(message = "Product quantity must be zero or positive") Double productquantity,
			@NotBlank(message = "Seller area cannot be blank") String sellerarea,
			@NotBlank(message = "Seller place cannot be blank") String sellerplace) {
		super();
		this.productid = productid;
		this.seller = seller;
		this.productprice = productprice;
		this.productname = productname;
		this.productquantity = productquantity;
		this.sellerarea = sellerarea;
		this.sellerplace = sellerplace;
	}



	// Getters and Setters
    public Integer getproductid() {
        return productid;
    }

    public void setproductid(Integer productid) {
        this.productid = productid;
    }

   

    public Double getproductprice() {
        return productprice;
    }

    public void setproductprice(Double productprice) {
        this.productprice = productprice;
    }

    public String getproductname() {
        return productname;
    }

    public void setproductname(String productname) {
        this.productname = productname;
    }

    public Double getproductquantity() {
        return productquantity;
    }

    public void setproductquantity(Double productquantity) {
        this.productquantity = productquantity;
    }

    public String getsellerarea() {
        return sellerarea;
    }

    public void setsellerarea(String sellerarea) {
        this.sellerarea = sellerarea;
    }

    public String getsellerplace() {
        return sellerplace;
    }

    public void setsellerplace(String sellerplace) {
        this.sellerplace = sellerplace;
    }

    public Seller getSeller() {
		return seller;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}
	

	@Override
	public String toString() {
		return "ProductModel [productid=" + productid + ", seller=" + seller + ", productprice=" + productprice
				+ ", productname=" + productname + ", productquantity=" + productquantity + ", sellerarea=" + sellerarea
				+ ", sellerplace=" + sellerplace + "]";
	}
}
