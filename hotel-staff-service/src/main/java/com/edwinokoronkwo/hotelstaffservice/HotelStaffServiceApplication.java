package com.edwinokoronkwo.hotelstaffservice;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDiscoveryClient
public class HotelStaffServiceApplication {
    //testing
    public static void main(String[] args) {
        SpringApplication.run(HotelStaffServiceApplication.class, args);
    }

}
