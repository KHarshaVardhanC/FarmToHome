package com.ftohbackend.repository;

import com.ftohbackend.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {
    List<Rating> findByProductId(Integer productId);

    List<Rating> findByCustomerId(Integer customerId);
}
