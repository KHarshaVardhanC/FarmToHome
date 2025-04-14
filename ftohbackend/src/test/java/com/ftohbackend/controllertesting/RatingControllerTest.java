package com.ftohbackend.controllertesting;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftohbackend.controller.CustomerControllerImpl;
import com.ftohbackend.controller.RatingControllerImpl;
import com.ftohbackend.dto.ProductRatingDTO;
import com.ftohbackend.dto.RatingDTO;
import com.ftohbackend.exception.RatingException;
import com.ftohbackend.model.Product;
import com.ftohbackend.model.Rating;
import com.ftohbackend.service.CustomerService;
import com.ftohbackend.service.RatingService;

@WebMvcTest
@ContextConfiguration(classes = { RatingControllerImpl.class })//@AutoConfigureMockMvc

public class RatingControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingService ratingService;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private ModelMapper modelMapper;

    private Rating rating;
    private RatingDTO ratingDTO;
    private Product product;
    private ProductRatingDTO productRatingDTO;

    @BeforeEach
    public void setUp() {
        // Set up the Product
        product = new Product();
        product.setProductId(1);
        product.setProductName("Test Product");
        product.setImageUrl("test-image-url.jpg");
        
        // Set up the Rating
        rating = new Rating();
        rating.setRatingId(1);
//        rating.setCustomerId(1);
//        rating.setProductId(1);
        rating.setRatingValue(4.5);
        rating.setFeedback("Great product!");
        rating.setProduct(product);
        rating.setCreatedAt(LocalDateTime.now());
        
        // Set up the RatingDTO
        ratingDTO = new RatingDTO();
        ratingDTO.setRatingId(1);
        ratingDTO.setCustomerId(1);
        ratingDTO.setProductId(1);
        ratingDTO.setRatingValue(4.5);
        ratingDTO.setFeedback("Great product!");
        
        // Set up the ProductRatingDTO
        productRatingDTO = new ProductRatingDTO();
        productRatingDTO.setRatingId(1);
        productRatingDTO.setProductName("Test Product");
        productRatingDTO.setRatingValue(4.5);
        productRatingDTO.setFeedback("Great product!");
        productRatingDTO.setImageUrl("test-image-url.jpg");
        productRatingDTO.setCreatedAt(LocalDateTime.now());
    }
    
    @AfterEach
    public void tearDown() {
        rating = null;
        ratingDTO = null;
        product = null;
        productRatingDTO = null;
    }

    @Test
    @DisplayName("JUnit test for addRating operation")
    public void givenRatingDTO_whenAddRating_thenReturnSuccessMessage() throws Exception {
        // given - precondition or setup
        given(modelMapper.map(any(RatingDTO.class), any())).willReturn(rating);
        given(ratingService.addRating(any(Rating.class))).willReturn("Rating added successfully");
        
        // when - action or the behavior
        ResultActions response = mockMvc.perform(post("/rating")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ratingDTO)));
                
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Rating added successfully"));
    }
    
    @Test
    @DisplayName("JUnit test for getAllRatings operation")
    public void givenRatingList_whenGetAllRatings_thenReturnRatingDTOList() throws Exception {
        // given - precondition or setup
        List<Rating> ratings = new ArrayList<>();
        ratings.add(rating);
        
        given(ratingService.getAllRatings()).willReturn(ratings);
        given(modelMapper.map(any(Rating.class), any())).willReturn(ratingDTO);
        
        // when - action or the behavior
        ResultActions response = mockMvc.perform(get("/rating"));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(ratings.size())));
    }
    
    @Test
    @DisplayName("JUnit test for getRatingById operation")
    public void givenRatingId_whenGetRatingById_thenReturnRatingDTO() throws Exception {
        // given - precondition or setup
        given(ratingService.getRatingById(anyInt())).willReturn(rating);
        given(modelMapper.map(any(Rating.class), any())).willReturn(ratingDTO);
        
        // when - action or the behavior
        ResultActions response = mockMvc.perform(get("/rating/{ratingId}", 1));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ratingId", is(ratingDTO.getRatingId())))
                .andExpect(jsonPath("$.customerId", is(ratingDTO.getCustomerId())))
                .andExpect(jsonPath("$.productId", is(ratingDTO.getProductId())))
                .andExpect(jsonPath("$.ratingValue", is(ratingDTO.getRatingValue())))
                .andExpect(jsonPath("$.feedback", is(ratingDTO.getFeedback())));
    }
    
    @Test
    @DisplayName("JUnit test for getRatingById operation - RatingException")
    public void givenInvalidRatingId_whenGetRatingById_thenThrowsRatingException() throws Exception {
        // given - precondition or setup
        given(ratingService.getRatingById(anyInt())).willThrow(RatingException.class);
        
        // when - action or the behavior
        ResultActions response = mockMvc.perform(get("/rating/{ratingId}", 999));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isInternalServerError());
    }
    
    @Test
    @DisplayName("JUnit test for getRatingsByProductId operation")
    public void givenProductId_whenGetRatingsByProductId_thenReturnProductRatingDTOList() throws Exception {
        // given - precondition or setup
        List<Rating> ratings = new ArrayList<>();
        ratings.add(rating);
        
        given(ratingService.getRatingsByProductId(anyInt())).willReturn(ratings);
        
        // when - action or the behavior
        ResultActions response = mockMvc.perform(get("/rating/product/{productId}", 1));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(ratings.size())))
                .andExpect(jsonPath("$[0].ratingId", is(productRatingDTO.getRatingId())))
                .andExpect(jsonPath("$[0].productName", is(productRatingDTO.getProductName())))
                .andExpect(jsonPath("$[0].ratingValue", is(productRatingDTO.getRatingValue())))
                .andExpect(jsonPath("$[0].feedback", is(productRatingDTO.getFeedback())))
                .andExpect(jsonPath("$[0].imageUrl", is(productRatingDTO.getImageUrl())));
    }
    
    @Test
    @DisplayName("JUnit test for getRatingsByProductId operation - RatingException")
    public void givenInvalidProductId_whenGetRatingsByProductId_thenThrowsRatingException() throws Exception {
        // given - precondition or setup
        given(ratingService.getRatingsByProductId(anyInt())).willThrow(RatingException.class);
        
        // when - action or the behavior
        ResultActions response = mockMvc.perform(get("/rating/product/{productId}", 999));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isInternalServerError());
    }
    
    @Test
    @DisplayName("JUnit test for getRatingsByCustomerId operation")
    public void givenCustomerId_whenGetRatingsByCustomerId_thenReturnRatingDTOList() throws Exception {
        // given - precondition or setup
        List<Rating> ratings = new ArrayList<>();
        ratings.add(rating);
        
        given(ratingService.getRatingsByCustomerId(anyInt())).willReturn(ratings);
        given(modelMapper.map(any(Rating.class), any())).willReturn(ratingDTO);
        
        // when - action or the behavior
        ResultActions response = mockMvc.perform(get("/rating/customer/{customerId}", 1));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(ratings.size())));
    }
    
    @Test
    @DisplayName("JUnit test for getRatingsByCustomerId operation - RatingException")
    public void givenInvalidCustomerId_whenGetRatingsByCustomerId_thenThrowsRatingException() throws Exception {
        // given - precondition or setup
        given(ratingService.getRatingsByCustomerId(anyInt())).willThrow(RatingException.class);
        
        // when - action or the behavior
        ResultActions response = mockMvc.perform(get("/rating/customer/{customerId}", 999));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isInternalServerError());
    }
    
    @Test
    @DisplayName("JUnit test for deleteRating operation")
    public void givenRatingId_whenDeleteRating_thenReturnSuccessMessage() throws Exception {
        // given - precondition or setup
        willDoNothing().given(ratingService).deleteRating(anyInt());
        
        // when - action or the behavior
        ResultActions response = mockMvc.perform(delete("/rating/{ratingId}", 1));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Rating deleted successfully"));
    }
    
    @Test
    @DisplayName("JUnit test for deleteRating operation - RatingException")
    public void givenInvalidRatingId_whenDeleteRating_thenThrowsRatingException() throws Exception {
        // given - precondition or setup
        willThrow(RatingException.class).given(ratingService).deleteRating(anyInt());
        
        // when - action or the behavior
        ResultActions response = mockMvc.perform(delete("/rating/{ratingId}", 999));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isInternalServerError());
    }
}