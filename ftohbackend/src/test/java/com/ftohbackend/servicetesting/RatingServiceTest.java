package com.ftohbackend.servicetesting;

package com.ftohbackend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ftohbackend.exception.RatingException;
import com.ftohbackend.model.Customer;
import com.ftohbackend.model.Product;
import com.ftohbackend.model.Rating;
import com.ftohbackend.repository.RatingRepository;
import com.ftohbackend.service.RatingServiceImpl;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingServiceImpl ratingService;

    private Rating rating;
    private Customer customer;
    private Product product;

    @BeforeEach
    public void setUp() {
        customer = new Customer();
        customer.setCustomerId(1);
        customer.setCustomerName("Test Customer");

        product = new Product();
        product.setProductId(1);
        product.setProductName("Test Product");

        rating = new Rating();
        rating.setRatingId(1);
        rating.setCustomer(customer);
        rating.setProduct(product);
        rating.setRatingValue(5);
        rating.setFeedback("Great product!");
    }

    @AfterEach
    public void tearDown() {
        rating = null;
        customer = null;
        product = null;
    }

    @Test
    @DisplayName("JUnit test for addRating operation")
    public void givenRatingObject_whenAddRating_thenReturnSuccessMessage() throws RatingException {
        // given - precondition or setup
        given(ratingRepository.save(any(Rating.class))).willReturn(rating);

        // when - action or behavior
        String result = ratingService.addRating(rating);

        // then - verify the output
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo("Rating Added Successfully");
        verify(ratingRepository, times(1)).save(rating);
    }

    @Test
    @DisplayName("JUnit test for addRating operation - with null Rating")
    public void givenNullRating_whenAddRating_thenThrowsRatingException() {
        // given - precondition or setup
        Rating nullRating = null;

        // when - action or behavior & then - verify the output
        assertThrows(RatingException.class, () -> {
            ratingService.addRating(nullRating);
        });

        verify(ratingRepository, never()).save(any(Rating.class));
    }

    @Test
    @DisplayName("JUnit test for getAllRatings operation")
    public void givenRatingsList_whenGetAllRatings_thenReturnRatingsList() throws RatingException {
        // given - precondition or setup
        Rating rating2 = new Rating();
        rating2.setRatingId(2);
        rating2.setRatingValue(4);
        rating2.setFeedback("Good product");

        given(ratingRepository.findAll()).willReturn(List.of(rating, rating2));

        // when - action or behavior
        List<Rating> ratings = ratingService.getAllRatings();

        // then - verify the output
        assertThat(ratings).isNotNull();
        assertThat(ratings.size()).isEqualTo(2);
        verify(ratingRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("JUnit test for getAllRatings operation - Empty List")
    public void givenEmptyRatingsList_whenGetAllRatings_thenThrowsRatingException() {
        // given - precondition or setup
        given(ratingRepository.findAll()).willReturn(Collections.emptyList());

        // when - action or behavior & then - verify the output
        assertThrows(RatingException.class, () -> {
            ratingService.getAllRatings();
        });

        verify(ratingRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("JUnit test for getRatingsByProductId operation")
    public void givenProductId_whenGetRatingsByProductId_thenReturnRatingsList() throws RatingException {
        // given - precondition or setup
        Rating rating2 = new Rating();
        rating2.setRatingId(2);
        rating2.setProduct(product);
        rating2.setRatingValue(4);

        given(ratingRepository.findByProductProductId(anyInt())).willReturn(List.of(rating, rating2));

        // when - action or behavior
        List<Rating> ratings = ratingService.getRatingsByProductId(1);

        // then - verify the output
        assertThat(ratings).isNotNull();
        assertThat(ratings.size()).isEqualTo(2);
        verify(ratingRepository, times(1)).findByProductProductId(1);
    }

    @Test
    @DisplayName("JUnit test for getRatingsByProductId operation - Empty List")
    public void givenProductId_whenGetRatingsByProductIdWithNoResults_thenThrowsRatingException() {
        // given - precondition or setup
        given(ratingRepository.findByProductProductId(anyInt())).willReturn(Collections.emptyList());

        // when - action or behavior & then - verify the output
        assertThrows(RatingException.class, () -> {
            ratingService.getRatingsByProductId(1);
        });

        verify(ratingRepository, times(1)).findByProductProductId(1);
    }

    @Test
    @DisplayName("JUnit test for getRatingsByProductId operation - Null ProductId")
    public void givenNullProductId_whenGetRatingsByProductId_thenThrowsRatingException() {
        // when - action or behavior & then - verify the output
        assertThrows(RatingException.class, () -> {
            ratingService.getRatingsByProductId(null);
        });

        verify(ratingRepository, never()).findByProductProductId(anyInt());
    }

    @Test
    @DisplayName("JUnit test for getRatingsByCustomerId operation")
    public void givenCustomerId_whenGetRatingsByCustomerId_thenReturnRatingsList() throws RatingException {
        // given - precondition or setup
        Rating rating2 = new Rating();
        rating2.setRatingId(2);
        rating2.setCustomer(customer);
        rating2.setRatingValue(3);

        given(ratingRepository.findByCustomerCustomerId(anyInt())).willReturn(List.of(rating, rating2));

        // when - action or behavior
        List<Rating> ratings = ratingService.getRatingsByCustomerId(1);

        // then - verify the output
        assertThat(ratings).isNotNull();
        assertThat(ratings.size()).isEqualTo(2);
        verify(ratingRepository, times(1)).findByCustomerCustomerId(1);
    }

    @Test
    @DisplayName("JUnit test for getRatingsByCustomerId operation - Empty List")
    public void givenCustomerId_whenGetRatingsByCustomerIdWithNoResults_thenThrowsRatingException() {
        // given - precondition or setup
        given(ratingRepository.findByCustomerCustomerId(anyInt())).willReturn(Collections.emptyList());

        // when - action or behavior & then - verify the output
        assertThrows(RatingException.class, () -> {
            ratingService.getRatingsByCustomerId(1);
        });

        verify(ratingRepository, times(1)).findByCustomerCustomerId(1);
    }

    @Test
    @DisplayName("JUnit test for getRatingsByCustomerId operation - Null CustomerId")
    public void givenNullCustomerId_whenGetRatingsByCustomerId_thenThrowsRatingException() {
        // when - action or behavior & then - verify the output
        assertThrows(RatingException.class, () -> {
            ratingService.getRatingsByCustomerId(null);
        });

        verify(ratingRepository, never()).findByCustomerCustomerId(anyInt());
    }

    @Test
    @DisplayName("JUnit test for getRatingById operation")
    public void givenRatingId_whenGetRatingById_thenReturnRating() throws RatingException {
        // given - precondition or setup
        given(ratingRepository.findById(anyInt())).willReturn(Optional.of(rating));

        // when - action or behavior
        Rating foundRating = ratingService.getRatingById(1);

        // then - verify the output
        assertThat(foundRating).isNotNull();
        assertThat(foundRating.getRatingId()).isEqualTo(1);
        verify(ratingRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("JUnit test for getRatingById operation - Rating Not Found")
    public void givenInvalidRatingId_whenGetRatingById_thenThrowsRatingException() {
        // given - precondition or setup
        given(ratingRepository.findById(anyInt())).willReturn(Optional.empty());

        // when - action or behavior & then - verify the output
        assertThrows(RatingException.class, () -> {
            ratingService.getRatingById(999);
        });

        verify(ratingRepository, times(1)).findById(999);
    }

    @Test
    @DisplayName("JUnit test for getRatingById operation - Null RatingId")
    public void givenNullRatingId_whenGetRatingById_thenThrowsRatingException() {
        // when - action or behavior & then - verify the output
        assertThrows(RatingException.class, () -> {
            ratingService.getRatingById(null);
        });

        verify(ratingRepository, never()).findById(anyInt());
    }

    @Test
    @DisplayName("JUnit test for deleteRating operation")
    public void givenRatingId_whenDeleteRating_thenDeleteSuccessfully() throws RatingException {
        // given - precondition or setup
        given(ratingRepository.existsById(anyInt())).willReturn(true);
        willDoNothing().given(ratingRepository).deleteById(anyInt());

        // when - action or behavior
        ratingService.deleteRating(1);

        // then - verify the output
        verify(ratingRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("JUnit test for deleteRating operation - Rating Not Found")
    public void givenInvalidRatingId_whenDeleteRating_thenThrowsRatingException() {
        // given - precondition or setup
        given(ratingRepository.existsById(anyInt())).willReturn(false);

        // when - action or behavior & then - verify the output
        assertThrows(RatingException.class, () -> {
            ratingService.deleteRating(999);
        });

        verify(ratingRepository, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("JUnit test for deleteRating operation - Null RatingId")
    public void givenNullRatingId_whenDeleteRating_thenThrowsRatingException() {
        // when - action or behavior & then - verify the output
        assertThrows(RatingException.class, () -> {
            ratingService.deleteRating(null);
        });

        verify(ratingRepository, never()).existsById(anyInt());
        verify(ratingRepository, never()).deleteById(anyInt());
    }
}