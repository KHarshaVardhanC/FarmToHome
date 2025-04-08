package com.ftohbackend.service;

import java.util.List;

import com.ftohbackend.exception.RatingException;
import com.ftohbackend.model.Rating;

public interface RatingService {
    String addRating(Rating rating) throws RatingException;

    List<Rating> getAllRatings() throws RatingException;

    List<Rating> getRatingsByProductId(Integer productId) throws RatingException;

    List<Rating> getRatingsByCustomerId(Integer customerId) throws RatingException;

    Rating getRatingById(Integer ratingId) throws RatingException;

    void deleteRating(Integer ratingId) throws RatingException;
}
