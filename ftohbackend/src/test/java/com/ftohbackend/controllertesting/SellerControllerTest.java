package com.ftohbackend.controllertesting;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftohbackend.controller.SellerControllerImpl;
import com.ftohbackend.dto.LoginRequest;
import com.ftohbackend.dto.SellerDTO;
import com.ftohbackend.exception.SellerException;
import com.ftohbackend.model.Mails;
import com.ftohbackend.model.Seller;
import com.ftohbackend.service.MailServiceImpl;
import com.ftohbackend.service.SellerService;

@WebMvcTest
@ContextConfiguration(classes = { SellerControllerImpl.class })//@AutoConfigureMockMvc

public class SellerControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SellerService sellerService;
    
    @MockBean
    private MailServiceImpl mailService;
    
    @MockBean
    private org.modelmapper.ModelMapper modelMapper;

    private Seller seller;
    private SellerDTO sellerDTO;

    @BeforeEach
    public void setUp() {
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
        given(modelMapper.map(any(SellerDTO.class), eq(Seller.class))).willReturn(seller);
        given(modelMapper.map(any(Seller.class), eq(SellerDTO.class))).willReturn(sellerDTO);
    }

    @AfterEach
    public void tearDown() {
        seller = null;
        sellerDTO = null;
    }

    @Test
    @DisplayName("JUnit test for addSeller operation - Success")
    public void givenSellerDTO_whenAddSeller_thenReturnSuccessMessage() throws Exception {
        // given - precondition or setup
        given(mailService.isMailExists(anyString())).willReturn(false);
        given(sellerService.addSeller(any(Seller.class))).willReturn("Seller Added Successfully");
        willDoNothing().given(mailService).addMail(any(Mails.class));
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(post("/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sellerDTO)));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("You Registered Successfully"));
    }
    
    @Test
    @DisplayName("JUnit test for addSeller operation - Email Already Exists")
    public void givenSellerDTO_whenAddSellerWithExistingEmail_thenReturnEmailExistsMessage() throws Exception {
        // given - precondition or setup
        given(mailService.isMailExists(anyString())).willReturn(true);
        
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
    public void givenSellerId_whenGetSeller_thenReturnSellerDTO() throws Exception {
        // given - precondition or setup
        given(sellerService.getSeller(anyInt())).willReturn(seller);
        
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
    public void givenInvalidSellerId_whenGetSeller_thenThrowSellerException() throws Exception {
        // given - precondition or setup
        given(sellerService.getSeller(anyInt())).willThrow(new SellerException("Seller not found"));
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(get("/seller/{sellerId}", 999));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("JUnit test for getAllSellers operation")
    public void givenSellersList_whenGetAllSellers_thenReturnSellersList() throws Exception {
        // given - precondition or setup
        List<Seller> sellerList = new ArrayList<>();
        sellerList.add(seller);
        
        Seller seller2 = new Seller();
        seller2.setSellerId(2);
        seller2.setSellerEmail("seller2@example.com");
        seller2.setSellerFirstName("Jane");
        seller2.setSellerLastName("Doe");
        sellerList.add(seller2);
        
        given(sellerService.getSeller()).willReturn(sellerList);
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(get("/seller"));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(sellerList.size())));
    }

    @Test
    @DisplayName("JUnit test for deleteSeller by ID operation")
    public void givenSellerId_whenDeleteSeller_thenReturnSuccessMessage() throws Exception {
        // given - precondition or setup
        given(sellerService.deleteSeller(anyInt())).willReturn("Seller deleted successfully");
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(delete("/seller/{sellerId}", 1));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Seller deleted successfully"));
    }
    
    @Test
    @DisplayName("JUnit test for deleteSeller by ID operation - Not Found")
    public void givenInvalidSellerId_whenDeleteSeller_thenThrowSellerException() throws Exception {
        // given - precondition or setup
        given(sellerService.deleteSeller(anyInt())).willThrow(new SellerException("Seller not found"));
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(delete("/seller/{sellerId}", 999));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("JUnit test for updateSeller operation")
    public void givenSellerDTOAndId_whenUpdateSeller_thenReturnSuccessMessage() throws Exception {
        // given - precondition or setup
        given(sellerService.updateSeller(anyInt(), any(Seller.class))).willReturn("Seller updated successfully");
        
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
    public void givenSellerIdAndStatus_whenUpdateSellerStatus_thenReturnSuccessMessage() throws Exception {
        // given - precondition or setup
        given(sellerService.updateSeller(anyInt(), anyString())).willReturn("Seller status updated successfully");
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(put("/seller/{sellerId}/{sellerStatus}", 1, "Inactive"));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Seller status updated successfully"));
    }
    
    @Test
    @DisplayName("JUnit test for loginSeller operation - Success")
    public void givenLoginRequest_whenLoginSeller_thenReturnSellerResponse() throws Exception {
        // given - precondition or setup
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("seller@example.com");
        loginRequest.setPassword("password123");
        
        given(sellerService.authenticateSeller(anyString(), anyString())).willReturn(seller);
        
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
    public void givenInvalidLoginRequest_whenLoginSeller_thenReturn401Status() throws Exception {
        // given - precondition or setup
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("invalid@example.com");
        loginRequest.setPassword("wrongpassword");
        
        given(sellerService.authenticateSeller(anyString(), anyString()))
            .willThrow(new SellerException("Invalid email or password"));
        
        // when - action or behavior
        ResultActions response = mockMvc.perform(post("/seller/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));
        
        // then - verify the output
        response.andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid email or password"));
    }
    
    // Helper method to make the tests more readable
    private static <T> T eq(T value) {
        return org.mockito.ArgumentMatchers.eq(value);
    }
}