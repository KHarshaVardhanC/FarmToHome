package com.ftohbackend.controllertesting;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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
import com.ftohbackend.controller.SellerControllerImpl;
import com.ftohbackend.dto.LoginRequest;
import com.ftohbackend.dto.SellerDTO;
import com.ftohbackend.exception.SellerException;
import com.ftohbackend.model.Mails;
import com.ftohbackend.model.Seller;
import com.ftohbackend.service.MailServiceImpl;
import com.ftohbackend.service.SellerService;

class SellerControllerTest {

    private MockMvc mockMvc;
    
    @Mock
    private SellerService sellerService;
    
    @Mock
    private MailServiceImpl mailService;
    
    @Mock
    private ModelMapper modelMapper;
    
    @InjectMocks
    private SellerControllerImpl sellerController;
    
    private ObjectMapper objectMapper;
    
    private Seller seller;
    private SellerDTO sellerDTO;
    
    // Create a global exception handler for testing
    @ControllerAdvice
    public static class GlobalExceptionHandler {
        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> handleException(Exception ex, WebRequest request) {
            return ResponseEntity.status(401).body(ex.getMessage());
        }
        
        @ExceptionHandler(SellerException.class)
        public ResponseEntity<String> handleSellerException(SellerException ex, WebRequest request) {
            return ResponseEntity.status(401).body(ex.getMessage());
        }
    }
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(sellerController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        
        objectMapper = new ObjectMapper();
        
        // Setup seller test data
        seller = new Seller();
        seller.setSellerId(1);
        seller.setSellerEmail("seller@example.com");
        seller.setSellerFirstName("John");
        seller.setSellerLastName("Smith");
        seller.setSellerMobileNumber("9876543210");
        seller.setSellerPlace("Downtown");
        seller.setSellerCity("Metropolis");
        seller.setSellerState("State");
        seller.setSellerPincode("123456");
        seller.setSellerPassword("password123");
        seller.setSellerStatus("Active");
        
        sellerDTO = new SellerDTO();
        sellerDTO.setSellerId(1);
        sellerDTO.setSellerEmail("seller@example.com");
        sellerDTO.setSellerFirstName("John");
        sellerDTO.setSellerLastName("Smith");
        sellerDTO.setSellerMobileNumber("9876543210");
        sellerDTO.setSellerPlace("Downtown");
        sellerDTO.setSellerCity("Metropolis");
        sellerDTO.setSellerState("State");
        sellerDTO.setSellerPincode("123456");
        sellerDTO.setSellerPassword("password123");
        sellerDTO.setSellerStatus("Active");
        
        // Configure ModelMapper behavior
        when(modelMapper.map(any(SellerDTO.class), any())).thenReturn(seller);
        when(modelMapper.map(any(Seller.class), any())).thenReturn(sellerDTO);
    }

    @Test
    @DisplayName("JUnit test for addSeller operation - Success")
    void givenSellerDTO_whenAddSeller_thenReturnSuccessMessage() throws Exception {
        // given - precondition or setup
        when(mailService.isMailExists(anyString())).thenReturn(false);
        when(sellerService.addSeller(any(Seller.class))).thenReturn("Seller Added Successfully");
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(post("/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sellerDTO)));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("You Registered Successfully"));
        
        verify(mailService, times(1)).addMail(any(Mails.class));
    }
    
    @Test
    @DisplayName("JUnit test for addSeller operation - Email Already Exists")
    void givenSellerDTO_whenAddSellerWithExistingEmail_thenReturnEmailExistsMessage() throws Exception {
        // given - precondition or setup
        when(mailService.isMailExists(anyString())).thenReturn(true);
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(post("/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sellerDTO)));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Provided Email All ready Exists"));
    }

    @Test
    @DisplayName("JUnit test for getSeller by ID operation")
    void givenSellerId_whenGetSeller_thenReturnSellerDTO() throws Exception {
        // given - precondition or setup
        when(sellerService.getSeller(anyInt())).thenReturn(seller);
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(get("/seller/{sellerId}", 1));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sellerId", is(sellerDTO.getSellerId())))
                .andExpect(jsonPath("$.sellerEmail", is(sellerDTO.getSellerEmail())))
                .andExpect(jsonPath("$.sellerFirstName", is(sellerDTO.getSellerFirstName())))
                .andExpect(jsonPath("$.sellerLastName", is(sellerDTO.getSellerLastName())));
    }
    
    @Test
    @DisplayName("JUnit test for getSeller by ID operation - Not Found")
    void givenInvalidSellerId_whenGetSeller_thenThrowSellerException() throws Exception {
        // given - precondition or setup
        int invalidSellerId = 999;
        when(sellerService.getSeller(invalidSellerId)).thenThrow(new SellerException("Seller not found"));
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(get("/seller/{sellerId}", invalidSellerId));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Seller not found"));
    }

    @Test
    @DisplayName("JUnit test for getAllSellers operation")
    void givenSellersList_whenGetAllSellers_thenReturnSellersList() throws Exception {
        // given - precondition or setup
        List<Seller> sellerList = new ArrayList<>();
        sellerList.add(seller);
        
        Seller seller2 = new Seller();
        seller2.setSellerId(2);
        seller2.setSellerEmail("seller2@example.com");
        seller2.setSellerFirstName("Jane");
        seller2.setSellerLastName("Doe");
        sellerList.add(seller2);
        
        when(sellerService.getSeller()).thenReturn(sellerList);
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(get("/seller"));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(sellerList.size())));
    }

