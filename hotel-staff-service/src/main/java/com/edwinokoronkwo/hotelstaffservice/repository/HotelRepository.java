package com.edwinokoronkwo.hotelstaffservice.repository;


import com.edwinokoronkwo.hotelstaffservice.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, String> {
}
