package com.ftohbackend.modeltesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ftohbackend.model.Customer;
import com.ftohbackend.model.Order;
import com.ftohbackend.model.Rating;

class CustomerTest {

    private Customer customer;
    private BCryptPasswordEncoder passwordEncoder;
    
    @BeforeEach
    void setUp() {
        customer = new Customer();
        passwordEncoder = new BCryptPasswordEncoder();
    }
    
    @Test
    void testCustomerProperties() {
        // Arrange
        Integer customerId = 1;
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String password = "password123";
        String place = "Downtown";
        String city = "New York";
        String pincode = "10001";
        String state = "NY";
        String phoneNumber = "1234567890";
        Boolean isActive = true;
        List<Order> orders = new ArrayList<>();
        List<Rating> ratings = new ArrayList<>();
        
        // Act
        customer.setCustomerId(customerId);
        customer.setCustomerFirstName(firstName);
        customer.setCustomerLastName(lastName);
        customer.setCustomerEmail(email);
        customer.setCustomerPassword(password);
        customer.setCustomerPlace(place);
        customer.setCustomerCity(city);
        customer.setCustomerPincode(pincode);
        customer.setCustomerState(state);
        customer.setCustomerPhoneNumber(phoneNumber);
        customer.setCustomerIsActive(isActive);
        customer.setOrders(orders);
        customer.setRatings(ratings);
        
        // Assert
        assertEquals(customerId, customer.getCustomerId());
        assertEquals(firstName, customer.getCustomerFirstName());
        assertEquals(lastName, customer.getCustomerLastName());
        assertEquals(email, customer.getCustomerEmail());
        assertEquals(password, customer.getCustomerPassword());
        assertEquals(place, customer.getCustomerPlace());
        assertEquals(city, customer.getCustomerCity());
        assertEquals(pincode, customer.getCustomerPincode());
        assertEquals(state, customer.getCustomerState());
        assertEquals(phoneNumber, customer.getCustomerPhoneNumber());
        assertEquals(isActive, customer.getCustomerIsActive());
        assertEquals(orders, customer.getOrders());
        assertEquals(ratings, customer.getRatings());
    }
    
    @Test
    void testVerifyPassword_withCorrectPassword_returnsTrue() {
        // Arrange
        String rawPassword = "password123";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        customer.setCustomerPassword(encodedPassword);
        
        // Act
        boolean result = customer.verifyPassword(rawPassword);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    void testVerifyPassword_withIncorrectPassword_returnsFalse() {
        // Arrange
        String rawPassword = "password123";
        String wrongPassword = "wrongPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        customer.setCustomerPassword(encodedPassword);
        
        // Act
        boolean result = customer.verifyPassword(wrongPassword);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    void testEqualsAndHashCode() {
        // Arrange
        Customer customer1 = new Customer();
        customer1.setCustomerId(1);
        
        Customer customer2 = new Customer();
        customer2.setCustomerId(1);
        
        Customer customer3 = new Customer();
        customer3.setCustomerId(2);
        
        // Assert - equals
        assertEquals(customer1, customer2);
        assertNotEquals(customer1, customer3);
        
        // Assert - hashCode
        assertEquals(customer1.hashCode(), customer2.hashCode());
        assertNotEquals(customer1.hashCode(), customer3.hashCode());
    }
    
    @Test
    void testAllArgsConstructor() {
        // Arrange
        Integer customerId = 1;
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String password = "password123";
        String place = "Downtown";
        String city = "New York";
        String pincode = "10001";
        String state = "NY";
        String phoneNumber = "1234567890";
        Boolean isActive = true;
        List<Order> orders = new ArrayList<>();
        List<Rating> ratings = new ArrayList<>();
        
        // Act
        Customer customer0 = new Customer(
            customerId, firstName, lastName, email, password,
            place, city, pincode, state, phoneNumber, isActive,
            orders, ratings
        );
        
        // Assert
        assertEquals(customerId, customer0.getCustomerId());
        assertEquals(firstName, customer0.getCustomerFirstName());
        assertEquals(lastName, customer0.getCustomerLastName());
        assertEquals(email, customer0.getCustomerEmail());
        assertEquals(password, customer0.getCustomerPassword());
        assertEquals(place, customer0.getCustomerPlace());
        assertEquals(city, customer0.getCustomerCity());
        assertEquals(pincode, customer0.getCustomerPincode());
        assertEquals(state, customer0.getCustomerState());
        assertEquals(phoneNumber, customer0.getCustomerPhoneNumber());
        assertEquals(isActive, customer0.getCustomerIsActive());
        assertEquals(orders, customer0.getOrders());
        assertEquals(ratings, customer0.getRatings());
    }
    
    @Test
    void testNoArgsConstructor() {
        // Act
        Customer newCustomer = new Customer();
        
        // Assert
        assertNull(newCustomer.getCustomerId());
        assertNull(newCustomer.getCustomerFirstName());
        assertNull(newCustomer.getCustomerLastName());
        assertNull(newCustomer.getCustomerEmail());
        assertNull(newCustomer.getCustomerPassword());
        assertNull(newCustomer.getCustomerPlace());
        assertNull(newCustomer.getCustomerCity());
        assertNull(newCustomer.getCustomerPincode());
        assertNull(newCustomer.getCustomerState());
        assertNull(newCustomer.getCustomerPhoneNumber());
        assertNull(newCustomer.getCustomerIsActive());
        assertNotNull(newCustomer.getOrders());
        assertNotNull(newCustomer.getRatings());
        assertTrue(newCustomer.getOrders().isEmpty());
        assertTrue(newCustomer.getRatings().isEmpty());
    }
    
    @Test
    void testToString() {
        // Arrange
        customer.setCustomerId(1);
        customer.setCustomerEmail("test@example.com");
        
        // Act
        String toString = customer.toString();
        
        // Assert
        assertTrue(toString.contains("customerId=1"));
        assertTrue(toString.contains("customerEmail=test@example.com"));
    }
}