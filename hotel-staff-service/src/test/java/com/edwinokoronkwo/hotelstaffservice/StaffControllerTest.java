package com.edwinokoronkwo.hotelstaffservice;


import com.edwinokoronkwo.hotelstaffservice.controller.StaffController;
import com.edwinokoronkwo.hotelstaffservice.exception.InvalidHotelAssignmentException;
import com.edwinokoronkwo.hotelstaffservice.model.Hotel;
import com.edwinokoronkwo.hotelstaffservice.model.Staff;
import com.edwinokoronkwo.hotelstaffservice.service.StaffService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.ComponentScan; // Import ComponentScan
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(StaffController.class)
@AutoConfigureMockMvc(addFilters = false)
@ComponentScan("com.edwinokoronkwo.hotelstaffservice.security.jwt") // Include JwtUtils package
class StaffControllerTest {

    @MockitoBean
    private StaffService staffService;

    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void updateStaffRating_shouldReturnUpdatedStaff_whenStaffExists() throws Exception {
        // Arrange
        Long staffId = 1L;
        int newRating = 5;
        Staff updatedStaff = new Staff();
        updatedStaff.setStaffId(staffId);
        updatedStaff.setPerformanceRating(newRating);

        when(staffService.updateStaffPerformanceRating(staffId, newRating)).thenReturn(updatedStaff);

        // Act & Assert
        mockMvc.perform(put("/staff/{staffId}/rating", staffId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(java.util.Map.of("performanceRating", newRating)))) // Send request body
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.performanceRating").value(newRating));
    }

    @Test
    void updateStaffRating_shouldReturnNotFound_whenStaffNotFound() throws Exception {
        // Arrange
        Long staffId = 1L;
        int newRating = 5;

        when(staffService.updateStaffPerformanceRating(staffId, newRating)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/staff/{staffId}/rating", staffId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(java.util.Map.of("performanceRating", newRating)))) // Send request body
                .andExpect(status().isNotFound());
    }

    @Test
    void updateStaffRating_shouldReturnBadRequest_whenInvalidHotelAssignment() throws Exception {
        // Arrange
        Long staffId = 1L;
        int newRating = 4;

        when(staffService.updateStaffPerformanceRating(staffId, newRating)).thenThrow(new InvalidHotelAssignmentException("Invalid hotel assignment"));

        // Act & Assert
        mockMvc.perform(put("/staff/{staffId}/rating", staffId)
                        .param("performanceRating", String.valueOf(newRating))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllStaff_shouldReturnAllStaff() throws Exception {
        // Arrange
        List<Staff> staffList = Arrays.asList(new Staff(), new Staff());
        when(staffService.getAllStaff()).thenReturn(staffList);

        // Act & Assert
        mockMvc.perform(get("/staff"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }



    @Test
    void getStaffById_shouldReturnStaff_whenStaffExists() throws Exception {
        // Arrange
        Long staffId = 1L;
        Staff staff = new Staff();
        staff.setStaffId(staffId);
        System.out.println("Mock setup: staffService.getStaffById(" + staffId + ") -> " + staff); // Debugging
        when(staffService.getStaffById(staffId)).thenReturn(staff);

        // Act & Assert
        mockMvc.perform(get("/staff/{staffId}", staffId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.staffId").value(staffId));
    }


    @Test
    void getStaffById_shouldReturnNotFound_whenStaffNotFound() throws Exception {
        // Arrange
        Long staffId = 1L;
        when(staffService.getStaffById(staffId)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/staff/{staffId}", staffId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createStaff_shouldCreateStaff() throws Exception {
        // Arrange
        Staff staff = new Staff();
        staff.setStaffName("Test Staff");
        Hotel hotel = new Hotel();
        staff.setHotel(hotel);
        when(staffService.createStaff(any(Staff.class), any(Hotel.class))).thenReturn(staff);

        // Act & Assert
        mockMvc.perform(post("/staff")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(staff)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.staffName").value("Test Staff"));
    }

    @Test
    void updateStaff_shouldUpdateStaff() throws Exception {
        // Arrange
        Long staffId = 1L;
        Staff staff = new Staff();
        staff.setStaffName("Updated Staff");
        Hotel hotel = new Hotel();
        staff.setHotel(hotel);
        when(staffService.updateStaff(eq(staffId), any(Staff.class), any(Hotel.class))).thenReturn(staff);

        // Act & Assert
        mockMvc.perform(put("/staff/{staffId}", staffId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(staff)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.staffName").value("Updated Staff"));
    }

    @Test
    void updateStaff_shouldReturnNotFound_whenStaffNotFound() throws Exception {
        // Arrange
        Long staffId = 1L;
        Staff staff = new Staff();
        staff.setStaffName("Test Staff");
        Hotel hotel = new Hotel();
        hotel.setHotelId("TestHotelID");
        staff.setHotel(hotel);
        staff.setDepartment("Test Department");
        staff.setPerformanceRating(4);

        when(staffService.updateStaff(eq(staffId), any(Staff.class), any(Hotel.class))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/staff/{staffId}", staffId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(staff)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteStaff_shouldDeleteStaff() throws Exception {
        // Arrange
        Long staffId = 1L;

        // Act & Assert
        mockMvc.perform(delete("/staff/{staffId}", staffId))
                .andExpect(status().isNoContent());

        verify(staffService, times(1)).deleteStaff(staffId);
    }

    @Test
    void getAllStaffSorted_shouldReturnSortedStaff() throws Exception {
        // Arrange
        List<Staff> staffList = Arrays.asList(new Staff(), new Staff());
        when(staffService.getAllStaffSortedByDepartment()).thenReturn(staffList);

        // Act & Assert
        mockMvc.perform(get("/staff/sorted"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }
}