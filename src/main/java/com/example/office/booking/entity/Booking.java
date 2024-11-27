package com.example.office.booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", nullable=false)
    private Long userId;

    @Column(name="object_id", nullable=false)
    private Long objectId;

    @Column(name="startTime", nullable=false)
    private LocalDateTime startTime;

    @Column(name="endTime", nullable=false)
    private LocalDateTime endTime;
}




