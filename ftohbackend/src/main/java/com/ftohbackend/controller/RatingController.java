package com.ftohbackend.controller;

import com.ftohbackend.dto.RatingDTO;
import java.util.List;

public interface RatingController{
	
	public RatingDTO addRating(RatingDTO ratingDTO);
	
	public List<RatingDTO> getAllRatings();
	
	public RatingDTO getRatingById(Integer ratingId);
	
	public List<RatingDTO> getRatingsByProductId(Integer productId);
	
	public List<RatingDTO> getRatingsByCustomerId(Integer customerId);

    public String deleteRating(Integer ratingId);
}


