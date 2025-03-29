package com.edwinokoronkwo.hotelstaffservice;

import com.edwinokoronkwo.hotelstaffservice.exception.InvalidHotelAssignmentException;
import com.edwinokoronkwo.hotelstaffservice.model.Staff;
import com.edwinokoronkwo.hotelstaffservice.model.Hotel;
import com.edwinokoronkwo.hotelstaffservice.repository.StaffRepository;
import com.edwinokoronkwo.hotelstaffservice.service.StaffService;
import com.edwinokoronkwo.hotelstaffservice.service.StaffServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StaffServiceImplTest {

    @Mock
    private StaffRepository staffRepository;


    @InjectMocks
    private StaffServiceImpl staffService;


    private Staff staff;
    private Hotel hotel;

    @BeforeEach
    void setUp() {
        staff = new Staff();
        staff.setStaffId(1L);
        staff.setStaffName("Test Staff");

        hotel = new Hotel();
        hotel.setHotelId("Test1234");
        hotel.setHotelName("Test Hotel");
        hotel.setStarRating(3);
    }


    @Test
    void createStaff_invalidRating_shouldThrowException() {
        staff.setPerformanceRating(5);
        hotel.setStarRating(3);

        assertThrows(IllegalArgumentException.class, () -> staffService.createStaff(staff, hotel));
        verify(staffRepository, never()).save(any(Staff.class));
    }

    @Test
    void updateStaff_valid_shouldSaveStaff() {
        when(staffRepository.save(staff)).thenReturn(staff);

        Staff updatedStaff = staffService.updateStaff(1L, staff, hotel);

        assertEquals(staff, updatedStaff);
        verify(staffRepository, times(1)).save(staff);
    }



    @Test
    void createStaff_valid_shouldSaveStaff() {
        when(staffRepository.save(staff)).thenReturn(staff);

        Staff createdStaff = staffService.createStaff(staff, hotel);

        assertEquals(staff, createdStaff);
        verify(staffRepository, times(1)).save(staff);
    }

    @Test
    void createStaff_defaultPerformanceRating_shouldBe3() {
        when(staffRepository.save(staff)).thenReturn(staff);

        Staff createdStaff = staffService.createStaff(staff, hotel);

        assertEquals(3, createdStaff.getPerformanceRating());
        verify(staffRepository, times(1)).save(staff);
    }



    @Test
    void updateStaff_invalidRating_shouldThrowException() {
        staff.setPerformanceRating(5);
        hotel.setStarRating(3);

        assertThrows(IllegalArgumentException.class, () -> staffService.updateStaff(1L, staff, hotel));
        verify(staffRepository, never()).save(any(Staff.class));
    }

    @Test
    void getStaffById_validId_shouldReturnStaff() {
        when(staffRepository.findById(1L)).thenReturn(Optional.of(staff));

        Staff foundStaff = staffService.getStaffById(1L);

        assertEquals(staff, foundStaff);
        verify(staffRepository, times(1)).findById(1L);
    }

    @Test
    void getStaffById_invalidId_shouldReturnNull() {
        when(staffRepository.findById(1L)).thenReturn(Optional.empty());

        Staff foundStaff = staffService.getStaffById(1L);

        assertNull(foundStaff);
        verify(staffRepository, times(1)).findById(1L);
    }

    @Test
    void deleteStaff_shouldCallDeleteById() {
        staffService.deleteStaff(1L);
        verify(staffRepository, times(1)).deleteById(1L);
    }

    @Test
    void getAllStaffSortedByDepartment_shouldReturnSortedList() {
        // Set up test data
        Staff staff1 = new Staff();
        staff1.setStaffName("Staff D");
        staff1.setDepartment("Reception");

        Staff staff2 = new Staff();
        staff2.setStaffName("Staff E");
        staff2.setDepartment("Reception");

        Staff staff3 = new Staff();
        staff3.setStaffName("Staff A");
        staff3.setDepartment("Cleaning");

        Staff staff4 = new Staff();
        staff4.setStaffName("Staff B");
        staff4.setDepartment("Cleaning");

        Staff staff5 = new Staff();
        staff5.setStaffName("Staff F");
        staff5.setDepartment("Management");

        Staff staff6 = new Staff();
        staff6.setStaffName("Staff G");
        staff6.setDepartment("Restaurant");

        List<Staff> staffList = Arrays.asList(staff1, staff2, staff3, staff4, staff5, staff6);

        // Mock the repository
        when(staffRepository.findAllSortedByDepartment()).thenReturn(staffList);

        // Call the service method
        List<Staff> sortedStaffList = staffService.getAllStaffSortedByDepartment();

        // Assert the order
        assertEquals(6, sortedStaffList.size());
        assertEquals("Staff D", sortedStaffList.get(0).getStaffName()); // Reception
        assertEquals("Staff E", sortedStaffList.get(1).getStaffName()); // Reception
        assertEquals("Staff A", sortedStaffList.get(2).getStaffName()); // Cleaning
        assertEquals("Staff B", sortedStaffList.get(3).getStaffName()); // Cleaning
        assertEquals("Staff F", sortedStaffList.get(4).getStaffName()); // Management
        assertEquals("Staff G", sortedStaffList.get(5).getStaffName()); // Restaurant

        verify(staffRepository, times(1)).findAllSortedByDepartment();
    }

    @Test
    void updateStaffPerformanceRating_shouldUpdateRating_whenStaffExists() {
        // Arrange
        Long staffId = 1L;
        int newRating = 5;
        Staff staff = new Staff();
        staff.setStaffId(staffId);
        staff.setPerformanceRating(3);

        when(staffRepository.findById(staffId)).thenReturn(Optional.of(staff));
        when(staffRepository.save(staff)).thenReturn(staff);

        // Act
        Staff updatedStaff = staffService.updateStaffPerformanceRating(staffId, newRating);

        // Assert
        assertEquals(newRating, updatedStaff.getPerformanceRating());
        verify(staffRepository, times(1)).save(staff);
    }

    @Test
    void updateStaffPerformanceRating_shouldReturnNull_whenStaffNotFound() {
        // Arrange
        Long staffId = 1L;
        int newRating = 5;

        when(staffRepository.findById(staffId)).thenReturn(Optional.empty());

        // Act
        Staff updatedStaff = staffService.updateStaffPerformanceRating(staffId, newRating);

        // Assert
        assertNull(updatedStaff);
        verify(staffRepository, never()).save(any());
    }

    @Test
    void updateStaffPerformanceRating_shouldThrowException_whenInvalidHotelAssignment() {
        // Arrange
        Long staffId = 1L;
        int newRating = 4;
        Staff staff = new Staff();
        staff.setStaffId(staffId);
        staff.setPerformanceRating(3);
        Hotel hotel = new Hotel();
        hotel.setStarRating(3);
        staff.setHotel(hotel);

        when(staffRepository.findById(staffId)).thenReturn(Optional.of(staff));

        // Act & Assert
        assertThrows(InvalidHotelAssignmentException.class, () -> staffService.updateStaffPerformanceRating(staffId, newRating));
        verify(staffRepository, never()).save(any());
    }
}