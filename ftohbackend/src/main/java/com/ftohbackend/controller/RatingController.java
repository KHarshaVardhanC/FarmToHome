package com.ftohbackend.controller;

import com.ftohbackend.dto.ProductRatingDTO;
import com.ftohbackend.dto.RatingDTO;
import java.util.List;

public interface RatingController{
	
	public String addRating(RatingDTO ratingDTO) throws Exception;
	
	public List<RatingDTO> getAllRatings() throws Exception;
	
	public RatingDTO getRatingById(Integer ratingId) throws Exception;
	
	public List<ProductRatingDTO> getRatingsByProductId(Integer productId) throws Exception;
	
	public List<RatingDTO> getRatingsByCustomerId(Integer customerId) throws Exception;

    public String deleteRating(Integer ratingId) throws Exception;
}


