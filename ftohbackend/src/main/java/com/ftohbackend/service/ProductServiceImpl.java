package com.ftohbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftohbackend.model.ProductModel;
import com.ftohbackend.repository.ProductRepository;
import com.ftohbackend.repository.SellerRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService{

	@Autowired
	ProductRepository ProductRepo;
	
	@Autowired
	SellerRepository sellerRepository;


	@Override
	public String addProduct(ProductModel product) {
		ProductRepo.save(product);
		return("Added product");
	}

	@Override
	public List<ProductModel> getAllProduct(){
		return ProductRepo.findAll();
	}
	
	public List<ProductModel> getAllProductBySellerId(Integer sellerid){
		return ProductRepo.findAll().stream()
	            .filter(product -> product.getSeller().getSellerId().equals(sellerid))
	            .toList(); 
	}
	
	@Override
	public ProductModel getProductByTitle(String name) {
		List<ProductModel> allProducts=ProductRepo.findAll();
		ProductModel pro=new ProductModel();
		for(ProductModel prod: allProducts) {
			if(prod.getproductname().equalsIgnoreCase(name)) {
				return prod;
			}

		}
		return null;
	}
	@Override
	public String updateProduct(Integer productId, ProductModel updatedDetails) {
        ProductModel product = ProductRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        if (updatedDetails.getproductprice() != null) {
			product.setproductprice(updatedDetails.getproductprice());
		}
        if (updatedDetails.getproductname() != null) {
			product.setproductname(updatedDetails.getproductname());
		}
        if (updatedDetails.getproductquantity() != null) {
			product.setproductquantity(updatedDetails.getproductquantity());
		}


        ProductRepo.save(product);
        return "Product updated successfully";
    }

	@Override
	public String deleteProduct(Integer productId) {
		ProductRepo.deleteById(productId);
		return "Product is deleted";
	}
	






}
