package com.edwinokoronkwo.hotelstaffservice;

import com.edwinokoronkwo.hotelstaffservice.dto.LoginRequest;
import com.edwinokoronkwo.hotelstaffservice.model.User;
import com.edwinokoronkwo.hotelstaffservice.repository.UserRepository;
import com.edwinokoronkwo.hotelstaffservice.security.jwt.JwtAuthenticationResponse;
import com.edwinokoronkwo.hotelstaffservice.security.jwt.JwtUtils;

import com.edwinokoronkwo.hotelstaffservice.service.UserDetailsImpl;
import com.edwinokoronkwo.hotelstaffservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private UserService userService;

    private User user;
    private LoginRequest loginRequest;
    private Authentication authentication;
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("test@example.com");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        authentication = mock(Authentication.class);
        userDetails = new UserDetailsImpl(1L, "testuser", "encodedPassword", "test@example.com", null);
    }

    @Test
    void registerUser_shouldSaveEncodedUser() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.registerUser(user);

        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(user);

        assertThat(user)
                .isNotNull()
                .extracting(User::getPassword)
                .isEqualTo("encodedPassword");
    }

    @Test
    void loginUser_shouldReturnJwtAuthenticationResponse() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateToken(userDetails)).thenReturn("jwtToken");

        JwtAuthenticationResponse response = userService.loginUser(loginRequest);

        assertThat(response)
                .isNotNull()
                .extracting(JwtAuthenticationResponse::getToken)
                .isEqualTo("jwtToken");

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils, times(1)).generateToken(userDetails);
        assertThat(SecurityContextHolder.getContext().getAuthentication())
                .isNotNull()
                .isEqualTo(authentication);
    }

    @Test
    void findByUsername_shouldReturnUser_whenUserExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        User foundUser = userService.findByUsername("testuser");

        assertThat(foundUser)
                .isNotNull()
                .extracting(User::getUsername)
                .isEqualTo("testuser");
    }

    @Test
    void findByUsername_shouldThrowException_whenUserNotFound() {
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.findByUsername("nonexistentuser"));
    }

//    @Test
//    void registerUser_shouldSaveEncodedUser() {
//        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
//        when(userRepository.save(any(User.class))).thenReturn(user);
//
//        userService.registerUser(user);
//
//        verify(passwordEncoder, times(1)).encode("password");
//        verify(userRepository, times(1)).save(user);
//        assertEquals("encodedPassword", user.getPassword());
//    }
//
//    @Test
//    void loginUser_shouldReturnJwtAuthenticationResponse() {
//        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
//        when(authentication.getPrincipal()).thenReturn(userDetails);
//        when(jwtUtils.generateToken(userDetails)).thenReturn("jwtToken");
//
//        JwtAuthenticationResponse response = userService.loginUser(loginRequest);
//
//        assertNotNull(response);
//        assertEquals("jwtToken", response.getToken()); // Corrected line
//        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
//        verify(jwtUtils, times(1)).generateToken(userDetails);
//        assertEquals(SecurityContextHolder.getContext().getAuthentication(), authentication);
//    }
//
//    @Test
//    void findByUsername_shouldReturnUser_whenUserExists() {
//        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
//
//        User foundUser = userService.findByUsername("testuser");
//
//        assertNotNull(foundUser);
//        assertEquals("testuser", foundUser.getUsername());
//    }
//
//    @Test
//    void findByUsername_shouldThrowException_whenUserNotFound() {
//        when(userRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());
//
//        assertThrows(UsernameNotFoundException.class, () -> userService.findByUsername("nonexistentuser"));
//    }
}