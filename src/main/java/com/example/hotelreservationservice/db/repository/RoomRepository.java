package com.example.hotelreservationservice.db.repository;

import com.example.hotelreservationservice.db.model.RoomModel;
import com.example.hotelreservationservice.db.model.enums.RoomStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<RoomModel, Long> {
    List<RoomModel> findByStatus(RoomStatusEnum roomStatus);
    Optional<RoomModel> findByNumber(String number);
}
