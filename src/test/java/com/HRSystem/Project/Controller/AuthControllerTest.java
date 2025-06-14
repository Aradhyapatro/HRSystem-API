package com.HRSystem.Project.Controller;

import com.HRSystem.Project.DTO.LoginDTO;
import com.HRSystem.Project.DTO.RegisterDTO;
import com.HRSystem.Project.Service.JwtService;
import com.HRSystem.Project.Service.authService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private authService auth;

    @MockBean
    private JwtService jwt;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegister_Success() throws Exception {
        RegisterDTO dto = RegisterDTO.builder()
                .username("alice")
                .password("pass123")
                .role("USER")
                .build();

        when(auth.regitrationService(any())).thenReturn(ResponseEntity.ok("Registration Successfull"));
        when(jwt.generateToken("alice")).thenReturn("mocked-jwt");

        mockMvc.perform(post("/api/auth/Register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("mocked-jwt"));
    }

    @Test
    void testLogin_Success() throws Exception {
        LoginDTO dto = LoginDTO.builder()
                .username("alice")
                .password("pass123")
                .build();

        when(auth.loginService(any())).thenReturn(ResponseEntity.ok("Login Successfull"));
        when(jwt.generateToken("alice")).thenReturn("mocked-jwt");

        mockMvc.perform(post("/api/auth/Login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("mocked-jwt"));
    }

    @Test
    void testGetRole() throws Exception {
        when(jwt.isRole("mocked-token")).thenReturn("ADMIN");

        mockMvc.perform(get("/api/auth/getRole")
                        .header("Authorization", "Bearer mocked-token"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("ADMIN"));
    }

    @Test
    void testIsExpired() throws Exception {
        when(jwt.isTokenExpired("mocked-token")).thenReturn(false);

        mockMvc.perform(get("/api/auth/isExpired")
                        .header("Authorization", "Bearer mocked-token"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("false"));
    }

    @Test
    void testOnboarding() throws Exception {
        RegisterDTO dto = RegisterDTO.builder()
                .username("alice")
                .password("pass123")
                .build();

        mockMvc.perform(post("/api/auth/Onboarding")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee Onboarded Department to be assigned"));
    }
}
