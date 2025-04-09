package com.ftohbackend.controller;

import java.util.List;

import com.ftohbackend.dto.ProductRatingDTO;
import com.ftohbackend.dto.RatingDTO;
import com.ftohbackend.exception.RatingException;

public interface RatingController{
	
	public String addRating(RatingDTO ratingDTO) throws RatingException;
	
	public List<RatingDTO> getAllRatings() throws RatingException;
	
	public RatingDTO getRatingById(Integer ratingId) throws RatingException;
	
	public List<ProductRatingDTO> getRatingsByProductId(Integer productId) throws RatingException;
	
	public List<RatingDTO> getRatingsByCustomerId(Integer customerId) throws RatingException;

    public String deleteRating(Integer ratingId) throws RatingException;
}


