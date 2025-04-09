package com.ftohbackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ftohbackend.exception.ProductException;
import com.ftohbackend.model.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product,Integer>{


	List<Product> findBySellerSellerId(Integer sellerId) throws ProductException;
    
    // Method to search products by name with seller details
    @Query("SELECT p FROM Product p JOIN FETCH p.seller WHERE p.productName LIKE %:name%")
    List<Product> findProductsByNameWithSeller(@Param("name") String name) throws ProductException;
    
    // Alternative method to search products with partial matching
    List<Product> findByProductNameContainingIgnoreCase(String name) throws ProductException;


    
    
}
