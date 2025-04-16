package com.ftohbackend.servicetesting;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ftohbackend.service.OtpService;

class OtpServiceTest {

    private OtpService otpService;
    
    @BeforeEach
    void setUp() {
        otpService = new OtpService();
    }
    
    @Test
    void generateOtp_shouldReturnFourDigitOtp() {
        // Act
        String email = "test@example.com";
        String otp = otpService.generateOtp(email);
        
        // Assert
        assertNotNull(otp);
        assertEquals(4, otp.length());
        assertTrue(otp.matches("\\d{4}"));
    }
    
    @Test
    void generateOtp_shouldStoreOtpInMap() throws Exception {
        // Arrange
        String email = "test@example.com";
        
        // Act
        String otp = otpService.generateOtp(email);
        
        // Get access to the private otpMap
        Field otpMapField = OtpService.class.getDeclaredField("otpMap");
        otpMapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, String> otpMap = (Map<String, String>) otpMapField.get(otpService);
        
        // Assert
        assertTrue(otpMap.containsKey(email));
        assertEquals(otp, otpMap.get(email));
    }
    
    @Test
    void validateOtp_shouldReturnTrueForValidOtp() {
        // Arrange
        String email = "test@example.com";
        String otp = otpService.generateOtp(email);
        
        // Act
        boolean isValid = otpService.validateOtp(email, otp);
        
        // Assert
        assertTrue(isValid);
    }
    
    @Test
    void validateOtp_shouldReturnFalseForInvalidOtp() {
        // Arrange
        String email = "test@example.com";
        otpService.generateOtp(email);
        String invalidOtp = "9999";
        
        // Act
        boolean isValid = otpService.validateOtp(email, invalidOtp);
        
        // Assert
        assertFalse(isValid);
    }
    
    @Test
    void validateOtp_shouldReturnFalseForNonExistentEmail() {
        // Arrange
        String email = "nonexistent@example.com";
        String otp = "1234";
        
        // Act
        boolean isValid = otpService.validateOtp(email, otp);
        
        // Assert
        assertFalse(isValid);
    }
    
    @Test
    void clearOtp_shouldRemoveOtpFromMap() throws Exception {
        // Arrange
        String email = "test@example.com";
        otpService.generateOtp(email);
        
        // Get access to the private otpMap
        Field otpMapField = OtpService.class.getDeclaredField("otpMap");
        otpMapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, String> otpMap = (Map<String, String>) otpMapField.get(otpService);
        
        // Verify that the email is in the map before clearing
        assertTrue(otpMap.containsKey(email));
        
        // Act
        otpService.clearOtp(email);
        
        // Assert
        assertFalse(otpMap.containsKey(email));
    }
    
    @Test
    void clearOtp_shouldNotThrowExceptionForNonExistentEmail() {
        // Arrange
        String email = "nonexistent@example.com";
        
        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> otpService.clearOtp(email));
    }
}
