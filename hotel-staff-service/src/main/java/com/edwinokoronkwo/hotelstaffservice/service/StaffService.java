package com.edwinokoronkwo.hotelstaffservice.service;

import com.edwinokoronkwo.hotelstaffservice.model.Staff;
import com.edwinokoronkwo.hotelstaffservice.model.Hotel;
import java.util.List;

public interface StaffService {
    List<Staff> getAllStaff();
    Staff getStaffById(Long staffId);
    Staff createStaff(Staff staff, Hotel hotel); // Modified method signature
    Staff updateStaff(Long staffId, Staff staff, Hotel hotel); // Modified method signature
    void deleteStaff(Long staffId);

    List<Staff> getAllStaffSortedByDepartment();
    Staff updateStaffPerformanceRating(Long staffId, int performanceRating);
}