package com.ftohbackend.repositorytesting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ftohbackend.exception.SellerException;
import com.ftohbackend.model.Seller;
import com.ftohbackend.repository.SellerRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SellerRepositoryTest {

    @Autowired
    private SellerRepository sellerRepository;
    
    private Seller testSeller;
    
    @BeforeEach
    public void setUp() {
        // Create test seller
        testSeller = new Seller();
        testSeller.setSellerFirstName("John");
        testSeller.setSellerLastName("Doe");
        testSeller.setSellerEmail("john.doe@example.com");
        testSeller.setSellerPassword("securepass");
        testSeller.setSellerMobileNumber("9876543210");
        testSeller.setSellerCity("City");
        testSeller.setSellerPlace("Place");
        testSeller.setSellerState("State");
        testSeller.setSellerPincode("123456");
        testSeller.setSellerStatus("Active");
        sellerRepository.save(testSeller);
    }
    
    @AfterEach
    public void tearDown() {
        sellerRepository.deleteAll();
    }
    
    @Test
    @DisplayName("Save seller test")
    public void testSaveSeller() {
        // Create a new seller
        Seller seller = new Seller();
        seller.setSellerFirstName("Jane");
        seller.setSellerLastName("Smith");
        seller.setSellerEmail("jane.smith@example.com");
        seller.setSellerPassword("password123");
        seller.setSellerMobileNumber("9876543211");
        seller.setSellerCity("New City");
        seller.setSellerPlace("New Place");
        seller.setSellerState("New State");
        seller.setSellerPincode("654321");
        seller.setSellerStatus("Pending");
        
        // Save the seller
        Seller savedSeller = sellerRepository.save(seller);
        
        // Verify the saved seller
        assertThat(savedSeller).isNotNull();
        assertThat(savedSeller.getSellerId()).isNotNull();
        assertThat(savedSeller.getSellerFirstName()).isEqualTo("Jane");
        assertThat(savedSeller.getSellerLastName()).isEqualTo("Smith");
        assertThat(savedSeller.getSellerEmail()).isEqualTo("jane.smith@example.com");
    }
    
    @Test
    @DisplayName("Find seller by ID test")
    public void testFindById() {
        Seller found = sellerRepository.findById(testSeller.getSellerId()).orElse(null);
        
        assertThat(found).isNotNull();
        assertThat(found.getSellerFirstName()).isEqualTo("John");
        assertThat(found.getSellerLastName()).isEqualTo("Doe");
        assertThat(found.getSellerEmail()).isEqualTo("john.doe@example.com");
    }
    
    @Test
    @DisplayName("Find seller by email test")
    public void testFindBySellerEmail() throws SellerException {
        Seller found = sellerRepository.findBySellerEmail("john.doe@example.com");
        
        assertThat(found).isNotNull();
        assertThat(found.getSellerId()).isEqualTo(testSeller.getSellerId());
        assertThat(found.getSellerFirstName()).isEqualTo("John");
        assertThat(found.getSellerLastName()).isEqualTo("Doe");
    }
    
    @Test
    @DisplayName("Find seller by non-existent email test")
    public void testFindByNonExistentEmail() throws SellerException {
        Seller found = sellerRepository.findBySellerEmail("nonexistent@example.com");
        
        assertThat(found).isNull();
    }
    
    @Test
    @DisplayName("Update seller test")
    public void testUpdateSeller() {
        Seller sellerToUpdate = sellerRepository.findById(testSeller.getSellerId()).orElse(null);
        assertThat(sellerToUpdate).isNotNull();
        
        // Update seller details
        sellerToUpdate.setSellerFirstName("Updated John");
        sellerToUpdate.setSellerLastName("Updated Doe");
        sellerToUpdate.setSellerStatus("Inactive");
        
        Seller updatedSeller = sellerRepository.save(sellerToUpdate);
        
        assertThat(updatedSeller.getSellerFirstName()).isEqualTo("Updated John");
        assertThat(updatedSeller.getSellerLastName()).isEqualTo("Updated Doe");
        assertThat(updatedSeller.getSellerStatus()).isEqualTo("Inactive");
        assertThat(updatedSeller.getSellerId()).isEqualTo(testSeller.getSellerId());
    }
    
    @Test
    @DisplayName("Delete seller test")
    public void testDeleteSeller() {
        sellerRepository.deleteById(testSeller.getSellerId());
        
        assertThat(sellerRepository.findById(testSeller.getSellerId())).isEmpty();
    }
    
    @Test
    @DisplayName("Find all sellers test")
    public void testFindAllSellers() {
        // Add another seller
        Seller anotherSeller = new Seller();
        anotherSeller.setSellerFirstName("Alice");
        anotherSeller.setSellerLastName("Johnson");
        anotherSeller.setSellerEmail("alice.johnson@example.com");
        anotherSeller.setSellerPassword("pass123");
        anotherSeller.setSellerMobileNumber("8765432109");
        anotherSeller.setSellerCity("Another City");
        anotherSeller.setSellerPlace("Another Place");
        anotherSeller.setSellerState("Another State");
        anotherSeller.setSellerPincode("987654");
        anotherSeller.setSellerStatus("Active");
        sellerRepository.save(anotherSeller);
        
        List<Seller> allSellers = sellerRepository.findAll();
        
        assertThat(allSellers).isNotNull();
        assertThat(allSellers).hasSize(2);
    }
    
    @Test
    @DisplayName("Update seller status test")
    public void testUpdateSellerStatus() {
        Seller sellerToUpdate = sellerRepository.findById(testSeller.getSellerId()).orElse(null);
        assertThat(sellerToUpdate).isNotNull();
        
        // Update seller status
        sellerToUpdate.setSellerStatus("Suspended");
        
        Seller updatedSeller = sellerRepository.save(sellerToUpdate);
        
        assertThat(updatedSeller.getSellerStatus()).isEqualTo("Suspended");
        assertThat(updatedSeller.getSellerId()).isEqualTo(testSeller.getSellerId());
    }
    
    @Test
    @DisplayName("Multiple sellers with different emails test")
    public void testMultipleSellersWithDifferentEmails() throws SellerException {
        // Create and save several sellers with different emails
        Seller seller1 = new Seller();
        seller1.setSellerFirstName("Seller");
        seller1.setSellerLastName("One");
        seller1.setSellerEmail("seller.one@example.com");
        seller1.setSellerPassword("password1");
        seller1.setSellerMobileNumber("1111111111");
        seller1.setSellerCity("City1");
        seller1.setSellerPlace("Place1");
        seller1.setSellerState("State1");
        seller1.setSellerPincode("111111");
        seller1.setSellerStatus("Active");
        sellerRepository.save(seller1);
        
        Seller seller2 = new Seller();
        seller2.setSellerFirstName("Seller");
        seller2.setSellerLastName("Two");
        seller2.setSellerEmail("seller.two@example.com");
        seller2.setSellerPassword("password2");
        seller2.setSellerMobileNumber("2222222222");
        seller2.setSellerCity("City2");
        seller2.setSellerPlace("Place2");
        seller2.setSellerState("State2");
        seller2.setSellerPincode("222222");
        seller2.setSellerStatus("Active");
        sellerRepository.save(seller2);
        
        // Verify finding each seller by email
        Seller foundSeller1 = sellerRepository.findBySellerEmail("seller.one@example.com");
        Seller foundSeller2 = sellerRepository.findBySellerEmail("seller.two@example.com");
        
        assertThat(foundSeller1).isNotNull();
        assertThat(foundSeller1.getSellerFirstName()).isEqualTo("Seller");
        assertThat(foundSeller1.getSellerLastName()).isEqualTo("One");
        
        assertThat(foundSeller2).isNotNull();
        assertThat(foundSeller2.getSellerFirstName()).isEqualTo("Seller");
        assertThat(foundSeller2.getSellerLastName()).isEqualTo("Two");
        
        // Verify they're different sellers
        assertThat(foundSeller1.getSellerId()).isNotEqualTo(foundSeller2.getSellerId());
    }
}