package com.ftohbackend.controllertesting;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftohbackend.controller.AdminControllerImpl;
import com.ftohbackend.dto.LoginRequest;

public class AdminControllerTest {
    
    private MockMvc mockMvc;
    
    @InjectMocks
    private AdminControllerImpl adminController;
    
    private ObjectMapper objectMapper;
    
    private LoginRequest validLoginRequest;
    private LoginRequest invalidLoginRequest;

    @BeforeEach
    public void setUp() {
        // Initialize mockito annotations
        MockitoAnnotations.openMocks(this);
        
        // Set up ObjectMapper
        objectMapper = new ObjectMapper();
        
        // Configure mockMvc with standaloneSetup (without loading full Spring context)
        mockMvc = MockMvcBuilders
                .standaloneSetup(adminController)
                .build();
            
        // Valid credentials setup
        validLoginRequest = new LoginRequest();
        validLoginRequest.setEmail("villagecart@gmail.com");
        validLoginRequest.setPassword("0000");

        // Invalid credentials setup
        invalidLoginRequest = new LoginRequest();
        invalidLoginRequest.setEmail("wrong@example.com");
        invalidLoginRequest.setPassword("wrongpassword");
    }

    @Test
    @DisplayName("JUnit test for successful admin login")
    public void givenValidCredentials_whenAdminLogin_thenReturnAdminResponse() throws Exception {
        // given - precondition or setup already done in setUp()

        // when - action or behavior
        ResultActions response = mockMvc.perform(post("/admin/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(validLoginRequest)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adminId", is(1)))
                .andExpect(jsonPath("$.adminEmail", is("villagecart@gmail.com")));
    }

    @Test
    @DisplayName("JUnit test for failed admin login with invalid credentials")
    public void givenInvalidCredentials_whenAdminLogin_thenReturnUnauthorized() throws Exception {
        // given - precondition or setup already done in setUp()

        // when - action or behavior
        ResultActions response = mockMvc.perform(post("/admin/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(invalidLoginRequest)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid email or password"));
    }

    @Test
    @DisplayName("JUnit test for failed admin login with valid email but wrong password")
    public void givenValidEmailButWrongPassword_whenAdminLogin_thenReturnUnauthorized() throws Exception {
        // given - precondition or setup
        LoginRequest partiallyValidRequest = new LoginRequest();
        partiallyValidRequest.setEmail("villagecart@gmail.com");
        partiallyValidRequest.setPassword("wrongpassword");

        // when - action or behavior
        ResultActions response = mockMvc.perform(post("/admin/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(partiallyValidRequest)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid email or password"));
    }

    @Test
    @DisplayName("JUnit test for failed admin login with wrong email but valid password")
    public void givenWrongEmailButValidPassword_whenAdminLogin_thenReturnUnauthorized() throws Exception {
        // given - precondition or setup
        LoginRequest partiallyValidRequest = new LoginRequest();
        partiallyValidRequest.setEmail("wrong@example.com");
        partiallyValidRequest.setPassword("0000");

        // when - action or behavior
        ResultActions response = mockMvc.perform(post("/admin/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(partiallyValidRequest)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid email or password"));
    }
}