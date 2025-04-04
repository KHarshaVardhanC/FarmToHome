package com.ftohbackend.controller;

import com.ftohbackend.dto.RatingDTO;
import com.ftohbackend.model.Rating;
import com.ftohbackend.service.RatingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rating")
public class RatingControllerImpl implements RatingController {
	
	@Autowired
	private RatingService ratingService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	@PostMapping("")
	public RatingDTO addRating(@RequestBody RatingDTO ratingDTO) {
		Rating rating = modelMapper.map(ratingDTO, Rating.class);
		Rating saved = ratingService.addRating(rating);
		
		return modelMapper.map(saved, RatingDTO.class);
	}
	
	@Override
	@GetMapping("")
	public List<RatingDTO> getAllRatings(){
		return ratingService.getAllRatings().stream().map(rating -> modelMapper.map(rating, RatingDTO.class)).collect(Collectors.toList());
				
	}
	
	@Override
	@GetMapping("/{ratingId}")
	public RatingDTO getRatingById(@PathVariable Integer ratingId) {
		Rating rating = ratingService.getRatingById(ratingId);
		return modelMapper.map(rating, RatingDTO.class);
		
	}
	
	@Override
	@GetMapping("/product/{productId}")
	public List<RatingDTO> getRatingsByProductId(@PathVariable Integer productId){
		return ratingService.getRatingsByProductId(productId).stream().map(r -> modelMapper.map(r, RatingDTO.class)).collect(Collectors.toList());
	}
	
	@Override
	@GetMapping("/customer/{customerId}")
	public List<RatingDTO> getRatingsByCustomerId(@PathVariable Integer customerId){
		return ratingService.getRatingsByCustomerId(customerId).stream().map(r -> modelMapper.map(r, RatingDTO.class)).collect(Collectors.toList());
		
	}
	
	@Override
	@DeleteMapping("/{ratingId}")
	public String deleteRating(@PathVariable Integer ratingId) {
		ratingService.deleteRating(ratingId);
		return "Rating deleted successfully";
	}
	
}