    @Test
    @DisplayName("JUnit test for deleteSeller by ID operation")
    void givenSellerId_whenDeleteSeller_thenReturnSuccessMessage() throws Exception {
        // given - precondition or setup
        when(sellerService.deleteSeller(anyInt())).thenReturn("Seller deleted successfully");
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(delete("/seller/{sellerId}", 1));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Seller deleted successfully"));
    }
    
    @Test
    @DisplayName("JUnit test for deleteSeller by ID operation - Not Found")
    void givenInvalidSellerId_whenDeleteSeller_thenThrowSellerException() throws Exception {
        // given - precondition or setup
        int invalidSellerId = 999;
        doThrow(new SellerException("Seller not found")).when(sellerService).deleteSeller(invalidSellerId);
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(delete("/seller/{sellerId}", invalidSellerId));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Seller not found"));
    }

    @Test
    @DisplayName("JUnit test for updateSeller operation")
    void givenSellerDTOAndId_whenUpdateSeller_thenReturnSuccessMessage() throws Exception {
        // given - precondition or setup
        when(sellerService.updateSeller(anyInt(), any(Seller.class))).thenReturn("Seller updated successfully");
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(put("/seller/{sellerId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sellerDTO)));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Seller updated successfully"));
    }
    
    @Test
    @DisplayName("JUnit test for updateSeller status operation")
    void givenSellerIdAndStatus_whenUpdateSellerStatus_thenReturnSuccessMessage() throws Exception {
        // given - precondition or setup
        when(sellerService.updateSeller(anyInt(), anyString())).thenReturn("Seller status updated successfully");
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(put("/seller/{sellerId}/{sellerStatus}", 1, "Inactive"));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Seller status updated successfully"));
    }
    
    @Test
    @DisplayName("JUnit test for loginSeller operation - Success")
    void givenLoginRequest_whenLoginSeller_thenReturnSellerResponse() throws Exception {
        // given - precondition or setup
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("seller@example.com");
        loginRequest.setPassword("password123");
        
        when(sellerService.authenticateSeller(anyString(), anyString())).thenReturn(seller);
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(post("/seller/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sellerId", is(seller.getSellerId())))
                .andExpect(jsonPath("$.sellerEmail", is(seller.getSellerEmail())))
                .andExpect(jsonPath("$.sellerFirstName", is(seller.getSellerFirstName())))
                .andExpect(jsonPath("$.sellerLastName", is(seller.getSellerLastName())));
    }
    
    @Test
    @DisplayName("JUnit test for loginSeller operation - Invalid Credentials")
    void givenInvalidLoginRequest_whenLoginSeller_thenReturn401Status() throws Exception {
        // given - precondition or setup
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("invalid@example.com");
        loginRequest.setPassword("wrongpassword");
        
        when(sellerService.authenticateSeller(anyString(), anyString()))
            .thenThrow(new SellerException("Invalid email or password"));
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(post("/seller/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid email or password"));
    }
}