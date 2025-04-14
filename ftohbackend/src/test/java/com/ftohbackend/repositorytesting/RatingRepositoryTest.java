//package com.ftohbackend.repositorytesting;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.time.LocalDateTime;
//import java.time.Month;
//import java.util.List;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import com.ftohbackend.exception.RatingException;
//import com.ftohbackend.model.Customer;
//import com.ftohbackend.model.Product;
//import com.ftohbackend.model.Rating;
//import com.ftohbackend.model.Seller;
//import com.ftohbackend.repository.CustomerRepository;
//import com.ftohbackend.repository.ProductRepository;
//import com.ftohbackend.repository.RatingRepository;
//import com.ftohbackend.repository.SellerRepository;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class RatingRepositoryTest {
//
//    @Autowired
//    private RatingRepository ratingRepository;
//    
//    @Autowired
//    private CustomerRepository customerRepository;
//    
//    @Autowired
//    private ProductRepository productRepository;
//    
//    @Autowired
//    private SellerRepository sellerRepository;
//    
//    private Customer testCustomer;
//    private Product testProduct;
//    private Seller testSeller;
//    private Rating testRating;
//    
//    @BeforeEach
//    public void setUp() {
//        // Create and save a test customer
//        testCustomer = new Customer();
//        testCustomer.setCustomerFirstName("Test");
//        testCustomer.setCustomerLastName("User");
//        testCustomer.setCustomerEmail("test.user@example.com");
//        testCustomer.setCustomerPassword("password123");
//        testCustomer.setCustomerPhoneNumber("9876543210");
//        testCustomer.setCustomerCity("City");
//        testCustomer.setCustomerPlace("Place");
//        testCustomer.setCustomerPincode("123456");
//        testCustomer.setCustomerState("State");
//        testCustomer.setCustomerIsActive(true);
//        customerRepository.save(testCustomer);
//        
//        // Create and save a test seller
//        testSeller = new Seller();
//        testSeller.setSellerFirstName("John");
//        testSeller.setSellerLastName("Doe");
//        testSeller.setSellerEmail("john.doe@example.com");
//        testSeller.setSellerPassword("securepass");
//        testSeller.setSellerMobileNumber("9876543210");
//        testSeller.setSellerCity("City");
//        testSeller.setSellerPlace("Place");
//        testSeller.setSellerState("State");
//        testSeller.setSellerPincode("123456");
//        testSeller.setSellerStatus("Active");
//        sellerRepository.save(testSeller);
//        
//        // Create and save a test product
//        testProduct = new Product();
//        testProduct.setProductName("Laptop");
//        testProduct.setProductDescription("Gaming laptop");
//        testProduct.setProductPrice(1500.0);
//        testProduct.setProductQuantity(10.0);
//        testProduct.setImageUrl("http://example.com/laptop.jpg");
//        testProduct.setSeller(testSeller);
//        productRepository.save(testProduct);
//        
//        // Create and save a test rating
//        testRating = new Rating();
//        testRating.setProduct(testProduct);
//        testRating.setCustomer(testCustomer);
//        testRating.setRatingValue(4.0);
//        testRating.setFeedback("Good product, fast delivery");
//        ratingRepository.save(testRating);
//    }
//    
//    @AfterEach
//    public void tearDown() {
//        ratingRepository.deleteAll();
//        productRepository.deleteAll();
//        customerRepository.deleteAll();
//        sellerRepository.deleteAll();
//    }
//    
//    @Test
//    @DisplayName("Save rating test")
//    public void testSaveRating() {
//        Rating rating = new Rating();
//        rating.setProduct(testProduct);
//        rating.setCustomer(testCustomer);
//        rating.setRatingValue(5.0);
//        rating.setFeedback("Excellent product, highly recommended");
//        
//        Rating savedRating = ratingRepository.save(rating);
//        
//        assertThat(savedRating).isNotNull();
//        assertThat(savedRating.getRatingId()).isNotNull();
//        assertThat(savedRating.getRatingValue()).isEqualTo(5.0);
//        assertThat(savedRating.getFeedback()).isEqualTo("Excellent product, highly recommended");
//        assertThat(savedRating.getCreatedAt()).isNotNull();
//    }
//    
//    @Test
//    @DisplayName("Find rating by ID test")
//    public void testFindById() {
//        Rating found = ratingRepository.findById(testRating.getRatingId()).orElse(null);
//        
//        assertThat(found).isNotNull();
//        assertThat(found.getRatingValue()).isEqualTo(4.0);
//        assertThat(found.getFeedback()).isEqualTo("Good product, fast delivery");
//    }
//    
//    @Test
//    @DisplayName("Find ratings by product ID test")
//    public void testFindByProductProductId() throws RatingException {
//        // Add another rating for the same product
//        Rating anotherRating = new Rating();
//        anotherRating.setProduct(testProduct);
//        anotherRating.setCustomer(testCustomer);
//        anotherRating.setRatingValue(3.0);
//        anotherRating.setFeedback("Average product");
//        ratingRepository.save(anotherRating);
//        
//        List<Rating> ratings = ratingRepository.findByProductProductId(testProduct.getProductId());
//        
//        assertThat(ratings).hasSize(2);
//        assertThat(ratings).extracting(Rating::getRatingValue).containsExactlyInAnyOrder(4.0, 3.0);
//        assertThat(ratings).extracting(Rating::getFeedback).containsExactlyInAnyOrder("Good product, fast delivery", "Average product");
//    }
//    
//    @Test
//    @DisplayName("Find ratings by customer ID test")
//    public void testFindByCustomerCustomerId() throws RatingException {
//        // Create another product for testing
//        Product anotherProduct = new Product();
//        anotherProduct.setProductName("Headphones");
//        anotherProduct.setProductDescription("Wireless headphones");
//        anotherProduct.setProductPrice(199.99);
//        anotherProduct.setProductQuantity(30.0);
//        anotherProduct.setImageUrl("http://example.com/headphones.jpg");
//        anotherProduct.setSeller(testSeller);
//        productRepository.save(anotherProduct);
//        
//        // Add rating for the new product by the same customer
//        Rating newRating = new Rating();
//        newRating.setProduct(anotherProduct);
//        newRating.setCustomer(testCustomer);
//        newRating.setRatingValue(5.0);
//        newRating.setFeedback("Amazing headphones");
//        ratingRepository.save(newRating);
//        
//        List<Rating> ratings = ratingRepository.findByCustomerCustomerId(testCustomer.getCustomerId());
//        
//        assertThat(ratings).hasSize(2);
//        assertThat(ratings).extracting(Rating::getFeedback)
//                          .containsExactlyInAnyOrder("Good product, fast delivery", "Amazing headphones");
//    }
//    
//    @Test
//    @DisplayName("Delete rating test")
//    public void testDeleteRating() {
//        ratingRepository.deleteById(testRating.getRatingId());
//        
//        assertThat(ratingRepository.findById(testRating.getRatingId())).isEmpty();
//    }
//    
//    @Test
//    @DisplayName("Find all ratings test")
//    public void testFindAllRatings() {
//        // Add another rating
//        Rating anotherRating = new Rating();
//        anotherRating.setProduct(testProduct);
//        anotherRating.setCustomer(testCustomer);
//        anotherRating.setRatingValue(2.0);
//        anotherRating.setFeedback("Not satisfied with the product");
//        ratingRepository.save(anotherRating);
//        
//        List<Rating> allRatings = ratingRepository.findAll();
//        
//        assertThat(allRatings).isNotNull();
//        assertThat(allRatings).hasSize(2);
//    }
//    
//    @Test
//    @DisplayName("Update rating test")
//    public void testUpdateRating() {
//        Rating ratingToUpdate = ratingRepository.findById(testRating.getRatingId()).orElse(null);
//        assertThat(ratingToUpdate).isNotNull();
//        
//        ratingToUpdate.setRatingValue(5.0);
//        ratingToUpdate.setFeedback("Updated feedback: This product is excellent!");
//        
//        Rating updatedRating = ratingRepository.save(ratingToUpdate);
//        
//        assertThat(updatedRating.getRatingValue()).isEqualTo(5.0);
//        assertThat(updatedRating.getFeedback()).isEqualTo("Updated feedback: This product is excellent!");
//        assertThat(updatedRating.getRatingId()).isEqualTo(testRating.getRatingId());
//    }
//    
//    @Test
//    @DisplayName("Test rating creation timestamp")
//    public void testRatingCreationTimestamp() {
//        Rating rating = new Rating();
//        rating.setProduct(testProduct);
//        rating.setCustomer(testCustomer);
//        rating.setRatingValue(4.0);
//        rating.setFeedback("Testing creation timestamp");
//        
//        Rating savedRating = ratingRepository.save(rating);
//        
//        assertThat(savedRating.getCreatedAt()).isNotNull();
//        assertThat(savedRating.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
//    }
//    
//    @Test
//    @DisplayName("Test rating constructor with parameters")
//    public void testRatingConstructorWithParameters() {
//        LocalDateTime testDateTime = LocalDateTime.of(2019, Month.MARCH, 28, 14, 33);
//        Rating rating = new Rating(null, testCustomer, testProduct, "Great product!", 4.0, testDateTime);
//        Rating savedRating = ratingRepository.save(rating);
//        
//        assertThat(savedRating).isNotNull();
//        assertThat(savedRating.getRatingId()).isNotNull();
//        assertThat(savedRating.getRatingValue()).isEqualTo(4.0);
//        assertThat(savedRating.getFeedback()).isEqualTo("Great product!");
//        assertThat(savedRating.getCustomer()).isEqualTo(testCustomer);
//        assertThat(savedRating.getProduct()).isEqualTo(testProduct);
//        assertThat(savedRating.getCreatedAt()).isNotNull();
//    }
//}
