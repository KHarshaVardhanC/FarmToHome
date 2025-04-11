package com.ftohbackend.servicetesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ftohbackend.exception.SellerException;
import com.ftohbackend.model.Seller;
import com.ftohbackend.repository.SellerRepository;
import com.ftohbackend.service.SellerServiceimpl;

@ExtendWith(MockitoExtension.class)
public class SellerServiceTest {
//
//    @Mock
//    private SellerRepository sellerRepository;
//
//    @InjectMocks
//    private SellerServiceimpl sellerService;
//
//    private Seller testSeller;
//    private Seller updatedSellerData;
//
//    @BeforeEach
//    void setUp() {
//        // Set up test seller
//        testSeller = new Seller();
////        testSeller.setSellerId(1);
//        testSeller.setSellerFirstName("John");
//        testSeller.setSellerLastName("Doe");
//        testSeller.setSellerEmail("john.doe@example.com");
//        testSeller.setSellerMobileNumber("1234567890");
//        testSeller.setSellerPassword("password123");
//        testSeller.setSellerStatus("Active");
//        
//        // Set up updated seller data for tests
//        updatedSellerData = new Seller();
//        updatedSellerData.setSellerFirstName("Jane");
//        updatedSellerData.setSellerLastName("Smith");
//        updatedSellerData.setSellerEmail("jane.smith@example.com");
//        updatedSellerData.setSellerMobileNumber("9876543210");
//        updatedSellerData.setSellerPassword("newpassword");
//        updatedSellerData.setSellerStatus("Inactive");
//    }
//
//    @Test
//    void testAddSeller_Success() throws SellerException {
//        when(sellerRepository.save(any(Seller.class))).thenReturn(testSeller);
//
//        String result = sellerService.addSeller(testSeller);
//
//        assertEquals("Seller Added Successfully", result);
//        verify(sellerRepository, times(1)).save(testSeller);
//    }
//
//    @Test
//    void testAddSeller_NullSeller() {
//        SellerException exception = assertThrows(SellerException.class, () -> {
//            sellerService.addSeller(null);
//        });
//
//        assertEquals("Seller object cannot be null.", exception.getMessage());
//        verify(sellerRepository, never()).save(any(Seller.class));
//    }
//
//    @Test
//    void testGetSeller_ById_Success() throws SellerException {
//        when(sellerRepository.findById(1)).thenReturn(Optional.of(testSeller));
//
//        Seller result = sellerService.getSeller(1);
//
//        assertNotNull(result);
//        assertEquals(testSeller.getSellerId(), result.getSellerId());
//        assertEquals(testSeller.getSellerFirstName(), result.getSellerFirstName());
//        verify(sellerRepository, times(1)).findById(1);
//    }
//
//    @Test
//    void testGetSeller_ById_NullId() {
//        SellerException exception = assertThrows(SellerException.class, () -> {
//            sellerService.getSeller(null);
//        });
//
//        assertEquals("Seller ID cannot be null.", exception.getMessage());
//        verify(sellerRepository, never()).findById(any());
//    }
//
//    @Test
//    void testGetSeller_ById_NotFound() {
//        when(sellerRepository.findById(99)).thenReturn(Optional.empty());
//
//        SellerException exception = assertThrows(SellerException.class, () -> {
//            sellerService.getSeller(99);
//        });
//
//        assertEquals("Seller not found with ID: 99", exception.getMessage());
//        verify(sellerRepository, times(1)).findById(99);
//    }
//
//    @Test
//    void testDeleteSeller_Success() throws SellerException {
//        when(sellerRepository.existsById(1)).thenReturn(true);
//        doNothing().when(sellerRepository).deleteById(1);
//
//        String result = sellerService.deleteSeller(1);
//
//        assertEquals("Seller Deleted Successfully", result);
//        verify(sellerRepository, times(1)).existsById(1);
//    }
//
//    @Test
//    void testDeleteSeller_NullId() {
//        SellerException exception = assertThrows(SellerException.class, () -> {
//            sellerService.deleteSeller(null);
//        });
//
//        assertEquals("Seller ID cannot be null.", exception.getMessage());
//        verify(sellerRepository, never()).existsById(any());
//        verify(sellerRepository, never()).deleteById(any());
//    }
//
//    @Test
//    void testDeleteSeller_NotFound() {
//        when(sellerRepository.existsById(99)).thenReturn(false);
//
//        SellerException exception = assertThrows(SellerException.class, () -> {
//            sellerService.deleteSeller(99);
//        });
//
//        assertEquals("Seller not found with ID: 99", exception.getMessage());
//        verify(sellerRepository, times(1)).existsById(99);
//        verify(sellerRepository, never()).deleteById(any());
//    }
//
//    @Test
//    void testUpdateSellerDetails_Success() throws SellerException {
//        when(sellerRepository.findById(1)).thenReturn(Optional.of(testSeller));
//        when(sellerRepository.save(any(Seller.class))).thenReturn(testSeller);
//
//        // Explicitly cast to resolve ambiguity
//        String result = sellerService.updateSeller(Integer.valueOf(1), updatedSellerData);
//
//        assertEquals("Updated Seller Details Successfully", result);
//        assertEquals(updatedSellerData.getSellerFirstName(), testSeller.getSellerFirstName());
//        assertEquals(updatedSellerData.getSellerLastName(), testSeller.getSellerLastName());
//        assertEquals(updatedSellerData.getSellerEmail(), testSeller.getSellerEmail());
//        verify(sellerRepository, times(1)).findById(1);
//        verify(sellerRepository, times(1)).save(testSeller);
//    }
//
//    @Test
//    void testUpdateSellerDetails_NullId() {
//        SellerException exception = assertThrows(SellerException.class, () -> {
//            // Explicitly cast the first parameter to resolve ambiguity
//            sellerService.updateSeller((Integer)null, updatedSellerData);
//        });
//
//        assertEquals("Seller ID and updated seller data cannot be null.", exception.getMessage());
//        verify(sellerRepository, never()).findById(any());
//        verify(sellerRepository, never()).save(any());
//    }
//
//    @Test
//    void testUpdateSellerDetails_NullSeller() {
//        SellerException exception = assertThrows(SellerException.class, () -> {
//            // Explicitly cast the second parameter to resolve ambiguity
//            sellerService.updateSeller(Integer.valueOf(1), (Seller)null);
//        });
//
//        assertEquals("Seller ID and updated seller data cannot be null.", exception.getMessage());
//        verify(sellerRepository, never()).findById(any());
//        verify(sellerRepository, never()).save(any());
//    }
//
//    @Test
//    void testUpdateSellerDetails_NotFound() {
//        when(sellerRepository.findById(99)).thenReturn(Optional.empty());
//
//        SellerException exception = assertThrows(SellerException.class, () -> {
//            // Explicitly cast to resolve ambiguity
//            sellerService.updateSeller(Integer.valueOf(99), updatedSellerData);
//        });
//
//        assertEquals("Seller not found with ID: 99", exception.getMessage());
//        verify(sellerRepository, times(1)).findById(99);
//        verify(sellerRepository, never()).save(any());
//    }
//
//    @Test
//    void testUpdateSellerStatus_ToActive() throws SellerException {
//        when(sellerRepository.findById(1)).thenReturn(Optional.of(testSeller));
//        when(sellerRepository.save(any(Seller.class))).thenReturn(testSeller);
//
//        // Explicitly call the status update method with String parameter
//        String result = sellerService.updateSeller(Integer.valueOf(1), "Active");
//
//        assertEquals("Seller Account is Activated Successfully", result);
//        assertEquals("Active", testSeller.getSellerStatus());
//        verify(sellerRepository, times(1)).findById(1);
//        verify(sellerRepository, times(1)).save(testSeller);
//    }
//
//    @Test
//    void testUpdateSellerStatus_ToInactive() throws SellerException {
//        when(sellerRepository.findById(1)).thenReturn(Optional.of(testSeller));
//        when(sellerRepository.save(any(Seller.class))).thenReturn(testSeller);
//
//        // Explicitly call the status update method with String parameter
//        String result = sellerService.updateSeller(Integer.valueOf(1), "Inactive");
//
//        assertEquals("Seller Account is Activated Successfully", result);
//        assertEquals("Inactive", testSeller.getSellerStatus());
//        verify(sellerRepository, times(1)).findById(1);
//        verify(sellerRepository, times(1)).save(testSeller);
//    }
//
//    @Test
//    void testUpdateSellerStatus_OtherStatus() throws SellerException {
//        when(sellerRepository.findById(1)).thenReturn(Optional.of(testSeller));
//        when(sellerRepository.save(any(Seller.class))).thenReturn(testSeller);
//
//        // Explicitly call the status update method with String parameter
//        String result = sellerService.updateSeller(Integer.valueOf(1), "Pending");
//
//        assertEquals("Seller Account status Activated", result);
//        assertEquals("Pending", testSeller.getSellerStatus());
//        verify(sellerRepository, times(1)).findById(1);
//        verify(sellerRepository, times(1)).save(testSeller);
//    }
//
//    @Test
//    void testUpdateSellerStatus_NullId() {
//        SellerException exception = assertThrows(SellerException.class, () -> {
//            // Cast first parameter to Integer, second to String to resolve ambiguity
//            sellerService.updateSeller((Integer)null, "Active");
//        });
//
//        assertEquals("Seller ID and status cannot be null.", exception.getMessage());
//        verify(sellerRepository, never()).findById(any());
//        verify(sellerRepository, never()).save(any());
//    }
//
//    @Test
//    void testUpdateSellerStatus_NullStatus() {
//        SellerException exception = assertThrows(SellerException.class, () -> {
//            // Cast first parameter to Integer, second to String to resolve ambiguity
//            sellerService.updateSeller(Integer.valueOf(1), (String)null);
//        });
//
//        assertEquals("Seller ID and status cannot be null.", exception.getMessage());
//        verify(sellerRepository, never()).findById(any());
//        verify(sellerRepository, never()).save(any());
//    }
//
//    @Test
//    void testGetAllSellers_Success() throws SellerException {
//        List<Seller> sellers = new ArrayList<>();
//        sellers.add(testSeller);
//        
//        Seller secondSeller = new Seller();
////        secondSeller.setSellerId(2);
//        secondSeller.setSellerFirstName("Jane");
//        secondSeller.setSellerLastName("Smith");
//        sellers.add(secondSeller);
//
//        when(sellerRepository.findAll()).thenReturn(sellers);
//
//        List<Seller> result = sellerService.getSeller();
//
//        assertNotNull(result);
//        assertEquals(2, result.size());
//        verify(sellerRepository, times(1)).findAll();
//    }
//
//    @Test
//    void testGetAllSellers_EmptyList() {
//        when(sellerRepository.findAll()).thenReturn(new ArrayList<>());
//
//        SellerException exception = assertThrows(SellerException.class, () -> {
//            sellerService.getSeller();
//        });
//
//        assertEquals("No sellers found.", exception.getMessage());
//        verify(sellerRepository, times(1)).findAll();
//    }
//
//    @Test
//    void testAuthenticateSeller_Success() throws SellerException {
//        String email = "john.doe@example.com";
//        String password = "password123";
//        
//        when(sellerRepository.findBySellerEmail(email)).thenReturn(testSeller);
//        when(testSeller.verifyPassword(password)).thenReturn(true);
//
//        Seller result = sellerService.authenticateSeller(email, password);
//
//        assertNotNull(result);
//        assertEquals(testSeller, result);
//        verify(sellerRepository, times(1)).findBySellerEmail(email);
//    }
//
//    @Test
//    void testAuthenticateSeller_NullEmail() throws SellerException {
//        SellerException exception = assertThrows(SellerException.class, () -> {
//            sellerService.authenticateSeller(null, "password123");
//        });
//
//        assertEquals("Email and password must not be null.", exception.getMessage());
//        verify(sellerRepository, never()).findBySellerEmail(any());
//    }
//
//    @Test
//    void testAuthenticateSeller_NullPassword() throws SellerException {
//        SellerException exception = assertThrows(SellerException.class, () -> {
//            sellerService.authenticateSeller("john.doe@example.com", null);
//        });
//
//        assertEquals("Email and password must not be null.", exception.getMessage());
//        verify(sellerRepository, never()).findBySellerEmail(any());
//    }
//
//    @Test
//    void testAuthenticateSeller_EmailNotFound() throws SellerException {
//        when(sellerRepository.findBySellerEmail("nonexistent@example.com")).thenReturn(null);
//
//        SellerException exception = assertThrows(SellerException.class, () -> {
//            sellerService.authenticateSeller("nonexistent@example.com", "password123");
//        });
//
//        assertEquals("Seller not found with email: nonexistent@example.com", exception.getMessage());
//        verify(sellerRepository, times(1)).findBySellerEmail("nonexistent@example.com");
//    }
//
//    @Test
//    void testAuthenticateSeller_IncorrectPassword() throws SellerException {
//        String email = "john.doe@example.com";
//        String password = "wrongpassword";
//        
//        when(sellerRepository.findBySellerEmail(email)).thenReturn(testSeller);
//        when(testSeller.verifyPassword(password)).thenReturn(false);
//
//        SellerException exception = assertThrows(SellerException.class, () -> {
//            sellerService.authenticateSeller(email, password);
//        });
//
//        assertEquals("Incorrect password.", exception.getMessage());
//        verify(sellerRepository, times(1)).findBySellerEmail(email);
//    }
}