package com.ftohbackend.controllertesting;


import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftohbackend.controller.CustomerControllerImpl;
import com.ftohbackend.dto.CustomerDTO;
import com.ftohbackend.dto.LoginRequest;
import com.ftohbackend.exception.CustomerException;
import com.ftohbackend.model.Customer;
import com.ftohbackend.service.CustomerService;
import com.ftohbackend.service.MailServiceImpl;

@WebMvcTest
@ContextConfiguration(classes = { CustomerControllerImpl.class })//@AutoConfigureMockMvc
//@Import({ObjectMapper.class, ModelMapper.class})
public class CustomerControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private MailServiceImpl mailServiceImpl;

    @MockBean
    private ModelMapper modelMapper;

    private Customer customer;
    private CustomerDTO customerDTO;
    private LoginRequest loginRequest;

    @BeforeEach
    public void setUp() {
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
        given(mailServiceImpl.isMailExists(anyString())).willReturn(false);
        given(modelMapper.map(any(CustomerDTO.class), any())).willReturn(customer);
        given(customerService.addCustomer(any(Customer.class))).willReturn("Customer added successfully");
        
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
        given(mailServiceImpl.isMailExists(anyString())).willReturn(true);
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO)));
                
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("Provided Email All ready Exists"));
    }
    
    @Test
    @DisplayName("JUnit test for getCustomer operation")
    public void givenCustomerId_whenGetCustomer_thenReturnCustomerDTO() throws Exception {
        // given - precondition or setup
        given(customerService.getCustomer(anyInt())).willReturn(customer);
        given(modelMapper.map(any(Customer.class), any())).willReturn(customerDTO);
        
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
        given(customerService.getCustomer(anyInt())).willThrow(CustomerException.class);
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(get("/customer/{customerId}", 999));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isInternalServerError());
    }
    
    @Test
    @DisplayName("JUnit test for getAllCustomers operation")
    public void givenCustomersList_whenGetAllCustomers_thenReturnCustomerDTOList() throws Exception {
        // given - precondition or setup
        List<Customer> customers = new ArrayList<>();
        customers.add(customer);
        
        List<CustomerDTO> customerDTOs = new ArrayList<>();
        customerDTOs.add(customerDTO);
        
        given(customerService.getAllCustomers()).willReturn(customers);
        given(modelMapper.map(any(Customer.class), any())).willReturn(customerDTO);
        
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
        given(customerService.authenticateCustomer(anyString(), anyString())).willReturn(customer);
        
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
        given(customerService.authenticateCustomer(anyString(), anyString())).willReturn(null);
        
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
        given(modelMapper.map(any(CustomerDTO.class), any())).willReturn(customer);
        given(customerService.updateCustomer(anyInt(), any(Customer.class))).willReturn("Customer updated successfully");
        
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
        given(modelMapper.map(any(CustomerDTO.class), any())).willReturn(customer);
        given(customerService.updateCustomer(anyInt(), any(Customer.class))).willThrow(CustomerException.class);
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(put("/customer/{customerId}", 999)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO)));
                
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isInternalServerError());
    }
}