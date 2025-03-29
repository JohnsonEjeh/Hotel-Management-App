package com.edwinokoronkwo.hotelstaffservice.service;


import com.edwinokoronkwo.hotelstaffservice.dto.LoginRequest;
import com.edwinokoronkwo.hotelstaffservice.model.User;
import com.edwinokoronkwo.hotelstaffservice.repository.UserRepository;
import com.edwinokoronkwo.hotelstaffservice.security.jwt.JwtAuthenticationResponse;
import com.edwinokoronkwo.hotelstaffservice.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // Generate constructor with required dependencies
public class UserService {

    private final PasswordEncoder passwordEncoder; // Make final
    private final UserRepository userRepository; // Make final
    private final AuthenticationManager authenticationManager; // Make final
    private final JwtUtils jwtUtils; // Make final

    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public JwtAuthenticationResponse loginUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetails);
        return new JwtAuthenticationResponse(jwt);

    }

    public User findByUsername(String name) {
        return userRepository.findByUsername(name).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + name)
        );
    }
}
