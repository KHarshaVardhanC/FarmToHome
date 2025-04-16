package com.ftohbackend.servicetesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
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

import com.ftohbackend.exception.CustomerException;
import com.ftohbackend.model.Customer;
import com.ftohbackend.repository.CustomerRepository;
import com.ftohbackend.service.CustomerServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer testCustomer;
    private List<Customer> customerList;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setCustomerId(1);
        testCustomer.setCustomerFirstName("John");
        testCustomer.setCustomerLastName("Doe");
        testCustomer.setCustomerEmail("john.doe@example.com");
        testCustomer.setCustomerPassword("password123");
        testCustomer.setCustomerPlace("Downtown");
        testCustomer.setCustomerCity("Metropolis");
        testCustomer.setCustomerState("State");
        testCustomer.setCustomerPincode("123456");
        testCustomer.setCustomerPhoneNumber("9876543210");
        testCustomer.setCustomerIsActive(true);

        customerList = new ArrayList<>();
        customerList.add(testCustomer);

        Customer customer2 = new Customer();
        customer2.setCustomerId(2);
        customer2.setCustomerFirstName("Jane");
        customer2.setCustomerLastName("Smith");
        customer2.setCustomerEmail("jane.smith@example.com");
        customerList.add(customer2);
    }

    @Test
    void testAddCustomer_Success() throws CustomerException {
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        String result = customerService.addCustomer(testCustomer);

        assertEquals("customer added successfully", result);
        verify(customerRepository, times(1)).save(testCustomer);
    }

    @Test
    void testAddCustomer_NullCustomer() {
        Exception exception = assertThrows(CustomerException.class, () -> {
            customerService.addCustomer(null);
        });

        assertEquals("Customer object cannot be null.", exception.getMessage());
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void testGetCustomer_Success() throws CustomerException {
        when(customerRepository.findById(anyInt())).thenReturn(Optional.of(testCustomer));

        Customer result = customerService.getCustomer(1);

        assertNotNull(result);
        assertEquals(testCustomer.getCustomerId(), result.getCustomerId());
        assertEquals(testCustomer.getCustomerEmail(), result.getCustomerEmail());
        verify(customerRepository, times(1)).findById(1);
    }

    @Test
    void testGetCustomer_NullId() {
        Exception exception = assertThrows(CustomerException.class, () -> {
            customerService.getCustomer(null);
        });

        assertEquals("Customer ID cannot be null.", exception.getMessage());
        verify(customerRepository, never()).findById(anyInt());
    }

    @Test
    void testGetCustomer_NotFound() {
        when(customerRepository.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(CustomerException.class, () -> {
            customerService.getCustomer(999);
        });

        assertEquals("Customer not found with ID: 999", exception.getMessage());
        verify(customerRepository, times(1)).findById(999);
    }

    @Test
    void testGetAllCustomers_Success() throws CustomerException {
        when(customerRepository.findAll()).thenReturn(customerList);

        List<Customer> result = customerService.getAllCustomers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testCustomer.getCustomerId(), result.get(0).getCustomerId());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testGetAllCustomers_EmptyList() {
        when(customerRepository.findAll()).thenReturn(new ArrayList<>());

        Exception exception = assertThrows(CustomerException.class, () -> {
            customerService.getAllCustomers();
        });

        assertEquals("No customers found.", exception.getMessage());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCustomer_Success() throws CustomerException {
        Customer updatedCustomer = new Customer();
        updatedCustomer.setCustomerFirstName("JohnUpdated");
        updatedCustomer.setCustomerEmail("john.updated@example.com");
        updatedCustomer.setCustomerPassword("newPassword");

        when(customerRepository.findById(anyInt())).thenReturn(Optional.of(testCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        String result = customerService.updateCustomer(1, updatedCustomer);

        assertEquals("Customer updated successfully", result);
        assertEquals("JohnUpdated", testCustomer.getCustomerFirstName());
        assertEquals("john.updated@example.com", testCustomer.getCustomerEmail());
        // Password should be encoded, so we can't directly check the value
        assertNotEquals("newPassword", testCustomer.getCustomerPassword());
        verify(customerRepository, times(1)).findById(1);
        verify(customerRepository, times(1)).save(testCustomer);
    }

    @Test
    void testUpdateCustomer_NullId() {
        Exception exception = assertThrows(CustomerException.class, () -> {
            customerService.updateCustomer(null, testCustomer);
        });

        assertEquals("Customer ID and customer data cannot be null.", exception.getMessage());
        verify(customerRepository, never()).findById(anyInt());
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void testUpdateCustomer_NullCustomer() {
        Exception exception = assertThrows(CustomerException.class, () -> {
            customerService.updateCustomer(1, null);
        });

        assertEquals("Customer ID and customer data cannot be null.", exception.getMessage());
        verify(customerRepository, never()).findById(anyInt());
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void testUpdateCustomer_NotFound() {
        when(customerRepository.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(CustomerException.class, () -> {
            customerService.updateCustomer(999, testCustomer);
        });

        assertEquals("Customer not found with ID: 999", exception.getMessage());
        verify(customerRepository, times(1)).findById(999);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void testAuthenticateCustomer_Success() throws Exception {
        // Create a mock implementation of verifyPassword
        Customer mockCustomer = spy(testCustomer);
        doReturn(true).when(mockCustomer).verifyPassword(anyString());

        when(customerRepository.findByCustomerEmail(anyString())).thenReturn(mockCustomer);

        Customer result = customerService.authenticateCustomer("john.doe@example.com", "password123");

        assertNotNull(result);
        assertEquals(testCustomer.getCustomerId(), result.getCustomerId());
        verify(customerRepository, times(1)).findByCustomerEmail("john.doe@example.com");
        verify(mockCustomer, times(1)).verifyPassword("password123");
    }

    @Test
    void testAuthenticateCustomer_NullEmail() {
        Exception exception = assertThrows(CustomerException.class, () -> {
            customerService.authenticateCustomer(null, "password123");
        });

        assertEquals("Email and password must not be null.", exception.getMessage());
        verify(customerRepository, never()).findByCustomerEmail(anyString());
    }

    @Test
    void testAuthenticateCustomer_NullPassword() {
        Exception exception = assertThrows(CustomerException.class, () -> {
            customerService.authenticateCustomer("john.doe@example.com", null);
        });

        assertEquals("Email and password must not be null.", exception.getMessage());
        verify(customerRepository, never()).findByCustomerEmail(anyString());
    }

    @Test
    void testAuthenticateCustomer_CustomerNotFound() {
        when(customerRepository.findByCustomerEmail(anyString())).thenReturn(null);

        Exception exception = assertThrows(CustomerException.class, () -> {
            customerService.authenticateCustomer("nonexistent@example.com", "password123");
        });

        assertEquals("Customer not found with email: nonexistent@example.com", exception.getMessage());
        verify(customerRepository, times(1)).findByCustomerEmail("nonexistent@example.com");
    }

    @Test
    void testAuthenticateCustomer_IncorrectPassword() {
        // Create a mock implementation of verifyPassword
        Customer mockCustomer = spy(testCustomer);
        doReturn(false).when(mockCustomer).verifyPassword(anyString());

        when(customerRepository.findByCustomerEmail(anyString())).thenReturn(mockCustomer);

        Exception exception = assertThrows(CustomerException.class, () -> {
            customerService.authenticateCustomer("john.doe@example.com", "wrongPassword");
        });

        assertEquals("Incorrect password.", exception.getMessage());
        verify(customerRepository, times(1)).findByCustomerEmail("john.doe@example.com");
        verify(mockCustomer, times(1)).verifyPassword("wrongPassword");
    }
}