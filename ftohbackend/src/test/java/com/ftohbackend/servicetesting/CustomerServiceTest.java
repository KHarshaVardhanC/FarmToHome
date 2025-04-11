package com.ftohbackend.servicetesting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
//
//    @Mock
//    private CustomerRepository customerRepository;
//    
//    @InjectMocks
//    private CustomerServiceImpl customerService;
//
//    Customer customer;
//
//    @BeforeEach
//    public void setUp() {
//        customer = new Customer();
//        customer.setCustomerId(1);
//        customer.setCustomerFirstName("John");
//        customer.setCustomerLastName("Doe");
//        customer.setCustomerEmail("john.doe@example.com");
//        customer.setCustomerPassword("password123");
//        customer.setCustomerCity("New York");
//        customer.setCustomerPlace("Manhattan");
//        customer.setCustomerPincode("10001");
//        customer.setCustomerState("New York");
//        customer.setCustomerPhoneNumber("1234567890");
//        customer.setCustomerIsActive(true);
//    }
//
//    @AfterEach
//    public void tearDown() {
//        customer = null;
//    }
//
//    @Test
//    @DisplayName("JUnit test for addCustomer operation")
//    public void givenCustomerObject_whenAddCustomer_thenReturnSuccessMessage() throws CustomerException {
//        // given - precondition or setup
//        given(customerRepository.save(customer)).willReturn(customer);
//        
//        // when - action or the behaviour
//        String result = customerService.addCustomer(customer);
//        
//        // then - verify the output
//        assertThat(result).isEqualTo("customer added successfully");
//        verify(customerRepository, times(1)).save(customer);
//    }
//
//    @Test
//    @DisplayName("JUnit test for addCustomer operation with null customer")
//    public void givenNullCustomerObject_whenAddCustomer_thenThrowCustomerException() {
//        // when - action or the behaviour
//        assertThrows(CustomerException.class, () -> {
//            customerService.addCustomer(null);
//        });
//        
//        // then - verify the output
//        verify(customerRepository, never()).save(any(Customer.class));
//    }
//
//    @Test
//    @DisplayName("JUnit test for getCustomer operation")
//    public void givenCustomerId_whenGetCustomer_thenReturnCustomerObject() throws CustomerException {
//        // given - precondition or setup
//        given(customerRepository.findById(customer.getCustomerId())).willReturn(Optional.of(customer));
//        
//        // when - action or the behaviour
//        Customer foundCustomer = customerService.getCustomer(customer.getCustomerId());
//        
//        // then - verify the output
//        assertThat(foundCustomer).isNotNull();
//        assertThat(foundCustomer.getCustomerId()).isEqualTo(customer.getCustomerId());
//    }
//
//    @Test
//    @DisplayName("JUnit test for getCustomer operation - throw CustomerException when ID is null")
//    public void givenNullCustomerId_whenGetCustomer_thenThrowCustomerException() {
//        // when - action or the behaviour
//        assertThrows(CustomerException.class, () -> {
//            customerService.getCustomer(null);
//        });
//    }
//
//    @Test
//    @DisplayName("JUnit test for getCustomer operation - throw CustomerException when customer not found")
//    public void givenCustomerId_whenGetCustomer_thenThrowCustomerException() {
//        // given - precondition or setup
//        given(customerRepository.findById(anyInt())).willReturn(Optional.empty());
//        
//        // when - action or the behaviour
//        assertThrows(CustomerException.class, () -> {
//            customerService.getCustomer(1);
//        });
//    }
//
//    @Test
//    @DisplayName("JUnit test for getAllCustomers operation")
//    public void givenCustomersList_whenGetAllCustomers_thenReturnCustomersList() throws CustomerException {
//        // given - precondition or setup
//        Customer customer1 = new Customer();
//        customer1.setCustomerId(2);
//        customer1.setCustomerFirstName("Jane");
//        customer1.setCustomerLastName("Smith");
//        customer1.setCustomerEmail("jane.smith@example.com");
//        
//        given(customerRepository.findAll()).willReturn(List.of(customer, customer1));
//        
//        // when - action or the behaviour
//        List<Customer> customers = customerService.getAllCustomers();
//        
//        // then - verify the output
//        assertThat(customers).isNotNull();
//        assertThat(customers.size()).isEqualTo(2);
//    }
//
//    @Test
//    @DisplayName("JUnit test for getAllCustomers operation - throw CustomerException when no customers found")
//    public void givenEmptyCustomersList_whenGetAllCustomers_thenThrowCustomerException() {
//        // given - precondition or setup
//        given(customerRepository.findAll()).willReturn(Collections.emptyList());
//        
//        // when - action or the behaviour
//        assertThrows(CustomerException.class, () -> {
//            customerService.getAllCustomers();
//        });
//    }
//
//    @Test
//    @DisplayName("JUnit test for updateCustomer operation")
//    public void givenCustomer_whenUpdateCustomer_thenReturnSuccessMessage() throws CustomerException {
//        // given - precondition or setup
//        Customer updatedCustomer = new Customer();
//        updatedCustomer.setCustomerEmail("john.updated@example.com");
//        updatedCustomer.setCustomerCity("Los Angeles");
//        
//        given(customerRepository.findById(customer.getCustomerId())).willReturn(Optional.of(customer));
//        given(customerRepository.save(any(Customer.class))).willReturn(customer);
//        
//        // when - action or the behaviour
//        String result = customerService.updateCustomer(customer.getCustomerId(), updatedCustomer);
//        
//        // then - verify the output
//        assertThat(result).isEqualTo("Customer updated successfully");
//        assertThat(customer.getCustomerEmail()).isEqualTo(updatedCustomer.getCustomerEmail());
//        assertThat(customer.getCustomerCity()).isEqualTo(updatedCustomer.getCustomerCity());
//        verify(customerRepository, times(1)).save(customer);
//    }
//
//    @Test
//    @DisplayName("JUnit test for updateCustomer operation - throw CustomerException when ID is null")
//    public void givenNullCustomerId_whenUpdateCustomer_thenThrowCustomerException() {
//        // when - action or the behaviour
//        assertThrows(CustomerException.class, () -> {
//            customerService.updateCustomer(null, customer);
//        });
//        
//        // then - verify the output
//        verify(customerRepository, never()).save(any(Customer.class));
//    }
//
//    @Test
//    @DisplayName("JUnit test for updateCustomer operation - throw CustomerException when customer object is null")
//    public void givenNullCustomerObject_whenUpdateCustomer_thenThrowCustomerException() {
//        // when - action or the behaviour
//        assertThrows(CustomerException.class, () -> {
//            customerService.updateCustomer(1, null);
//        });
//        
//        // then - verify the output
//        verify(customerRepository, never()).save(any(Customer.class));
//    }
//
//    @Test
//    @DisplayName("JUnit test for updateCustomer operation - throw CustomerException when customer not found")
//    public void givenCustomerId_whenUpdateCustomer_thenThrowCustomerException() {
//        // given - precondition or setup
//        given(customerRepository.findById(anyInt())).willReturn(Optional.empty());
//        
//        // when - action or the behaviour
//        assertThrows(CustomerException.class, () -> {
//            customerService.updateCustomer(1, customer);
//        });
//        
//        // then - verify the output
//        verify(customerRepository, never()).save(any(Customer.class));
//    }
}