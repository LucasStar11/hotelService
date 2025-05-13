package com.example.hotelreservationservice.bookings.service;

import com.example.hotelreservationservice.bookings.dto.BookingRequestDTO;
import com.example.hotelreservationservice.db.model.BookingModel;
import com.example.hotelreservationservice.db.model.RoomModel;
import com.example.hotelreservationservice.db.model.enums.BookingStatusEnum;
import com.example.hotelreservationservice.db.model.enums.RoomStatusEnum;
import com.example.hotelreservationservice.db.repository.BookingRepository;
import com.example.hotelreservationservice.db.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public ResponseEntity<?> bookRoom(BookingRequestDTO request) {
        RoomModel room = roomRepository.findById(request.getRoomId())
                .orElseThrow(
                        () -> new IllegalArgumentException("Room with that id not found - " + request.getRoomId())
                );
        if (!RoomStatusEnum.AVAILABLE.equals(room.getStatus())) {
            throw new IllegalStateException("This room is not available");
        }

        LocalDateTime start = request.getCheckIn().atTime(14,0).withHour(14).withMinute(0).withSecond(0);
        LocalDateTime end = request.getCheckOut().atTime(12,0);

        if (end.isBefore(start)) {
            throw new IllegalStateException("Checkout is before checkin!");
        }

        room.setStatus(RoomStatusEnum.BOOKED);
        roomRepository.save(room);

        BookingModel booking = new BookingModel();
        booking.setRoom(room);
        booking.setStartDate(start);
        booking.setEndDate(end);
        booking.setVisitor(request.getVisitor());
        booking.setStatus(BookingStatusEnum.ACTIVE);
        bookingRepository.save(booking);

        return ResponseEntity.ok("Room is booked successfully");
    }
}
