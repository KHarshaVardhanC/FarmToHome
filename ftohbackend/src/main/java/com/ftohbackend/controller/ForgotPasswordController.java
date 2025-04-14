package com.ftohbackend.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftohbackend.dto.MailBody;
import com.ftohbackend.dto.PasswordResetRequest;
import com.ftohbackend.exception.SellerException;
import com.ftohbackend.model.Customer;
import com.ftohbackend.model.Seller;
import com.ftohbackend.repository.CustomerRepository;
import com.ftohbackend.repository.SellerRepository;
import com.ftohbackend.service.EmailService;
import com.ftohbackend.service.PasswordResetService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class ForgotPasswordController {

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private SellerRepository sellerRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetService passwordResetService;

    private final Random random = new Random();
    private final int otpValidityInMillis = 5 * 60 * 1000; // 5 minutes

    // In-memory OTP store
    private final Map<String, Integer> otpStore = new HashMap<>();
    private final Map<String, Long> otpExpiry = new HashMap<>();

//    @PostMapping("/verifyEmail/{userType}/{email}")
//    public ResponseEntity<String> sendOtp(@PathVariable String userType, @PathVariable String email) throws SellerException {
//        boolean userExists = false;
//
//        if (userType.equalsIgnoreCase("customer")) {
//            userExists = customerRepo.findByCustomerEmail(email) != null;
//        } else if (userType.equalsIgnoreCase("seller")) {
//            userExists = sellerRepo.findBySellerEmail(email) != null;
//        } else {
//            return new ResponseEntity<>("Invalid user type.", HttpStatus.BAD_REQUEST);
//        }
//
//        if (!userExists) {
//            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
//        }
//
//        int otp = generateOtp();
//        otpStore.put(email, otp);
//        otpExpiry.put(email, System.currentTimeMillis() + otpValidityInMillis);
//
//        MailBody mail = new MailBody(email, "OTP for Password Reset", "Your OTP is: " + otp + ". It is valid for 5 minutes.");
//        emailService.sendMail(mail);
//
//        return new ResponseEntity<>("OTP sent to email.", HttpStatus.OK);
//    }

    
    
    @PostMapping("/verifyEmail/{userType}/{email}")
    public ResponseEntity<Map<String, String>> sendOtp(@PathVariable String userType, @PathVariable String email) throws SellerException {
        boolean userExists = false;

        if (userType.equalsIgnoreCase("customer")) {
            userExists = customerRepo.findByCustomerEmail(email) != null;
        } else if (userType.equalsIgnoreCase("seller")) {
            userExists = sellerRepo.findBySellerEmail(email) != null;
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid user type."));
        }

        if (!userExists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found."));
        }

        int otp = generateOtp();
        otpStore.put(email, otp);
        otpExpiry.put(email, System.currentTimeMillis() + otpValidityInMillis);

        MailBody mail = new MailBody(email, "OTP for Password Reset", "Your OTP is: " + otp + ". It is valid for 5 minutes.");
        emailService.sendMail(mail);

        return ResponseEntity.ok(Map.of("message", "OTP sent to email."));
    }
    
    
    @PostMapping("/verifyOtp/{email}/{otp}")
    public ResponseEntity<String> verifyOtp(@PathVariable String email, @PathVariable int otp) {
        if (!otpStore.containsKey(email)) {
            return new ResponseEntity<>("No OTP generated for this email.", HttpStatus.BAD_REQUEST);
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime > otpExpiry.get(email)) {
            otpStore.remove(email);
            otpExpiry.remove(email);
            return new ResponseEntity<>("OTP expired.", HttpStatus.EXPECTATION_FAILED);
        }

        if (!otpStore.get(email).equals(otp)) {
            return new ResponseEntity<>("Invalid OTP.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("OTP verified successfully.", HttpStatus.OK);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest request) throws SellerException {
        String userType = request.getUserType().toLowerCase();
        String email = request.getEmail();

        if (!otpStore.containsKey(email)) {
            return new ResponseEntity<>("OTP not verified for this email.", HttpStatus.FORBIDDEN);
        }

        if (userType.equals("customer")) {
            Customer customer = customerRepo.findByCustomerEmail(email);
            if (customer == null) {
                return new ResponseEntity<>("Customer not found.", HttpStatus.NOT_FOUND);
            }
            passwordResetService.updatePassword(customer, request.getNewPassword());
            customerRepo.save(customer);

        } else if (userType.equals("seller")) {
            Seller seller = sellerRepo.findBySellerEmail(email);
            if (seller == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body("Seller with email " + email + " not found.");
            }

            passwordResetService.updatePassword(seller, request.getNewPassword());
            sellerRepo.save(seller);

        } else {
            return new ResponseEntity<>("Invalid user type.", HttpStatus.BAD_REQUEST);
        }

        otpStore.remove(email);
        otpExpiry.remove(email);

        return new ResponseEntity<>("Password reset successful.", HttpStatus.OK);
    }

    private int generateOtp() {
        return 100000 + random.nextInt(900000); // Generates 6-digit OTP
    }
}
