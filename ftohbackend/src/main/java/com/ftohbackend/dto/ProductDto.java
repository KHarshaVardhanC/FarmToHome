package com.ftohbackend.dto;

public class ProductDto {
	Integer productid;
	String sellerid;
	Double productprice;
	String productname;
	Double productquantity;
	String sellerplace;
	String sellerarea;

	public ProductDto(Integer productid, String sellerid, Double productprice, String productname,
			Double productquantity, String sellerplace, String sellerarea) {
		super();
		this.productid = productid;
		this.sellerid = sellerid;
		this.productprice = productprice;
		this.productname = productname;
		this.productquantity = productquantity;
		this.sellerplace = sellerplace;
		this.sellerarea = sellerarea;

	}

	public String getsellerarea() {
		return sellerarea;
	}
	public void setsellerarea(String sellerarea) {
		this.sellerarea = sellerarea;
	}
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
	public String getsellerplace() {
		return sellerplace;
	}
	public void setsellerplace(String sellerplace) {
		this.sellerplace = sellerplace;
	}
	public ProductDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "ProductDto [productid=" + productid + ", sellerid=" + sellerid + ", productprice=" + productprice
				+ ", productname=" + productname + ", productquantity=" + productquantity + ", sellerplace="
				+ sellerplace + ", sellerarea=" + sellerarea + "]";
	}


}
