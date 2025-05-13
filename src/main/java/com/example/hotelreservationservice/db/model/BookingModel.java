package com.example.hotelreservationservice.db.model;

import com.example.hotelreservationservice.db.model.enums.BookingStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name ="booking_model")
public class BookingModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_model")
    private RoomModel room;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "visitor")
    private String visitor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatusEnum status;
}
