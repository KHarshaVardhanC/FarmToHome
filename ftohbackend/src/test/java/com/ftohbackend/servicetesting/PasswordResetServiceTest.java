package com.ftohbackend.servicetesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ftohbackend.model.Customer;
import com.ftohbackend.model.Seller;
import com.ftohbackend.service.PasswordResetService;

class PasswordResetServiceTest {

    private PasswordResetService passwordResetService;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordResetService = new PasswordResetService();
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void updatePassword_shouldUpdateCustomerPassword() {
        // Arrange
        Customer customer = new Customer();
        String oldPassword = "oldPassword";
        customer.setCustomerPassword(oldPassword);
        String newPassword = "newPassword123";

        // Act
        passwordResetService.updatePassword(customer, newPassword);

        // Assert
        String updatedPassword = customer.getCustomerPassword();
        assertNotEquals(oldPassword, updatedPassword);
        assertNotEquals(newPassword, updatedPassword); // Should be encoded, not plaintext
        assertTrue(passwordEncoder.matches(newPassword, updatedPassword));
    }

    @Test
    void updatePassword_shouldUpdateSellerPassword() {
        // Arrange
        Seller seller = new Seller();
        String oldPassword = "oldPassword";
        seller.setSellerPassword(oldPassword);
        String newPassword = "newPassword123";

        // Act
        passwordResetService.updatePassword(seller, newPassword);

        // Assert
        String updatedPassword = seller.getSellerPassword();
        assertNotEquals(oldPassword, updatedPassword);
        assertNotEquals(newPassword, updatedPassword); // Should be encoded, not plaintext
        assertTrue(passwordEncoder.matches(newPassword, updatedPassword));
    }

    @Test
    void updatePassword_shouldThrowExceptionForUnsupportedUserType() {
        // Arrange
        Object unsupportedUser = new Object();
        String newPassword = "newPassword123";

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            passwordResetService.updatePassword(unsupportedUser, newPassword);
        });
        
        assertEquals("Unsupported user type", exception.getMessage());
    }
}
