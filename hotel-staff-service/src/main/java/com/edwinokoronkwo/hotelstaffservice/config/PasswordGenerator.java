//package com.edwinokoronkwo.hotelstaffservice.config;
//
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//public class PasswordGenerator {
//
//    public static void main(String[] args) {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String plainPassword1 = "userpassword"; // Replace with the user's password
//        String plainPassword2 = "adminpassword"; // Replace with the admin's password
//
//        String encodedPassword1 = passwordEncoder.encode(plainPassword1);
//        String encodedPassword2 = passwordEncoder.encode(plainPassword2);
//
//        System.out.println("Encoded password for 'userpassword': " + encodedPassword1);
//        System.out.println("Encoded password for 'adminpassword': " + encodedPassword2);
//    }
//}