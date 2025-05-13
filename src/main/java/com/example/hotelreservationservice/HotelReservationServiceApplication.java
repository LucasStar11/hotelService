package com.example.hotelreservationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HotelReservationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelReservationServiceApplication.class, args);
    }

}
