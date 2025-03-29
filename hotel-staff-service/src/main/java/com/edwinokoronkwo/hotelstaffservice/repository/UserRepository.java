package com.edwinokoronkwo.hotelstaffservice.repository;

import com.edwinokoronkwo.hotelstaffservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
