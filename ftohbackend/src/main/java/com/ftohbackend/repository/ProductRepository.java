package com.ftohbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ftohbackend.model.ProductModel;


@Repository
public interface ProductRepository extends JpaRepository<ProductModel,Integer>{


}
