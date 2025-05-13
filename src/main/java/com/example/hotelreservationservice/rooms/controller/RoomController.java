package com.example.hotelreservationservice.rooms.controller;

import com.example.hotelreservationservice.rooms.dto.RoomDTO;
import com.example.hotelreservationservice.rooms.service.RoomService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@Validated
public class RoomController {
    private final RoomService roomService;

    @GetMapping("/available")
    public List<RoomDTO> getAvailableRooms() {
        return roomService.getAvailableRooms();
    }

    @GetMapping
    public List<RoomDTO> getAllRooms() {
        return roomService.getAllRooms();
    }

    @PostMapping("/occupy")
    public ResponseEntity<?> occupyRoom(@Valid @RequestParam @Positive @NotNull Long roomId) {
        return roomService.occupyRoom(roomId);
    }

    @PostMapping("/free")
    public ResponseEntity<?> freeRoom(@Valid @RequestParam @Positive @NotNull Long roomId) {
        return roomService.freeRoom(roomId);
    }
}
