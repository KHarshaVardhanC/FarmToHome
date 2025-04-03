package com.ftohbackend.service;

import com.ftohbackend.model.Rating;
import com.ftohbackend.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    RatingRepository ratingRepository;

    @Override
    public Rating addRating(Rating rating) {
        rating.setCreatedAt(LocalDateTime.now());
        rating.setUpdatedAt(LocalDateTime.now());
        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    @Override
    public List<Rating> getRatingsByProductId(Integer productId) {
        return ratingRepository.findByProductId(productId);
    }

    @Override
    public List<Rating> getRatingsByCustomerId(Integer customerId) {
        return ratingRepository.findByCustomerId(customerId);
    }

    @Override
    public Rating getRatingById(Integer ratingId) {
        return ratingRepository.findById(ratingId).orElse(null);
    }

    @Override
    public void deleteRating(Integer ratingId) {
        ratingRepository.deleteById(ratingId);
    }
}
