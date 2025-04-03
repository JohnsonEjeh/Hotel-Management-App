package org.ejeh.hotelmanagementmvc.service;

import jakarta.servlet.http.HttpSession;
import org.ejeh.hotelmanagementmvc.model.Hotel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class HotelService {

    private final RestTemplate restTemplate;
    private final String apiBaseUrl = "http://hotel-staff-service";

    public HotelService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private HttpHeaders createHeadersWithToken(HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            throw new IllegalStateException("No JWT token found in session");
        }

        // Remove any surrounding quotes if present
        token = token.replaceAll("^\"|\"$", "").trim();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public List<Hotel> getAllHotels(HttpSession session) {
        HttpHeaders headers = createHeadersWithToken(session);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List<Hotel>> response = restTemplate.exchange(
                apiBaseUrl + "/hotels",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Hotel>>() {}
        );
        return response.getBody();
    }

    public Hotel getHotelById(String hotelId, HttpSession session) {
        HttpHeaders headers = createHeadersWithToken(session);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                apiBaseUrl + "/hotels/" + hotelId,
                HttpMethod.GET,
                entity,
                Hotel.class
        ).getBody();
    }

    public Hotel createHotel(Hotel hotel, HttpSession session) {
        HttpHeaders headers = createHeadersWithToken(session);
        HttpEntity<Hotel> entity = new HttpEntity<>(hotel, headers);

        return restTemplate.exchange(
                apiBaseUrl + "/hotels",
                HttpMethod.POST,
                entity,
                Hotel.class
        ).getBody();
    }

    public Hotel updateHotel(String hotelId, Hotel hotel, HttpSession session) {
        HttpHeaders headers = createHeadersWithToken(session);
        HttpEntity<Hotel> entity = new HttpEntity<>(hotel, headers);

        return restTemplate.exchange(
                apiBaseUrl + "/hotels/" + hotelId,
                HttpMethod.PUT,
                entity,
                Hotel.class
        ).getBody();
    }

    public void deleteHotel(String hotelId, HttpSession session) {
        HttpHeaders headers = createHeadersWithToken(session);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        restTemplate.exchange(
                apiBaseUrl + "/hotels/" + hotelId,
                HttpMethod.DELETE,
                entity,
                Void.class
        );
    }
}