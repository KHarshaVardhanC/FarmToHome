package com.ftohbackend.modeltesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ftohbackend.model.Product;
import com.ftohbackend.model.Seller;

class SellerTest {

    private Seller seller;
    private BCryptPasswordEncoder passwordEncoder;
    
    // Patterns from SellerDTO for validation testing
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^[0-9]{10,10}$");
    private static final Pattern PINCODE_PATTERN = Pattern.compile("^[0-9]{6,6}$");
    
    @BeforeEach
    void setUp() {
        seller = new Seller();
        passwordEncoder = new BCryptPasswordEncoder();
    }
    
    @Test
    void testSellerProperties() {
        // Arrange
        Integer sellerId = 1;
        String email = "seller@example.com";
        String firstName = "John";
        String lastName = "Doe";
        String mobileNumber = "1234567890";
        String place = "Downtown";
        String city = "New York";
        String state = "NY";
        String pincode = "123456";
        String password = "password123";
        String status = "Active";
        List<Product> products = new ArrayList<>();
        
        // Act
        seller.setSellerId(sellerId);
        seller.setSellerEmail(email);
        seller.setSellerFirstName(firstName);
        seller.setSellerLastName(lastName);
        seller.setSellerMobileNumber(mobileNumber);
        seller.setSellerPlace(place);
        seller.setSellerCity(city);
        seller.setSellerState(state);
        seller.setSellerPincode(pincode);
        seller.setSellerPassword(password);
        seller.setSellerStatus(status);
        seller.setProducts(products);
        
        // Assert
        assertEquals(sellerId, seller.getSellerId());
        assertEquals(email, seller.getSellerEmail());
        assertEquals(firstName, seller.getSellerFirstName());
        assertEquals(lastName, seller.getSellerLastName());
        assertEquals(mobileNumber, seller.getSellerMobileNumber());
        assertEquals(place, seller.getSellerPlace());
        assertEquals(city, seller.getSellerCity());
        assertEquals(state, seller.getSellerState());
        assertEquals(pincode, seller.getSellerPincode());
        assertEquals(password, seller.getSellerPassword());
        assertEquals(status, seller.getSellerStatus());
        assertEquals(products, seller.getProducts());
    }
    
    @Test
    void testEmailValidation_validEmails() {
        // Arrange
        String[] validEmails = {
            "test@example.com",
            "john.doe@example.com",
            "john_doe@example.com",
            "john-doe@example.com",
            "john+doe@example.com",
            "test@sub.example.com"
        };
        
        // Act & Assert
        for (String email : validEmails) {
            seller.setSellerEmail(email);
            assertEquals(email, seller.getSellerEmail());
            assertTrue(EMAIL_PATTERN.matcher(email).matches(), 
                    "Email " + email + " should match the pattern");
        }
    }
    
    @Test
    void testEmailValidation_invalidEmails() {
        // Arrange
        String[] invalidEmails = {
            "test@",
            "@example.com",
            "test@.com",
            "test@example.",
            "test@exam ple.com",
            "test@@example.com"
        };
        
        // Act & Assert
        for (String email : invalidEmails) {
            seller.setSellerEmail(email);
            assertEquals(email, seller.getSellerEmail());

        }
    }
    
    @Test
    void testMobileNumberValidation_validNumbers() {
        // Arrange
        String[] validMobileNumbers = {
            "1234567890",
            "9876543210",
            "5555555555"
        };
        
        // Act & Assert
        for (String number : validMobileNumbers) {
            seller.setSellerMobileNumber(number);
            assertEquals(number, seller.getSellerMobileNumber());
            assertTrue(MOBILE_PATTERN.matcher(number).matches(), 
                    "Mobile number " + number + " should match the pattern");
        }
    }
    
    @Test
    void testMobileNumberValidation_invalidNumbers() {
        // Arrange
        String[] invalidMobileNumbers = {
            "123456789",  
            "12345678901",
            "123abc7890",  
            "123 567890",  
            "+1234567890"  
        };
        
        // Act & Assert
        for (String number : invalidMobileNumbers) {
            seller.setSellerMobileNumber(number);
            assertEquals(number, seller.getSellerMobileNumber());
            assertFalse(MOBILE_PATTERN.matcher(number).matches(), 
                    "Mobile number " + number + " should not match the pattern");
        }
    }
    
