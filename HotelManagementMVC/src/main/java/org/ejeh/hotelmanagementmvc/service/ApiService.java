package org.ejeh.hotelmanagementmvc.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ejeh.hotelmanagementmvc.dto.LoginRequest;
import org.ejeh.hotelmanagementmvc.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.io.IOException;

@Service
public class ApiService {
    private final RestTemplate restTemplate;

    private final String loginApi = "http://hotel-staff-service/api/auth/public/login";

    @Autowired
    public ApiService(@LoadBalanced RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String registerUser(RegisterRequest registerRequest) {
        String serviceUrl = "http://hotel-staff-service/api/auth/public/register";
        ResponseEntity<String> response = restTemplate.postForEntity(
                serviceUrl,
                registerRequest,
                String.class
        );
        return response.getBody();
    }


    public String authenticateUser(LoginRequest loginRequest) {
        ResponseEntity<String> response = restTemplate.postForEntity(
                loginApi,
                loginRequest,
                String.class
        );

        // Parse the JSON response to extract the token
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            return root.path("token").asText(); // Extract just the token value
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse authentication response", e);
        }
    }

}
