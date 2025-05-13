package com.example.hotelreservationservice.rooms.mappers;

import com.example.hotelreservationservice.db.model.RoomModel;
import com.example.hotelreservationservice.rooms.dto.RoomDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    RoomDTO toRoomDTO(RoomModel roomModel);
}
