package com.ftohbackend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftohbackend.dto.ProductRatingDTO;
import com.ftohbackend.dto.RatingDTO;
import com.ftohbackend.exception.RatingException;
import com.ftohbackend.model.Order;
import com.ftohbackend.model.Rating;
import com.ftohbackend.service.CustomerService;
import com.ftohbackend.service.OrderService;
import com.ftohbackend.service.RatingService;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/rating")
public class RatingControllerImpl implements RatingController {
	
	@Autowired
	private RatingService ratingService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired 
	OrderService orderService;
	
	@Override
	@PostMapping("")
	public String addRating(@RequestBody RatingDTO ratingDTO) throws Exception {
		
		
		
		
		
		Rating rating =modelMapper.map(ratingDTO, Rating.class);
		Order order=orderService.getOrderById(ratingDTO.getOrderId());
		rating.setCustomer(order.getCustomer());
		rating.setProduct(order.getProduct());
		
		System.out.println(rating.getCustomer().getCustomerFirstName());
		System.out.println(rating.getProduct().getProductName());
		
		return ratingService.addRating(rating);
		
	}
	
	@Override
	@GetMapping("")
	public List<RatingDTO> getAllRatings() throws RatingException {
		return ratingService.getAllRatings().stream().map(rating -> modelMapper.map(rating, RatingDTO.class)).collect(Collectors.toList());
				
	}
	
	@Override
	@GetMapping("/{ratingId}")
	public RatingDTO getRatingById(@PathVariable Integer ratingId) throws RatingException {
		Rating rating = ratingService.getRatingById(ratingId);
		return modelMapper.map(rating, RatingDTO.class);
		
	}
	
	
	
	
	
	@Override
	@GetMapping("/product/{productId}")
	public List<ProductRatingDTO> getRatingsByProductId(@PathVariable Integer productId) throws RatingException{
		
		List<Rating> ratings=ratingService.getRatingsByProductId(productId);
		List<ProductRatingDTO> productratings=new ArrayList<>();
		
		for(Rating rating: ratings)
		{
			ProductRatingDTO productratingdto=new ProductRatingDTO();
			
			productratingdto.setRatingId(rating.getRatingId());
			productratingdto.setProductName(rating.getProduct().getProductName());
			productratingdto.setFeedback(rating.getFeedback());
			productratingdto.setRatingValue(rating.getRatingValue());
			productratingdto.setCreatedAt(rating.getCreatedAt());
			productratingdto.setImageUrl(rating.getProduct().getImageUrl());
			
			productratings.add(productratingdto);
		}
		System.out.println(productratings.size());
		
		
		return productratings;
	}
	
	@Override
	@GetMapping("/customer/{customerId}")
	public List<RatingDTO> getRatingsByCustomerId(@PathVariable Integer customerId) throws RatingException {
		return ratingService.getRatingsByCustomerId(customerId).stream().map(r -> modelMapper.map(r, RatingDTO.class)).collect(Collectors.toList());
		
	}
	
	@Override
	@DeleteMapping("/{ratingId}")
	public String deleteRating(@PathVariable Integer ratingId) throws RatingException {
		ratingService.deleteRating(ratingId);
		return "Rating deleted successfully";
	}
	
}
