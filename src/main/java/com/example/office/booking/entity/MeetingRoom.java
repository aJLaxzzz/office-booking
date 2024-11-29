package com.example.office.booking.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "meeting_rooms")
public class MeetingRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 512)
    private String name;//

    @Column(name = "area_meters", nullable = false)
    private int area;//

    @Column(nullable = false)
    private int internetSpeed;

    @Column(nullable = false)
    private int capacity;//

    @Column(name = "floor", nullable = false)
    private int floor;//

    @Column(name = "photo", nullable = false)
    private String photoURL;

    @Column(nullable = true, length = 1024)
    private String description;//
}
