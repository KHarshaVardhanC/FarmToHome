package com.ftohbackend.servicetesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ftohbackend.exception.SellerException;
import com.ftohbackend.model.Seller;
import com.ftohbackend.repository.SellerRepository;
import com.ftohbackend.service.SellerServiceimpl;

class SellerServiceTest {

    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private SellerServiceimpl sellerService;

    private Seller sampleSeller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create a sample seller for testing
        sampleSeller = new Seller();
        sampleSeller.setSellerId(1);
        sampleSeller.setSellerFirstName("John");
        sampleSeller.setSellerLastName("Doe");
        sampleSeller.setSellerEmail("john.doe@example.com");
        sampleSeller.setSellerMobileNumber("9876543210");
        sampleSeller.setSellerPassword("password123");
        sampleSeller.setSellerPlace("Manhattan");
        sampleSeller.setSellerCity("New York");
        sampleSeller.setSellerState("NY");
        sampleSeller.setSellerPincode("100001");
        sampleSeller.setSellerStatus("Active");
    }

    @Test
    void testAddSeller_Success() throws SellerException {
        // Arrange
        when(sellerRepository.save(any(Seller.class))).thenReturn(sampleSeller);

        // Act
        String result = sellerService.addSeller(sampleSeller);

        // Assert
        assertEquals("Seller Added Successfully", result);
        verify(sellerRepository, times(1)).save(any(Seller.class));
    }

    @Test
    void testAddSeller_NullSeller() {
        // Act & Assert
        assertThrows(SellerException.class, () -> sellerService.addSeller(null));
    }

    @Test
    void testGetSeller_ById_Success() throws SellerException {
        // Arrange
        when(sellerRepository.findById(1)).thenReturn(Optional.of(sampleSeller));

        // Act
        Seller result = sellerService.getSeller(1);

        // Assert
        assertNotNull(result);
        assertEquals(sampleSeller.getSellerId(), result.getSellerId());
        assertEquals(sampleSeller.getSellerFirstName(), result.getSellerFirstName());
    }

    @Test
    void testGetSeller_ById_NullId() {
        // Act & Assert
        assertThrows(SellerException.class, () -> sellerService.getSeller((Integer) null));
    }

    @Test
    void testGetSeller_ById_NotFound() {
        // Arrange
        when(sellerRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SellerException.class, () -> sellerService.getSeller(999));
    }

    @Test
    void testDeleteSeller_Success() throws SellerException {
        // Arrange
        when(sellerRepository.existsById(1)).thenReturn(true);
        doNothing().when(sellerRepository).deleteById(1);

        // Act
        String result = sellerService.deleteSeller(1);

        // Assert
        assertEquals("Seller Deleted Successfully", result);
        verify(sellerRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteSeller_NullId() {
        // Act & Assert
        assertThrows(SellerException.class, () -> sellerService.deleteSeller(null));
    }

    @Test
    void testDeleteSeller_NotFound() {
        // Arrange
        when(sellerRepository.existsById(anyInt())).thenReturn(false);

        // Act & Assert
        assertThrows(SellerException.class, () -> sellerService.deleteSeller(999));
        verify(sellerRepository, never()).deleteById(anyInt());
    }

    @Test
    void testUpdateSeller_Success() throws SellerException {
        // Arrange
        Seller updatedSeller = new Seller();
        updatedSeller.setSellerFirstName("Updated");
        updatedSeller.setSellerLastName("Name");
        updatedSeller.setSellerEmail("updated.email@example.com");
        updatedSeller.setSellerMobileNumber("9876543211");
        updatedSeller.setSellerPassword("updatedPwd");
        updatedSeller.setSellerStatus("Inactive");

        when(sellerRepository.findById(1)).thenReturn(Optional.of(sampleSeller));
        when(sellerRepository.save(any(Seller.class))).thenReturn(sampleSeller);

        // Act
        String result = sellerService.updateSeller(1, updatedSeller);

        // Assert
        assertEquals("Updated Seller Details Successfully", result);
        verify(sellerRepository, times(1)).save(any(Seller.class));
    }

    @Test
    void testUpdateSeller_NullId() {
        // Act & Assert
        assertThrows(SellerException.class, () -> sellerService.updateSeller(null, sampleSeller));
    }

 
    @Test
    void testUpdateSeller_NotFound() {
        // Arrange
        when(sellerRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SellerException.class, () -> sellerService.updateSeller(999, sampleSeller));
        verify(sellerRepository, never()).save(any(Seller.class));
    }

    @Test
    void testUpdateSellerStatus_ToActive() throws SellerException {
        // Arrange
        when(sellerRepository.findById(1)).thenReturn(Optional.of(sampleSeller));
        when(sellerRepository.save(any(Seller.class))).thenReturn(sampleSeller);

        // Act
        String result = sellerService.updateSeller(1, "Active");

        // Assert
        assertEquals("Seller Account is Activated Successfully", result);
        verify(sellerRepository, times(1)).save(any(Seller.class));
    }

    @Test
    void testUpdateSellerStatus_ToInactive() throws SellerException {
        // Arrange
        when(sellerRepository.findById(1)).thenReturn(Optional.of(sampleSeller));
        when(sellerRepository.save(any(Seller.class))).thenReturn(sampleSeller);

        // Act
        String result = sellerService.updateSeller(1, "Inactive");

        // Assert
        assertEquals("Seller Account is Activated Successfully", result);
        verify(sellerRepository, times(1)).save(any(Seller.class));
    }

    @Test
    void testUpdateSellerStatus_ToOtherStatus() throws SellerException {
        // Arrange
        when(sellerRepository.findById(1)).thenReturn(Optional.of(sampleSeller));
        when(sellerRepository.save(any(Seller.class))).thenReturn(sampleSeller);

        // Act
        String result = sellerService.updateSeller(1, "Pending");

        // Assert
        assertEquals("Seller Account status Activated", result);
        verify(sellerRepository, times(1)).save(any(Seller.class));
    }

    @Test
    void testUpdateSellerStatus_NullId() {
        // Act & Assert
        assertThrows(SellerException.class, () -> sellerService.updateSeller(null, "Active"));
    }

    @Test
    void testUpdateSellerStatus_NullStatus() {
        // Act & Assert
        assertThrows(SellerException.class, () -> sellerService.updateSeller(1, (String) null));
    }

    @Test
    void testUpdateSellerStatus_NotFound() {
        // Arrange
        when(sellerRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SellerException.class, () -> sellerService.updateSeller(999, "Active"));
        verify(sellerRepository, never()).save(any(Seller.class));
    }

    @Test
    void testGetAllSellers_Success() throws SellerException {
        // Arrange
        List<Seller> sellers = new ArrayList<>();
        sellers.add(sampleSeller);
        when(sellerRepository.findAll()).thenReturn(sellers);

        // Act
        List<Seller> result = sellerService.getSeller();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleSeller.getSellerId(), result.get(0).getSellerId());
    }

    @Test
    void testGetAllSellers_EmptyList() {
        // Arrange
        when(sellerRepository.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(SellerException.class, () -> sellerService.getSeller());
    }

    @Test
    void testAuthenticateSeller_Success() throws SellerException {
        // Arrange
        // Mock the behavior of the Seller class's verifyPassword method
        Seller mockSeller = mock(Seller.class);
        when(mockSeller.getSellerId()).thenReturn(1);
        when(mockSeller.verifyPassword("password123")).thenReturn(true);

        when(sellerRepository.findBySellerEmail("john.doe@example.com")).thenReturn(mockSeller);

        // Act
        Seller result = sellerService.authenticateSeller("john.doe@example.com", "password123");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getSellerId());
        verify(mockSeller, times(1)).verifyPassword("password123");
    }

    @Test
    void testAuthenticateSeller_NullEmail() {
        // Act & Assert
        assertThrows(SellerException.class, () -> sellerService.authenticateSeller(null, "password123"));
    }

    @Test
    void testAuthenticateSeller_NullPassword() {
        // Act & Assert
        assertThrows(SellerException.class, () -> sellerService.authenticateSeller("john.doe@example.com", null));
    }

    @Test
    void testAuthenticateSeller_SellerNotFound() throws SellerException {
        // Arrange
        when(sellerRepository.findBySellerEmail(anyString())).thenReturn(null);

        // Act & Assert
        assertThrows(SellerException.class, () -> sellerService.authenticateSeller("notfound@example.com", "password123"));
    }

    @Test
    void testAuthenticateSeller_IncorrectPassword() throws SellerException {
        // Arrange
        // Mock the behavior of the Seller class's verifyPassword method
        Seller mockSeller = mock(Seller.class);
        when(mockSeller.verifyPassword("wrongpassword")).thenReturn(false);

        when(sellerRepository.findBySellerEmail("john.doe@example.com")).thenReturn(mockSeller);

        // Act & Assert
        assertThrows(SellerException.class, () -> sellerService.authenticateSeller("john.doe@example.com", "wrongpassword"));
        verify(mockSeller, times(1)).verifyPassword("wrongpassword");
    }
}