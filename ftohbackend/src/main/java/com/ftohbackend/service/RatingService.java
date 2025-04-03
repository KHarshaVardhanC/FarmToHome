package com.ftohbackend.service;

import com.ftohbackend.model.Rating;

import java.util.List;

public interface RatingService {
    Rating addRating(Rating rating);

    List<Rating> getAllRatings();

    List<Rating> getRatingsByProductId(Integer productId);

    List<Rating> getRatingsByCustomerId(Integer customerId);

    Rating getRatingById(Integer ratingId);

    void deleteRating(Integer ratingId);
}
