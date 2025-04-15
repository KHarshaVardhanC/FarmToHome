package com.ftohbackend.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "Seller")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer sellerId;
    
    @Column(nullable = false, unique = true)
    String sellerEmail;
    
    @Column(nullable = false)
    String sellerFirstName;
    
    @Column(nullable = false)
    String sellerLastName;
    
    @Column(nullable = false)
    String sellerMobileNumber;
    
    @Column(nullable = false)
    String sellerPlace;
    
    @Column(nullable = false)
    String sellerCity;
    
    @Column(nullable = false)
    String sellerState;
    
    @Column(nullable = false)
    String sellerPincode;
    
    @Column(nullable = false)
    String sellerPassword;
    
    String sellerStatus;
    
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();
    
    // Use a single static instance of BCryptPasswordEncoder for consistency
//    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
//    public void setSellerPassword(String sellerPassword) {
//		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        this.sellerPassword = passwordEncoder.encode(sellerPassword);
//    }
    
    public boolean verifyPassword(String rawPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return passwordEncoder.matches(rawPassword, this.sellerPassword);
    }
}
