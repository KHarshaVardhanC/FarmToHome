package com.ftohbackend.controllertesting;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftohbackend.controller.ForgotPasswordController;
import com.ftohbackend.dto.MailBody;
import com.ftohbackend.dto.PasswordResetRequest;
import com.ftohbackend.model.Customer;
import com.ftohbackend.model.Seller;
import com.ftohbackend.repository.CustomerRepository;
import com.ftohbackend.repository.SellerRepository;
import com.ftohbackend.service.EmailService;
import com.ftohbackend.service.PasswordResetService;

 class ForgotPasswordControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordResetService passwordResetService;

    @InjectMocks
    private ForgotPasswordController forgotPasswordController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Customer testCustomer;
    private Seller testSeller;
    private PasswordResetRequest testResetRequest;

    @BeforeEach
     void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(forgotPasswordController).build();

        // Initialize test data
        testCustomer = new Customer();
        testCustomer.setCustomerId(1);
        testCustomer.setCustomerEmail("customer@example.com");
        testCustomer.setCustomerPassword("oldPassword");

        testSeller = new Seller();
        testSeller.setSellerId(1);
        testSeller.setSellerEmail("seller@example.com");
        testSeller.setSellerPassword("oldPassword");

        testResetRequest = new PasswordResetRequest();
        testResetRequest.setEmail("customer@example.com");
        testResetRequest.setUserType("customer");
        testResetRequest.setNewPassword("newPassword");
    }

    @Test
     void testSendOtp_CustomerExistsSuccess() throws Exception {
        // Arrange
        when(customerRepository.findByCustomerEmail(anyString())).thenReturn(testCustomer);
        doNothing().when(emailService).sendMail(any(MailBody.class));

        // Act & Assert
        mockMvc.perform(post("/auth/verifyEmail/customer/customer@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OTP sent to email."));

        verify(emailService, times(1)).sendMail(any(MailBody.class));
    }

    @Test
     void testSendOtp_SellerExistsSuccess() throws Exception {
        // Arrange
        when(sellerRepository.findBySellerEmail(anyString())).thenReturn(testSeller);
        doNothing().when(emailService).sendMail(any(MailBody.class));

        // Act & Assert
        mockMvc.perform(post("/auth/verifyEmail/seller/seller@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OTP sent to email."));

        verify(emailService, times(1)).sendMail(any(MailBody.class));
    }

    @Test
     void testSendOtp_CustomerNotFound() throws Exception {
        // Arrange
        when(customerRepository.findByCustomerEmail(anyString())).thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/auth/verifyEmail/customer/nonexistent@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found."));

        verify(emailService, times(0)).sendMail(any(MailBody.class));
    }

    @Test
     void testSendOtp_SellerNotFound() throws Exception {
        // Arrange
        when(sellerRepository.findBySellerEmail(anyString())).thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/auth/verifyEmail/seller/nonexistent@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found."));

        verify(emailService, times(0)).sendMail(any(MailBody.class));
    }

    @Test
     void testSendOtp_InvalidUserType() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/auth/verifyEmail/invalid/customer@example.com"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid user type."));

        verify(emailService, times(0)).sendMail(any(MailBody.class));
    }

    @Test
     void testVerifyOtp_Success() throws Exception {
        // Arrange
        String email = "test@example.com";
        int otp = 123456;
        
        // Add OTP to the controller's OTP store using reflection
        Map<String, Integer> otpStore = (Map<String, Integer>) ReflectionTestUtils.getField(forgotPasswordController, "otpStore");
        Map<String, Long> otpExpiry = (Map<String, Long>) ReflectionTestUtils.getField(forgotPasswordController, "otpExpiry");
        
        otpStore.put(email, otp);
        otpExpiry.put(email, System.currentTimeMillis() + 300000); // 5 minutes in future

        // Act & Assert
        mockMvc.perform(post("/auth/verifyOtp/{email}/{otp}", email, otp))
                .andExpect(status().isOk())
                .andExpect(content().string("OTP verified successfully."));
    }

    @Test
     void testVerifyOtp_NoOtpGenerated() throws Exception {
        // Act & Assert
        String email = "test@example.com";
        int otp = 123456;
        
        mockMvc.perform(post("/auth/verifyOtp/{email}/{otp}", email, otp))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No OTP generated for this email."));
    }

    @Test
     void testVerifyOtp_OtpExpired() throws Exception {
        // Arrange
        String email = "test@example.com";
        int otp = 123456;
        
        // Add expired OTP
        Map<String, Integer> otpStore = (Map<String, Integer>) ReflectionTestUtils.getField(forgotPasswordController, "otpStore");
        Map<String, Long> otpExpiry = (Map<String, Long>) ReflectionTestUtils.getField(forgotPasswordController, "otpExpiry");
        
        otpStore.put(email, otp);
        otpExpiry.put(email, System.currentTimeMillis() - 1000); // Expired 1 second ago

        // Act & Assert
        mockMvc.perform(post("/auth/verifyOtp/{email}/{otp}", email, otp))
                .andExpect(status().isExpectationFailed())
                .andExpect(content().string("OTP expired."));
    }

    @Test
     void testVerifyOtp_InvalidOtp() throws Exception {
        // Arrange
        String email = "test@example.com";
        int correctOtp = 123456;
        int wrongOtp = 654321;
        
        // Add OTP
        Map<String, Integer> otpStore = (Map<String, Integer>) ReflectionTestUtils.getField(forgotPasswordController, "otpStore");
        Map<String, Long> otpExpiry = (Map<String, Long>) ReflectionTestUtils.getField(forgotPasswordController, "otpExpiry");
        
        otpStore.put(email, correctOtp);
        otpExpiry.put(email, System.currentTimeMillis() + 300000); // 5 minutes in future

        // Act & Assert
        mockMvc.perform(post("/auth/verifyOtp/{email}/{otp}", email, wrongOtp))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid OTP."));
    }

    @Test
     void testResetPassword_Customer_Success() throws Exception {
        // Arrange
        String email = "customer@example.com";
        
        // Add entry to OTP store
        Map<String, Integer> otpStore = (Map<String, Integer>) ReflectionTestUtils.getField(forgotPasswordController, "otpStore");
        Map<String, Long> otpExpiry = (Map<String, Long>) ReflectionTestUtils.getField(forgotPasswordController, "otpExpiry");
        
        otpStore.put(email, 123456);
        otpExpiry.put(email, System.currentTimeMillis() + 300000);
        
        when(customerRepository.findByCustomerEmail(email)).thenReturn(testCustomer);
        doNothing().when(passwordResetService).updatePassword(any(Customer.class), anyString());
        
        // Act & Assert
        mockMvc.perform(post("/auth/resetPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testResetRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successful."));
        
        verify(passwordResetService, times(1)).updatePassword(any(Customer.class), eq("newPassword"));
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
     void testResetPassword_Seller_Success() throws Exception {
        // Arrange
        testResetRequest.setEmail("seller@example.com");
        testResetRequest.setUserType("seller");
        
        // Add entry to OTP store
        Map<String, Integer> otpStore = (Map<String, Integer>) ReflectionTestUtils.getField(forgotPasswordController, "otpStore");
        Map<String, Long> otpExpiry = (Map<String, Long>) ReflectionTestUtils.getField(forgotPasswordController, "otpExpiry");
        
        otpStore.put("seller@example.com", 123456);
        otpExpiry.put("seller@example.com", System.currentTimeMillis() + 300000);
        
        when(sellerRepository.findBySellerEmail("seller@example.com")).thenReturn(testSeller);
        doNothing().when(passwordResetService).updatePassword(any(Seller.class), anyString());
        
        // Act & Assert
        mockMvc.perform(post("/auth/resetPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testResetRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successful."));
        
        verify(passwordResetService, times(1)).updatePassword(any(Seller.class), eq("newPassword"));
        verify(sellerRepository, times(1)).save(any(Seller.class));
    }

    @Test
     void testResetPassword_OtpNotVerified() throws Exception {
        // Arrange - not adding anything to the OTP store
        
        // Act & Assert
        mockMvc.perform(post("/auth/resetPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testResetRequest)))
                .andExpect(status().isForbidden())
                .andExpect(content().string("OTP not verified for this email."));
        
        verify(passwordResetService, times(0)).updatePassword(any(Customer.class), anyString());
        verify(customerRepository, times(0)).save(any(Customer.class));
    }

    @Test
     void testResetPassword_CustomerNotFound() throws Exception {
        // Arrange
        String email = "customer@example.com";
        
        // Add entry to OTP store
        Map<String, Integer> otpStore = (Map<String, Integer>) ReflectionTestUtils.getField(forgotPasswordController, "otpStore");
        Map<String, Long> otpExpiry = (Map<String, Long>) ReflectionTestUtils.getField(forgotPasswordController, "otpExpiry");
        
        otpStore.put(email, 123456);
        otpExpiry.put(email, System.currentTimeMillis() + 300000);
        
        when(customerRepository.findByCustomerEmail(email)).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(post("/auth/resetPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testResetRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Customer not found."));
        
        verify(passwordResetService, times(0)).updatePassword(any(Customer.class), anyString());
    }

    @Test
     void testResetPassword_SellerNotFound() throws Exception {
        // Arrange
        testResetRequest.setEmail("seller@example.com");
        testResetRequest.setUserType("seller");
        
        // Add entry to OTP store
        Map<String, Integer> otpStore = (Map<String, Integer>) ReflectionTestUtils.getField(forgotPasswordController, "otpStore");
        Map<String, Long> otpExpiry = (Map<String, Long>) ReflectionTestUtils.getField(forgotPasswordController, "otpExpiry");
        
        otpStore.put("seller@example.com", 123456);
        otpExpiry.put("seller@example.com", System.currentTimeMillis() + 300000);
        
        when(sellerRepository.findBySellerEmail("seller@example.com")).thenReturn(null);
        
        // Act & Assert
        mockMvc.perform(post("/auth/resetPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testResetRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Seller with email seller@example.com not found."));
        
        verify(passwordResetService, times(0)).updatePassword(any(Seller.class), anyString());
    }

    @Test
     void testResetPassword_InvalidUserType() throws Exception {
        // Arrange
        testResetRequest.setUserType("invalid");
        
        // Add entry to OTP store
        Map<String, Integer> otpStore = (Map<String, Integer>) ReflectionTestUtils.getField(forgotPasswordController, "otpStore");
        Map<String, Long> otpExpiry = (Map<String, Long>) ReflectionTestUtils.getField(forgotPasswordController, "otpExpiry");
        
        otpStore.put(testResetRequest.getEmail(), 123456);
        otpExpiry.put(testResetRequest.getEmail(), System.currentTimeMillis() + 300000);
        
        // Act & Assert
        mockMvc.perform(post("/auth/resetPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testResetRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid user type."));
    }
}
