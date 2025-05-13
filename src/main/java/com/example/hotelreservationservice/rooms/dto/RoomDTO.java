package com.example.hotelreservationservice.rooms.dto;

import com.example.hotelreservationservice.db.model.enums.RoomStatusEnum;
import lombok.Data;

@Data
public class RoomDTO {
    private Long id;
    private String number;
    private RoomStatusEnum status;
}