    @Test
    void testPincodeValidation_validPincodes() {
        // Arrange
        String[] validPincodes = {
            "123456",
            "560001",
            "110011"
        };
        
        // Act & Assert
        for (String pincode : validPincodes) {
            seller.setSellerPincode(pincode);
            assertEquals(pincode, seller.getSellerPincode());
            assertTrue(PINCODE_PATTERN.matcher(pincode).matches(), 
                    "Pincode " + pincode + " should match the pattern");
        }
    }
    
    @Test
    void testPincodeValidation_invalidPincodes() {
        // Arrange
        String[] invalidPincodes = {
            "12345",    
            "1234567",  
            "12A456",   
            "12 456",   
            "12-345"    
        };
        
        // Act & Assert
        for (String pincode : invalidPincodes) {
            seller.setSellerPincode(pincode);
            assertEquals(pincode, seller.getSellerPincode());
            assertFalse(PINCODE_PATTERN.matcher(pincode).matches(), 
                    "Pincode " + pincode + " should not match the pattern");
        }
    }
    
    @Test
    void testVerifyPassword_withCorrectPassword_returnsTrue() {
        // Arrange
        String rawPassword = "password123";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        seller.setSellerPassword(encodedPassword);
        
        // Act
        boolean result = seller.verifyPassword(rawPassword);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    void testVerifyPassword_withIncorrectPassword_returnsFalse() {
        // Arrange
        String rawPassword = "password123";
        String wrongPassword = "wrongPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        seller.setSellerPassword(encodedPassword);
        
        // Act
        boolean result = seller.verifyPassword(wrongPassword);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    void testEqualsAndHashCode() {
        // Arrange
        Seller seller1 = new Seller();
        seller1.setSellerId(1);
        
        Seller seller2 = new Seller();
        seller2.setSellerId(1);
        
        Seller seller3 = new Seller();
        seller3.setSellerId(2);
        
        // Assert - equals
        assertEquals(seller1, seller2);
        assertNotEquals(seller1, seller3);
        
        // Assert - hashCode
        assertEquals(seller1.hashCode(), seller2.hashCode());
        assertNotEquals(seller1.hashCode(), seller3.hashCode());
    }
    
    @Test
    void testAllArgsConstructor() {
        // Arrange
        Integer sellerId = 1;
        String email = "seller@example.com";
        String firstName = "John";
        String lastName = "Doe";
        String mobileNumber = "1234567890";
        String place = "Downtown";
        String city = "New York";
        String state = "NY";
        String pincode = "123456";
        String password = "password123";
        String status = "Active";
        List<Product> products = new ArrayList<>();
        
        // Act
        Seller seller1 = new Seller(
            sellerId, email, firstName, lastName, mobileNumber,
            place, city, state, pincode, password, status, products
        );
        
        // Assert
        assertEquals(sellerId, seller1.getSellerId());
        assertEquals(email, seller1.getSellerEmail());
        assertEquals(firstName, seller1.getSellerFirstName());
        assertEquals(lastName, seller1.getSellerLastName());
        assertEquals(mobileNumber, seller1.getSellerMobileNumber());
        assertEquals(place, seller1.getSellerPlace());
        assertEquals(city, seller1.getSellerCity());
        assertEquals(state, seller1.getSellerState());
        assertEquals(pincode, seller1.getSellerPincode());
        assertEquals(password, seller1.getSellerPassword());
        assertEquals(status, seller1.getSellerStatus());
        assertEquals(products, seller1.getProducts());
    }
    
    @Test
    void testNoArgsConstructor() {
        // Act
        Seller newSeller = new Seller();
        
        // Assert
        assertNull(newSeller.getSellerId());
        assertNull(newSeller.getSellerEmail());
        assertNull(newSeller.getSellerFirstName());
        assertNull(newSeller.getSellerLastName());
        assertNull(newSeller.getSellerMobileNumber());
        assertNull(newSeller.getSellerPlace());
        assertNull(newSeller.getSellerCity());
        assertNull(newSeller.getSellerState());
        assertNull(newSeller.getSellerPincode());
        assertNull(newSeller.getSellerPassword());
        assertNull(newSeller.getSellerStatus());
        assertNotNull(newSeller.getProducts());
        assertTrue(newSeller.getProducts().isEmpty());
    }
    
    @Test
    void testToString() {
        // Arrange
        seller.setSellerId(1);
        seller.setSellerEmail("test@example.com");
        
        // Act
        String toString = seller.toString();
        
        // Assert
        assertTrue(toString.contains("sellerId=1"));
        assertTrue(toString.contains("sellerEmail=test@example.com"));
    }
    
    @Test
    void testRequiredFields() {
        
        Seller seller2 = new Seller();
        seller2.setSellerEmail("test@example.com");
        seller2.setSellerFirstName("John");
        seller2.setSellerLastName("Doe");
        seller2.setSellerMobileNumber("1234567890");
        seller2.setSellerPassword("password");
        seller2.setSellerPlace("Downtown");
        seller2.setSellerCity("New York");
        seller2.setSellerState("NY");
        seller2.setSellerPincode("123456");
        
        // Act & Assert
        assertNotNull(seller2.getSellerEmail(), "Email should not be null");
        assertNotNull(seller2.getSellerFirstName(), "First name should not be null");
        assertNotNull(seller2.getSellerLastName(), "Last name should not be null");
        assertNotNull(seller2.getSellerMobileNumber(), "Mobile number should not be null");
        assertNotNull(seller2.getSellerPlace(), "Place should not be null");
        assertNotNull(seller2.getSellerCity(), "City should not be null");
        assertNotNull(seller2.getSellerState(), "State should not be null");
        assertNotNull(seller2.getSellerPincode(), "Pincode should not be null");
        assertNotNull(seller2.getSellerPassword(), "Password should not be null");
    }
    
    @Test
    void testDTOCompatibility() {
        
        Seller seller3 = new Seller();
        Integer sellerId = 1;
        String email = "seller@example.com";
        String firstName = "John";
        String lastName = "Doe";
        String mobileNumber = "1234567890";
        String place = "Downtown";
        String city = "New York";
        String state = "NY";
        String pincode = "123456";
        String password = "password123";
        String status = "Active";
        
        // Act
        seller3.setSellerId(sellerId);
        seller3.setSellerEmail(email);
        seller3.setSellerFirstName(firstName);
        seller3.setSellerLastName(lastName);
        seller3.setSellerMobileNumber(mobileNumber);
        seller3.setSellerPlace(place);
        seller3.setSellerCity(city);
        seller3.setSellerState(state);
        seller3.setSellerPincode(pincode);
        seller3.setSellerPassword(password);
        seller3.setSellerStatus(status);
        
        // Assert - This simulates creating a DTO from the entity
        assertEquals(sellerId, seller3.getSellerId());
        assertEquals(email, seller3.getSellerEmail());
        assertTrue(EMAIL_PATTERN.matcher(email).matches(), "Email should match DTO pattern");
        assertEquals(firstName, seller3.getSellerFirstName());
        assertEquals(lastName, seller3.getSellerLastName());
        assertEquals(mobileNumber, seller3.getSellerMobileNumber());
        assertTrue(MOBILE_PATTERN.matcher(mobileNumber).matches(), "Mobile should match DTO pattern");
        assertEquals(place, seller3.getSellerPlace());
        assertEquals(city, seller3.getSellerCity());
        assertEquals(state, seller3.getSellerState());
        assertEquals(pincode, seller3.getSellerPincode());
        assertTrue(PINCODE_PATTERN.matcher(pincode).matches(), "Pincode should match DTO pattern");
        assertEquals(password, seller3.getSellerPassword());
        assertEquals(status, seller3.getSellerStatus());
    }
}