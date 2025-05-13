package com.ftohbackend.controllertesting;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftohbackend.controller.CustomerControllerImpl;
import com.ftohbackend.dto.CustomerDTO;
import com.ftohbackend.dto.LoginRequest;
import com.ftohbackend.exception.CustomerException;
import com.ftohbackend.model.Customer;
import com.ftohbackend.service.CustomerService;
import com.ftohbackend.service.MailServiceImpl;

class CustomerControllerTest {

    private MockMvc mockMvc;
    
    @Mock
    private CustomerService customerService;

    @Mock
    private MailServiceImpl mailServiceImpl;

    @Mock
    private ModelMapper modelMapper;
    
    @InjectMocks
    private CustomerControllerImpl customerController;
    
    private ObjectMapper objectMapper;

    private Customer customer;
    private CustomerDTO customerDTO;
    private LoginRequest loginRequest;
    
    // Create a global exception handler for testing
    @ControllerAdvice
    public static class GlobalExceptionHandler {
        @ExceptionHandler(CustomerException.class)
        public ResponseEntity<String> handleCustomerException(CustomerException ex, WebRequest request) {
            return ResponseEntity.status(404).body(ex.getMessage());
        }
        
        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> handleException(Exception ex, WebRequest request) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(customerController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        
        objectMapper = new ObjectMapper();
        
        // Set up Customer
        customer = new Customer();
        customer.setCustomerId(1);
        customer.setCustomerFirstName("John");
        customer.setCustomerLastName("Doe");
        customer.setCustomerEmail("john.doe@example.com");
        customer.setCustomerPassword("password");
        customer.setCustomerPlace("SomePlace");
        customer.setCustomerCity("SomeCity");
        customer.setCustomerPincode("123456");
        customer.setCustomerState("SomeState");
        customer.setCustomerPhoneNumber("9876543210");
        customer.setCustomerIsActive(true);
        
        // Set up CustomerDTO
        customerDTO = new CustomerDTO();
        customerDTO.setCustomerId(1);
        customerDTO.setCustomerFirstName("John");
        customerDTO.setCustomerLastName("Doe");
        customerDTO.setCustomerEmail("john.doe@example.com");
        customerDTO.setCustomerPassword("password");
        customerDTO.setCustomerPlace("SomePlace");
        customerDTO.setCustomerCity("SomeCity");
        customerDTO.setCustomerPincode("123456");
        customerDTO.setCustomerState("SomeState");
        customerDTO.setCustomerPhoneNumber("9876543210");
        customerDTO.setCustomerIsActive(true);
        
        // Set up LoginRequest
        loginRequest = new LoginRequest();
        loginRequest.setEmail("john.doe@example.com");
        loginRequest.setPassword("password");
    }
    
    @AfterEach
    public void tearDown() {
        customer = null;
        customerDTO = null;
        loginRequest = null;
    }

