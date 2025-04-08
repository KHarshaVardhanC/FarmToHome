package com.ftohbackend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftohbackend.exception.RatingException;
import com.ftohbackend.model.Rating;
import com.ftohbackend.repository.RatingRepository;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    RatingRepository ratingRepository;

    @Override
    public Rating addRating(Rating rating)throws RatingException {
    	 if (rating == null) {
             throw new RatingException("Rating object cannot be null");
         }
         if (rating.getCustomerId() == null || rating.getProductId() == null || rating.getRating() == null) {
             throw new RatingException("Customer ID, Product ID, and Rating value must not be null");
         }
    	rating.setCreatedAt(LocalDateTime.now());
        rating.setUpdatedAt(LocalDateTime.now());
        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getAllRatings() throws RatingException{
    	 List<Rating> ratings = ratingRepository.findAll();
         if (ratings.isEmpty()) {
             throw new RatingException("No ratings found");
         }
         return ratings;    }

    @Override
    public List<Rating> getRatingsByProductId(Integer productId) throws RatingException{
    	 if (productId == null) {
             throw new RatingException("Product ID cannot be null");
         }
         List<Rating> ratings = ratingRepository.findByProductId(productId);
         if (ratings.isEmpty()) {
             throw new RatingException("No ratings found for product ID: " + productId);
         }
         return ratings;
         }

    @Override
    public List<Rating> getRatingsByCustomerId(Integer customerId)throws RatingException {
        if (customerId == null) {
            throw new RatingException("Customer ID cannot be null");
        }
        List<Rating> ratings = ratingRepository.findByCustomerId(customerId);
        if (ratings.isEmpty()) {
            throw new RatingException("No ratings found for customer ID: " + customerId);
        }
        return ratings;    }

    @Override
    public Rating getRatingById(Integer ratingId)throws RatingException {
    	  if (ratingId == null) {
              throw new RatingException("Rating ID cannot be null");
          }
          Optional<Rating> optionalRating = ratingRepository.findById(ratingId);
          if (optionalRating.isEmpty()) {
              throw new RatingException("Rating not found with ID: " + ratingId);
          }
          return optionalRating.get();    }

    @Override
    public void deleteRating(Integer ratingId)throws RatingException {
    	if (ratingId == null) {
            throw new RatingException("Rating ID cannot be null");
        }
        if (!ratingRepository.existsById(ratingId)) {
            throw new RatingException("Cannot delete, rating not found with ID: " + ratingId);
        }
    	ratingRepository.deleteById(ratingId);
    }
}
