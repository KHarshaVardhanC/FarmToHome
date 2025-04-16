package com.ftohbackend.controllertesting;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ftohbackend.controller.RatingControllerImpl;
import com.ftohbackend.dto.RatingDTO;
import com.ftohbackend.exception.RatingException;
import com.ftohbackend.model.Customer;
import com.ftohbackend.model.Order;
import com.ftohbackend.model.Product;
import com.ftohbackend.model.Rating;
import com.ftohbackend.service.CustomerService;
import com.ftohbackend.service.OrderService;
import com.ftohbackend.service.RatingService;

public class RatingControllerTest1 {

    private MockMvc mockMvc;
    
    @Mock
    private RatingService ratingService;
    
    @Mock
    private CustomerService customerService;
    
    @Mock
    private OrderService orderService;
    
    @Mock
    private ModelMapper modelMapper;
    
    @InjectMocks
    private RatingControllerImpl ratingController;
    
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ratingController).build();
        
        // Configure ObjectMapper for LocalDateTime serialization
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }
    
    @Test
    void testAddRating() throws Exception {
        // Arrange
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setRatingId(1);
       
        ratingDTO.setOrderId(1);
        ratingDTO.setRatingValue(4.5);
        ratingDTO.setFeedback("Great product!");
        
        Rating rating = new Rating();
        rating.setRatingId(1);
        rating.setRatingValue(4.5);
        rating.setFeedback("Great product!");
        
        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setCustomerFirstName("John");
        customer.setCustomerLastName("Doe");
        
        Product product = new Product();
        product.setProductId(1);
        product.setProductName("Test Product");
        
        Order order = new Order();
        order.setOrderId(1);
        order.setCustomer(customer);
        order.setProduct(product);
        
        rating.setCustomer(customer);
        rating.setProduct(product);
        
        when(modelMapper.map(ratingDTO, Rating.class)).thenReturn(rating);
        when(orderService.getOrderById(1)).thenReturn(order);
        when(ratingService.addRating(any(Rating.class))).thenReturn("Rating added successfully");
        
        // Act & Assert
        mockMvc.perform(post("/rating")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ratingDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Rating added successfully"));
    }
    
    @Test
    void testGetAllRatings() throws Exception {
        // Arrange
        List<Rating> ratings = new ArrayList<>();
        Rating rating1 = new Rating();
        rating1.setRatingId(1);
        rating1.setRatingValue(4.5);
        rating1.setFeedback("Great product!");
        
        Rating rating2 = new Rating();
        rating2.setRatingId(2);
        rating2.setRatingValue(3.5);
        rating2.setFeedback("Good product");
        
        ratings.add(rating1);
        ratings.add(rating2);
        
        RatingDTO ratingDTO1 = new RatingDTO();
        ratingDTO1.setRatingId(1);
        ratingDTO1.setRatingValue(4.5);
        ratingDTO1.setFeedback("Great product!");
        
        RatingDTO ratingDTO2 = new RatingDTO();
        ratingDTO2.setRatingId(2);
        ratingDTO2.setRatingValue(3.5);
        ratingDTO2.setFeedback("Good product");
        
        when(ratingService.getAllRatings()).thenReturn(ratings);
        when(modelMapper.map(rating1, RatingDTO.class)).thenReturn(ratingDTO1);
        when(modelMapper.map(rating2, RatingDTO.class)).thenReturn(ratingDTO2);
        
        // Act & Assert
        mockMvc.perform(get("/rating"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ratingId").value(1))
                .andExpect(jsonPath("$[0].ratingValue").value(4.5))
                .andExpect(jsonPath("$[0].feedback").value("Great product!"))
                .andExpect(jsonPath("$[1].ratingId").value(2))
                .andExpect(jsonPath("$[1].ratingValue").value(3.5))
                .andExpect(jsonPath("$[1].feedback").value("Good product"));
    }
    
    @Test
    void testGetRatingById() throws Exception {
        // Arrange
        Integer ratingId = 1;
        Rating rating = new Rating();
        rating.setRatingId(ratingId);
        rating.setRatingValue(4.5);
        rating.setFeedback("Great product!");
        
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setRatingId(ratingId);
        ratingDTO.setRatingValue(4.5);
        ratingDTO.setFeedback("Great product!");
        
        when(ratingService.getRatingById(ratingId)).thenReturn(rating);
        when(modelMapper.map(rating, RatingDTO.class)).thenReturn(ratingDTO);
        
        // Act & Assert
        mockMvc.perform(get("/rating/{ratingId}", ratingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ratingId").value(ratingId))
                .andExpect(jsonPath("$.ratingValue").value(4.5))
                .andExpect(jsonPath("$.feedback").value("Great product!"));
    }
    
    @Test
    void testGetRatingsByProductId() throws Exception {
        // Arrange
        Integer productId = 1;
        List<Rating> ratings = new ArrayList<>();
        
        Rating rating1 = new Rating();
        rating1.setRatingId(1);
        rating1.setRatingValue(4.5);
        rating1.setFeedback("Great product!");
        rating1.setCreatedAt(LocalDateTime.now());
        
        Product product = new Product();
        product.setProductId(productId);
        product.setProductName("Test Product");
        product.setImageUrl("test-image.jpg");
        
        rating1.setProduct(product);
        ratings.add(rating1);
        
        when(ratingService.getRatingsByProductId(productId)).thenReturn(ratings);
        
        // Act & Assert
        mockMvc.perform(get("/rating/product/{productId}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ratingId").value(1))
                .andExpect(jsonPath("$[0].ratingValue").value(4.5))
                .andExpect(jsonPath("$[0].feedback").value("Great product!"))
                .andExpect(jsonPath("$[0].productName").value("Test Product"))
                .andExpect(jsonPath("$[0].imageUrl").value("test-image.jpg"));
    }
    
    @Test
    void testGetRatingsByCustomerId() throws Exception {
        // Arrange
        Integer customerId = 1;
        List<Rating> ratings = new ArrayList<>();
        
        Rating rating1 = new Rating();
        rating1.setRatingId(1);
        rating1.setRatingValue(4.5);
        rating1.setFeedback("Great product!");
        
        Rating rating2 = new Rating();
        rating2.setRatingId(2);
        rating2.setRatingValue(3.5);
        rating2.setFeedback("Good product");
        
        ratings.add(rating1);
        ratings.add(rating2);
        
        RatingDTO ratingDTO1 = new RatingDTO();
        ratingDTO1.setRatingId(1);
        ratingDTO1.setRatingValue(4.5);
        ratingDTO1.setFeedback("Great product!");
        
        RatingDTO ratingDTO2 = new RatingDTO();
        ratingDTO2.setRatingId(2);
        ratingDTO2.setRatingValue(3.5);
        ratingDTO2.setFeedback("Good product");
        
        when(ratingService.getRatingsByCustomerId(customerId)).thenReturn(ratings);
        when(modelMapper.map(rating1, RatingDTO.class)).thenReturn(ratingDTO1);
        when(modelMapper.map(rating2, RatingDTO.class)).thenReturn(ratingDTO2);
        
        // Act & Assert
        mockMvc.perform(get("/rating/customer/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ratingId").value(1))
                .andExpect(jsonPath("$[0].ratingValue").value(4.5))
                .andExpect(jsonPath("$[0].feedback").value("Great product!"))
                .andExpect(jsonPath("$[1].ratingId").value(2))
                .andExpect(jsonPath("$[1].ratingValue").value(3.5))
                .andExpect(jsonPath("$[1].feedback").value("Good product"));
    }
    
    @Test
    void testDeleteRating() throws Exception {
        // Arrange
        Integer ratingId = 1;
        
        // Act & Assert
        mockMvc.perform(delete("/rating/{ratingId}", ratingId))
                .andExpect(status().isOk())
                .andExpect(content().string("Rating deleted successfully"));
        
        verify(ratingService, times(1)).deleteRating(ratingId);
    }
    
    @Test
    void testGetRatingById_RatingNotFoundException() throws Exception {
        // Arrange
        Integer ratingId = 1;
        when(ratingService.getRatingById(ratingId)).thenThrow(new RatingException("Rating not found"));
        
        // Act & Assert
        mockMvc.perform(get("/rating/{ratingId}", ratingId))
                .andExpect(status().isInternalServerError());
    }
    
    @Test
    void testGetAllRatings_NoRatingsFound() throws Exception {
        // Arrange
        when(ratingService.getAllRatings()).thenThrow(new RatingException("No ratings found"));
        
        // Act & Assert
        mockMvc.perform(get("/rating"))
                .andExpect(status().isInternalServerError());
    }
    
    @Test
    void testAddRating_Exception() throws Exception {
        // Arrange
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setRatingId(1);
      
        ratingDTO.setOrderId(1);
        ratingDTO.setRatingValue(4.5);
        ratingDTO.setFeedback("Great product!");
        
        Rating rating = new Rating();
        rating.setRatingId(1);
        
        when(modelMapper.map(ratingDTO, Rating.class)).thenReturn(rating);
        when(orderService.getOrderById(1)).thenThrow(new Exception("Order not found"));
        
        // Act & Assert
        mockMvc.perform(post("/rating")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ratingDTO)))
                .andExpect(status().isInternalServerError());
    }
}
