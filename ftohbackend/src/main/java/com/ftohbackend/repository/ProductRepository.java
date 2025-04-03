package com.ftohbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ftohbackend.model.allProductModel;



public interface allProductRepository extends JpaRepository<allProductModel,Integer>{

	public allProductModel getProductByTitle(String name);

}
