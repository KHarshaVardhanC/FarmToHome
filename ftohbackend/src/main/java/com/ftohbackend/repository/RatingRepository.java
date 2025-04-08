package com.ftohbackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ftohbackend.exception.RatingException;
import com.ftohbackend.model.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {
    List<Rating> findByProductId(Integer productId) throws RatingException;

    List<Rating> findByCustomerId(Integer customerId) throws RatingException;
}
