package com.edwinokoronkwo.hotelstaffservice.repository;


import com.edwinokoronkwo.hotelstaffservice.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    @Query("SELECT s FROM Staff s ORDER BY CASE s.department " +
            "WHEN 'Reception' THEN 1 WHEN 'Cleaning' THEN 2 WHEN 'Management' THEN 3 WHEN 'Restaurant' THEN 4 ELSE 5 END")
    List<Staff> findAllSortedByDepartment();
}
