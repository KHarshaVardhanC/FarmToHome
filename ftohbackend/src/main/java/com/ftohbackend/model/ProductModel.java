package com.ftohbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

    @Column(name = "sellerid")
    String sellerid;

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

    public ProductModel(Integer productid, String sellerid, Double productprice, String productname,
                        Double productquantity, String sellerarea, String sellerplace) {
        super();
        this.productid = productid;
        this.sellerid = sellerid;
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

    public String getsellerid() {
        return sellerid;
    }

    public void setsellerid(String sellerid) {
        this.sellerid = sellerid;
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

    @Override
    public String toString() {
        return "ProductModel [productid=" + productid + ", sellerid=" + sellerid + ", productprice="
                + productprice + ", productname=" + productname + ", productquantity=" + productquantity
                + ", sellerarea=" + sellerarea + ", sellerplace=" + sellerplace + "]";
    }
}