    @Test
    @DisplayName("JUnit test for addCustomer operation - new email")
    public void givenCustomerDTO_whenAddCustomer_thenReturnSuccessMessage() throws Exception {
        // given - precondition or setup
        when(mailServiceImpl.isMailExists(anyString())).thenReturn(false);
        when(modelMapper.map(any(CustomerDTO.class), any())).thenReturn(customer);
        when(customerService.addCustomer(any(Customer.class))).thenReturn("Customer added successfully");
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO)));
                
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("You Registered Successfully"));
    }
    
    @Test
    @DisplayName("JUnit test for addCustomer operation - existing email")
    public void givenCustomerDTOWithExistingEmail_whenAddCustomer_thenReturnErrorMessage() throws Exception {
        // given - precondition or setup
        when(mailServiceImpl.isMailExists(anyString())).thenReturn(true);

        // when - action or behavior
        ResultActions response = mockMvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isConflict())  // Changed from isCreated() to isConflict()
                .andExpect(content().string("Provided Email Already Exists"));
    }
    
    @Test
    @DisplayName("JUnit test for getCustomer operation")
    public void givenCustomerId_whenGetCustomer_thenReturnCustomerDTO() throws Exception {
        // given - precondition or setup
        when(customerService.getCustomer(anyInt())).thenReturn(customer);
        when(modelMapper.map(any(Customer.class), any())).thenReturn(customerDTO);
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(get("/customer/{customerId}", 1));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId", is(customerDTO.getCustomerId())))
                .andExpect(jsonPath("$.customerFirstName", is(customerDTO.getCustomerFirstName())))
                .andExpect(jsonPath("$.customerLastName", is(customerDTO.getCustomerLastName())))
                .andExpect(jsonPath("$.customerEmail", is(customerDTO.getCustomerEmail())));
    }
    
    @Test
    @DisplayName("JUnit test for getCustomer operation - CustomerException")
    public void givenInvalidCustomerId_whenGetCustomer_thenThrowsCustomerException() throws Exception {
        // given - precondition or setup
        int invalidCustomerId = 999;
        when(customerService.getCustomer(invalidCustomerId)).thenThrow(new CustomerException("Customer not found with ID: " + invalidCustomerId));
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(get("/customer/{customerId}", invalidCustomerId));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Customer not found with ID: " + invalidCustomerId));
    }
    
    @Test
    @DisplayName("JUnit test for getAllCustomers operation")
    public void givenCustomersList_whenGetAllCustomers_thenReturnCustomerDTOList() throws Exception {
        // given - precondition or setup
        List<Customer> customers = new ArrayList<>();
        customers.add(customer);
        
        when(customerService.getAllCustomers()).thenReturn(customers);
        when(modelMapper.map(any(Customer.class), any())).thenReturn(customerDTO);
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(get("/customer/"));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(customers.size())));
    }
    
    @Test
    @DisplayName("JUnit test for loginCustomer operation - successful login")
    public void givenLoginRequest_whenLoginCustomer_thenReturnCustomerResponse() throws Exception {
        // given - precondition or setup
        when(customerService.authenticateCustomer(anyString(), anyString())).thenReturn(customer);
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(post("/customer/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));
                
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId", is(customer.getCustomerId())))
                .andExpect(jsonPath("$.customerEmail", is(customer.getCustomerEmail())));
    }
    
    @Test
    @DisplayName("JUnit test for loginCustomer operation - failed login")
    public void givenInvalidLoginRequest_whenLoginCustomer_thenReturnUnauthorized() throws Exception {
        // given - precondition or setup
        when(customerService.authenticateCustomer(anyString(), anyString())).thenReturn(null);
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(post("/customer/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));
                
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid email or password"));
    }
    
    @Test
    @DisplayName("JUnit test for updateCustomer operation")
    public void givenCustomerIdAndCustomerDTO_whenUpdateCustomer_thenReturnSuccessMessage() throws Exception {
        // given - precondition or setup
        when(modelMapper.map(any(CustomerDTO.class), any())).thenReturn(customer);
        when(customerService.updateCustomer(anyInt(), any(Customer.class))).thenReturn("Customer updated successfully");
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(put("/customer/{customerId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO)));
                
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Customer updated successfully"));
    }
    
    @Test
    @DisplayName("JUnit test for updateCustomer operation - CustomerException")
    public void givenInvalidCustomerId_whenUpdateCustomer_thenThrowsCustomerException() throws Exception {
        // given - precondition or setup
        int invalidCustomerId = 999;
        when(modelMapper.map(any(CustomerDTO.class), any())).thenReturn(customer);
        when(customerService.updateCustomer(invalidCustomerId, customer)).thenThrow(new CustomerException("Customer not found with ID: " + invalidCustomerId));
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(put("/customer/{customerId}", invalidCustomerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO)));
                
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Customer not found with ID: " + invalidCustomerId));
    }
}