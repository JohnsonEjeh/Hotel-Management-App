package com.edwinokoronkwo.hotelstaffservice;

import com.edwinokoronkwo.hotelstaffservice.controller.AuthController;

import com.edwinokoronkwo.hotelstaffservice.dto.LoginRequest;
import com.edwinokoronkwo.hotelstaffservice.dto.RegisterRequest;

import com.edwinokoronkwo.hotelstaffservice.model.User;
import com.edwinokoronkwo.hotelstaffservice.security.jwt.JwtAuthenticationResponse;
import com.edwinokoronkwo.hotelstaffservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoginUser() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        JwtAuthenticationResponse jwtResponse = new JwtAuthenticationResponse("test_jwt_token");
        when(userService.loginUser(loginRequest)).thenReturn(jwtResponse);

        ResponseEntity<?> response = authController.loginUser(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jwtResponse, response.getBody());
        verify(userService, times(1)).loginUser(loginRequest);
    }

    @Test
    public void testRegisterUser() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("newuser");
        registerRequest.setEmail("newuser@example.com");
        registerRequest.setPassword("newpassword");
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        registerRequest.setRole(roles);

        ResponseEntity<?> response = authController.registerUser(registerRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());
        verify(userService, times(1)).registerUser(any(User.class));
    }

    @Test
    public void testRegisterUser_withEmptyRoles(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("newuser");
        registerRequest.setEmail("newuser@example.com");
        registerRequest.setPassword("newpassword");

        ResponseEntity<?> response = authController.registerUser(registerRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());
        verify(userService, times(1)).registerUser(any(User.class));

    }
}
