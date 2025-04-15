package com.ftohbackend.servicetesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ftohbackend.exception.ProductException;
import com.ftohbackend.exception.RatingException;
import com.ftohbackend.model.Customer;
import com.ftohbackend.model.Order;
import com.ftohbackend.model.Product;
import com.ftohbackend.model.Rating;
import com.ftohbackend.model.Seller;
import com.ftohbackend.repository.RatingRepository;
import com.ftohbackend.service.ProductService;
import com.ftohbackend.service.RatingServiceImpl;

class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private RatingServiceImpl ratingService;

    private Rating sampleRating;
    private Product sampleProduct;
    private Customer sampleCustomer;
    private Order sampleOrder;
    private Seller sampleSeller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up sample seller
        sampleSeller = new Seller();
        sampleSeller.setSellerId(1);
        sampleSeller.setSellerFirstName("John");
        sampleSeller.setSellerLastName("Doe");

        // Set up sample product
        sampleProduct = new Product();
        sampleProduct.setProductId(1);
        sampleProduct.setProductName("Apple");
        sampleProduct.setProductPrice(2.99);
        sampleProduct.setProductQuantity(100.0);
        sampleProduct.setProductDescription("Fresh Apples");
        sampleProduct.setSeller(sampleSeller);
        sampleProduct.setProductRatingValue(0.0);
        sampleProduct.setProductRatingCount(0);

        // Set up sample customer
        sampleCustomer = new Customer();
        sampleCustomer.setCustomerId(1);
        sampleCustomer.setCustomerFirstName("Jane");
        sampleCustomer.setCustomerLastName("Smith");

        // Set up sample order
        sampleOrder = new Order();
        sampleOrder.setOrderId(1);
        sampleOrder.setProduct(sampleProduct);
        sampleOrder.setCustomer(sampleCustomer);
        sampleOrder.setOrderQuantity(5.0);

        // Set up sample rating
        sampleRating = new Rating();
        sampleRating.setRatingId(1);
        sampleRating.setRatingValue(4.5);
        sampleRating.setFeedback("Great product!");
        sampleRating.setProduct(sampleProduct);
        sampleRating.setCustomer(sampleCustomer);
        sampleRating.setOrder(sampleOrder);
    }

    @Test
    void testAddRating_Success() throws Exception {
        // Arrange
        when(productService.updateProduct(anyInt(), any(Product.class))).thenReturn("Product updated successfully");
        when(ratingRepository.save(any(Rating.class))).thenReturn(sampleRating);

        // Act
        String result = ratingService.addRating(sampleRating);

        // Assert
        assertEquals("Rating Added Successfully", result);
        verify(productService, times(1)).updateProduct(eq(1), any(Product.class));
        verify(ratingRepository, times(1)).save(sampleRating);
    }

    @Test
    void testAddRating_NullRating() {
        // Act & Assert
        assertThrows(RatingException.class, () -> ratingService.addRating(null));
    }

    @Test
    void testAddRating_ProductUpdateFails() throws Exception {
        // Arrange
        when(productService.updateProduct(anyInt(), any(Product.class))).thenThrow(new ProductException("Product update failed"));

        // Act & Assert
        assertThrows(ProductException.class, () -> ratingService.addRating(sampleRating));
        verify(ratingRepository, never()).save(any(Rating.class));
    }

    @Test
    void testGetAllRatings_Success() throws RatingException {
        // Arrange
        List<Rating> ratings = new ArrayList<>();
        ratings.add(sampleRating);
        when(ratingRepository.findAll()).thenReturn(ratings);

        // Act
        List<Rating> result = ratingService.getAllRatings();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleRating.getRatingId(), result.get(0).getRatingId());
    }

    @Test
    void testGetAllRatings_EmptyList() {
        // Arrange
        when(ratingRepository.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(RatingException.class, () -> ratingService.getAllRatings());
    }

    @Test
    void testGetRatingsByProductId_Success() throws RatingException {
        // Arrange
        List<Rating> ratings = new ArrayList<>();
        ratings.add(sampleRating);
        when(ratingRepository.findByProductProductId(1)).thenReturn(ratings);

        // Act
        List<Rating> result = ratingService.getRatingsByProductId(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleRating.getRatingId(), result.get(0).getRatingId());
    }

    @Test
    void testGetRatingsByProductId_NullProductId() {
        // Act & Assert
        assertThrows(RatingException.class, () -> ratingService.getRatingsByProductId(null));
    }

    @Test
    void testGetRatingsByProductId_EmptyList() throws RatingException {
        // Arrange
        when(ratingRepository.findByProductProductId(anyInt())).thenReturn(new ArrayList<>());

        // Act
        List<Rating> result = ratingService.getRatingsByProductId(1);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetRatingsByCustomerId_Success() throws RatingException {
        // Arrange
        List<Rating> ratings = new ArrayList<>();
        ratings.add(sampleRating);
        when(ratingRepository.findByCustomerCustomerId(1)).thenReturn(ratings);

        // Act
        List<Rating> result = ratingService.getRatingsByCustomerId(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleRating.getRatingId(), result.get(0).getRatingId());
    }

    @Test
    void testGetRatingsByCustomerId_NullCustomerId() {
        // Act & Assert
        assertThrows(RatingException.class, () -> ratingService.getRatingsByCustomerId(null));
    }

    @Test
    void testGetRatingsByCustomerId_EmptyList() throws RatingException {
        // Arrange
        when(ratingRepository.findByCustomerCustomerId(anyInt())).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(RatingException.class, () -> ratingService.getRatingsByCustomerId(1));
    }

    @Test
    void testGetRatingByOrderId_RatingExists() {
        // Arrange
        List<Rating> ratings = new ArrayList<>();
        ratings.add(sampleRating);
        when(ratingRepository.findByOrderOrderId(1)).thenReturn(ratings);

        // Act
        Boolean result = ratingService.getRatingByOrderId(1);

        // Assert
        assertTrue(result);
    }

    @Test
    void testGetRatingByOrderId_RatingDoesNotExist() {
        // Arrange
        when(ratingRepository.findByOrderOrderId(anyInt())).thenReturn(new ArrayList<>());

        // Act
        Boolean result = ratingService.getRatingByOrderId(1);

        // Assert
        assertFalse(result);
    }

    @Test
    void testGetRatingById_Success() throws RatingException {
        // Arrange
        when(ratingRepository.findById(1)).thenReturn(Optional.of(sampleRating));

        // Act
        Rating result = ratingService.getRatingById(1);

        // Assert
        assertNotNull(result);
        assertEquals(sampleRating.getRatingId(), result.getRatingId());
    }

    @Test
    void testGetRatingById_NullRatingId() {
        // Act & Assert
        assertThrows(RatingException.class, () -> ratingService.getRatingById(null));
    }

    @Test
    void testGetRatingById_RatingNotFound() {
        // Arrange
        when(ratingRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RatingException.class, () -> ratingService.getRatingById(1));
    }

    @Test
    void testDeleteRating_Success() throws RatingException {
        // Arrange
        when(ratingRepository.existsById(1)).thenReturn(true);
        doNothing().when(ratingRepository).deleteById(1);

        // Act
        ratingService.deleteRating(1);

        // Assert
        verify(ratingRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteRating_NullRatingId() {
        // Act & Assert
        assertThrows(RatingException.class, () -> ratingService.deleteRating(null));
    }

    @Test
    void testDeleteRating_RatingNotFound() {
        // Arrange
        when(ratingRepository.existsById(anyInt())).thenReturn(false);

        // Act & Assert
        assertThrows(RatingException.class, () -> ratingService.deleteRating(1));
        verify(ratingRepository, never()).deleteById(anyInt());
    }
}