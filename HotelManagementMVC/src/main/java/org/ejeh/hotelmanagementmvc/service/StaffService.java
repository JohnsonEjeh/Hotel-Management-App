package org.ejeh.hotelmanagementmvc.service;

import jakarta.servlet.http.HttpSession;
import org.ejeh.hotelmanagementmvc.model.Hotel;
import org.ejeh.hotelmanagementmvc.model.Staff;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StaffService {
    private final RestTemplate restTemplate;
    private final HotelService hotelService;

    private final String apiBaseUrl =  "http://hotel-staff-service";

    public StaffService(RestTemplate restTemplate, HotelService hotelService) {
        this.restTemplate = restTemplate;
        this.hotelService = hotelService;
    }

    private HttpHeaders createHeaders(HttpSession session) {
        String token = (String) session.getAttribute("token");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public List<Staff> getAllStaff(HttpSession session) {
        HttpEntity<String> entity = new HttpEntity<>(createHeaders(session));
        ResponseEntity<List<Staff>> response = restTemplate.exchange(
                apiBaseUrl + "/staff",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Staff>>() {}
        );
        return response.getBody();
    }

    public List<Staff> getAllStaffSortedByDepartment(HttpSession session) {
        HttpEntity<String> entity = new HttpEntity<>(createHeaders(session));
        ResponseEntity<List<Staff>> response = restTemplate.exchange(
                apiBaseUrl + "/staff/sorted",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Staff>>() {}
        );
        return response.getBody();
    }

    public Staff createStaff(Staff staff, HttpSession session) {
        // Enforce default rating
        staff.setPerformanceRating(3);

        // Validate against all hotels
        List<Hotel> allHotels = hotelService.getAllHotels(session);
        staff.validateHotelAssignment(allHotels);

        HttpEntity<Staff> entity = new HttpEntity<>(staff, createHeaders(session));
        return restTemplate.postForObject(apiBaseUrl + "/staff", entity, Staff.class);
    }

    public Staff updateStaff(Long staffId, Staff staff, HttpSession session) {
        // Validate against all hotels
        List<Hotel> allHotels = hotelService.getAllHotels(session);
        staff.validateHotelAssignment(allHotels);

        HttpEntity<Staff> entity = new HttpEntity<>(staff, createHeaders(session));
        return restTemplate.exchange(
                apiBaseUrl + "/staff/" + staffId,
                HttpMethod.PUT,
                entity,
                Staff.class
        ).getBody();
    }

    public void deleteStaff(Long staffId, HttpSession session) {
        HttpEntity<String> entity = new HttpEntity<>(createHeaders(session));
        restTemplate.exchange(
                apiBaseUrl + "/staff/" + staffId,
                HttpMethod.DELETE,
                entity,
                Void.class
        );
    }
    // Add helper method to get eligible hotels
    public List<Hotel> getEligibleHotelsForStaff(int performanceRating, HttpSession session) {
        List<Hotel> allHotels = hotelService.getAllHotels(session);
        return performanceRating >= 4 ?
                allHotels.stream().filter(h -> h.getStarRating() >= 4).collect(Collectors.toList()) :
                allHotels.stream().filter(h -> h.getStarRating() <= 3).collect(Collectors.toList());
    }

    public Staff updateStaffRating(Long staffId, int newRating, HttpSession session) {
        // Get current staff
        Staff currentStaff = getStaffById(staffId, session);

        // Check if new rating would violate rules with current hotel assignment
        if (currentStaff.getHotel() != null) {
            // Need to get the full hotel details to check star rating
            Hotel hotel = hotelService.getHotelById(currentStaff.getHotel().getHotelId(), session);

            if (hotel != null) {
                if (newRating >= 4 && hotel.getStarRating() < 4) {
                    throw new IllegalArgumentException(
                            "Cannot set rating to 4+ while assigned to a hotel with less than 4 stars");
                }
            }
        }

        // Create request entity with headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + session.getAttribute("token"));
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create request body with new rating
        Map<String, Integer> requestBody = Map.of("performanceRating", newRating);
        HttpEntity<Map<String, Integer>> entity = new HttpEntity<>(requestBody, headers);

        // Make the PUT request
        return restTemplate.exchange(
                apiBaseUrl + "/staff/" + staffId + "/rating",
                HttpMethod.PUT,
                entity,
                Staff.class
        ).getBody();
    }

    public Staff getStaffById(Long staffId, HttpSession session) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + session.getAttribute("token"));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Staff> response = restTemplate.exchange(
                apiBaseUrl + "/staff/" + staffId,
                HttpMethod.GET,
                entity,
                Staff.class
        );

        return response.getBody();
    }
}