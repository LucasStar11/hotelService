package com.example.hotelreservationservice.db.model;


import com.example.hotelreservationservice.db.model.enums.RoomStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name ="room_model")
public class RoomModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "number", unique = true, nullable = false)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RoomStatusEnum status;
}
