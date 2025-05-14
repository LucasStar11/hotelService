package com.example.hotelreservationservice.config;


import com.example.hotelreservationservice.db.model.BookingModel;
import com.example.hotelreservationservice.db.model.RoomModel;
import com.example.hotelreservationservice.db.model.UserModel;
import com.example.hotelreservationservice.db.model.enums.BookingStatusEnum;
import com.example.hotelreservationservice.db.model.enums.RoleEnum;
import com.example.hotelreservationservice.db.model.enums.RoomStatusEnum;
import com.example.hotelreservationservice.db.repository.BookingRepository;
import com.example.hotelreservationservice.db.repository.RoomRepository;
import com.example.hotelreservationservice.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData(
            UserRepository userRepository,
            RoomRepository roomRepository,
            BookingRepository bookingRepository
    ) {
        return args -> {
            // Инициализация только если нет данных
            if (userRepository.count() == 0) {
                initUsers(userRepository);
                initRooms(roomRepository);
                initBookings(bookingRepository, roomRepository);
            }
        };
    }

    private void initUsers(UserRepository userRepository) {
        List<UserModel> users = List.of(
                createUser("admin", "admin123", RoleEnum.ADMIN),
                createUser("hostess", "hostess123", RoleEnum.HOSTESS),
                createUser("reception", "reception123", RoleEnum.HOSTESS),
                createUser("manager", "manager123", RoleEnum.ADMIN),
                createUser("staff", "staff123", RoleEnum.WORKER)
        );
        userRepository.saveAll(users);
    }

    private UserModel createUser(String username, String password, RoleEnum role) {
        UserModel user = new UserModel();
        user.setUserName(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return user;
    }

    private void initRooms(RoomRepository roomRepository) {
        List<RoomModel> rooms = List.of(
                createRoom("101", RoomStatusEnum.AVAILABLE),
                createRoom("102", RoomStatusEnum.BOOKED),
                createRoom("103", RoomStatusEnum.OCCUPIED),
                createRoom("104", RoomStatusEnum.AVAILABLE),
                createRoom("105", RoomStatusEnum.BOOKED)
        );
        roomRepository.saveAll(rooms);
    }

    private RoomModel createRoom(String number, RoomStatusEnum status) {
        RoomModel room = new RoomModel();
        room.setNumber(number);
        room.setStatus(status);
        return room;
    }

    private void initBookings(BookingRepository bookingRepository, RoomRepository roomRepository) {
        List<RoomModel> rooms = roomRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        List<BookingModel> bookings = List.of(
                createBooking(rooms.get(0)), // 101 - AVAILABLE
                createBooking(rooms.get(1)), // 102 - BOOKED
                createBooking(rooms.get(2)), // 103 - OCCUPIED
                createBooking(rooms.get(3)), // 104 - AVAILABLE
                createBooking(rooms.get(4))  // 105 - BOOKED
        );

        // Настраиваем разные статусы и даты
        bookings.get(0).setStatus(BookingStatusEnum.ACTIVE);
        bookings.get(1).setStatus(BookingStatusEnum.ACTIVE);
        bookings.get(2).setStatus(BookingStatusEnum.ACTIVE);
        bookings.get(3).setStatus(BookingStatusEnum.ACTIVE);
        bookings.get(4).setStatus(BookingStatusEnum.ACTIVE);

        bookings.forEach(booking -> {
            booking.setStartDate(now.minusDays(new Random().nextInt(5)).withHour(14).withMinute(0).withSecond(0));
            booking.setEndDate(now.plusDays(new Random().nextInt(10) + 5).withHour(12).withMinute(0).withSecond(0));
        });

        bookingRepository.saveAll(bookings);
    }

    private BookingModel createBooking(RoomModel room) {
        BookingModel booking = new BookingModel();
        booking.setRoom(room);
        booking.setVisitor("Guest " + UUID.randomUUID().toString().substring(0, 5));
        return booking;
    }
}