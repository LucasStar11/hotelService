package com.example.hotelreservationservice.rooms.service;

import com.example.hotelreservationservice.db.model.BookingModel;
import com.example.hotelreservationservice.db.model.RoomModel;
import com.example.hotelreservationservice.db.model.enums.BookingStatusEnum;
import com.example.hotelreservationservice.db.model.enums.RoomStatusEnum;
import com.example.hotelreservationservice.db.repository.BookingRepository;
import com.example.hotelreservationservice.db.repository.RoomRepository;
import com.example.hotelreservationservice.rooms.dto.RoomDTO;
import com.example.hotelreservationservice.rooms.mappers.RoomMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final RoomMapper roomMapper;

    // пагинацию было лень делать, я знаю что простой findAll() плохая практика
    public List<RoomDTO> getAvailableRooms() {
        List<RoomModel> rooms = roomRepository.findByStatus(RoomStatusEnum.AVAILABLE);
        return rooms.stream()
                .map(roomMapper::toRoomDTO)
                .collect(Collectors.toList());
    }

    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(roomMapper::toRoomDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ResponseEntity<?> occupyRoom(Long roomId) {
        RoomModel room = roomRepository.findById(roomId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Room not found")
                );

        if (!RoomStatusEnum.BOOKED.equals(room.getStatus())) {
            throw new IllegalStateException("Room is already not booked!");
        }

        BookingModel booking = bookingRepository.findByRoom_IdAndStatus(roomId, BookingStatusEnum.ACTIVE)
                .orElseThrow(
                        () -> new IllegalArgumentException("Booking for this room not found")
                );

        if (booking.getStartDate().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("Room cant be occupied now");
        }
        booking.setStatus(BookingStatusEnum.ACTIVE);
        bookingRepository.save(booking);

        room.setStatus(RoomStatusEnum.OCCUPIED);
        roomRepository.save(room);
        return ResponseEntity.ok("Room is occupied successfully");
    }

    public ResponseEntity<?> freeRoom(Long roomId) {
        RoomModel room = roomRepository.findById(roomId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Room not found")
                );
        BookingModel booking = bookingRepository.findByRoom_IdAndStatus(roomId, BookingStatusEnum.ACTIVE)
                .orElseThrow(
                        () -> new IllegalArgumentException("Booking for this room not found")
                );

        booking.setStatus(BookingStatusEnum.FINISHED);
        room.setStatus(RoomStatusEnum.AVAILABLE);
        roomRepository.save(room);
        bookingRepository.delete(booking);

        return ResponseEntity.ok("Room is available now");
    }

    @Transactional
    @Scheduled(cron = "0 0 12 * * ?")
    public void autoFreeRooms() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingModel> expiredBookings = bookingRepository.findByEndDateBeforeAndStatus(now,BookingStatusEnum.ACTIVE);
        for (BookingModel booking : expiredBookings) {
            RoomModel room = booking.getRoom();
            booking.setStatus(BookingStatusEnum.FINISHED);
            room.setStatus(RoomStatusEnum.AVAILABLE);
            roomRepository.save(room);
            bookingRepository.delete(booking);
            log.info("Booking {} and room {} now available ", booking, room);
        }
    }

}
