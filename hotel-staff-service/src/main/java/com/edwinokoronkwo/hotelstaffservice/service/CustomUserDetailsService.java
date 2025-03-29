//package com.edwinokoronkwo.hotelstaffservice.service;
//
//import com.edwinokoronkwo.hotelstaffservice.model.User;
//import com.edwinokoronkwo.hotelstaffservice.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
//
//        System.out.println("User from DB: " + user); // Debug the entire user object
//        System.out.println("Roles from DB: " + user.getRoles()); // Debug the roles string
//
//        List<String> roles = Arrays.asList(user.getRoles().split(","));
//
//        System.out.println("User: " + username + ", Roles: " + roles); // Debug the roles list
//
//        return org.springframework.security.core.userdetails.User.builder()
//                .username(user.getUsername())
//                .password(user.getPassword())
//                .roles(roles.toArray(new String[0]))
//                .build();
//    }
//}