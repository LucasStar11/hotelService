package com.example.hotelreservationservice.db.repository;

import com.example.hotelreservationservice.db.model.BookingModel;
import com.example.hotelreservationservice.db.model.enums.BookingStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<BookingModel, Long> {
    List<BookingModel> findByEndDateBeforeAndStatus(LocalDateTime endDateBefore, BookingStatusEnum status);

    Optional<BookingModel> findByRoom_IdAndStatus(Long roomId, BookingStatusEnum status);
}

