package com.example.office.booking.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "real_estate_objects")
public class RealEstateObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 512)
    private String address;

    @Column(name = "area_meters", nullable = false)
    private int area;

    @Column(nullable = false)
    private int price;

    @Column(nullable = true, length = 1024)
    private String description;

    @Column(name = "build_year", nullable = false)
    private int buildYear;

    @Column(name = "number_of_livingrooms", nullable = false)
    private int numberOfLivingRooms;

    @Column(name = "number_of_bathrooms", nullable = false)
    private int numberOfBathRooms;

    @Column(name = "photo", nullable = false)
    private String photoURL;
}
