package com.example.office.booking.repository;

// В RealEstateObjectRepository.java

import com.example.office.booking.entity.RealEstateObject;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RealEstateObjectRepository extends JpaRepository<RealEstateObject, Long> {

    @Query("SELECT r FROM RealEstateObject r WHERE " +
            "(:name IS NULL OR r.name LIKE %:name%) AND " +
            "(:description IS NULL OR r.description LIKE %:description%) AND " +
            "(:areaMin IS NULL OR r.area >= :areaMin) AND " +
            "(:areaMax IS NULL OR r.area <= :areaMax) AND " +
            "(:capacityMin IS NULL OR r.capacity >= :capacityMin) AND " +
            "(:capacityMax IS NULL OR r.capacity <= :capacityMax) AND " +
            "(:internetSpeedMin IS NULL OR r.internetSpeed >= :internetSpeedMin) AND " +
            "(:internetSpeedMax IS NULL OR r.internetSpeed <= :internetSpeedMax) AND " +
            "(:floorMin IS NULL OR r.floor >= :floorMin) AND " +
            "(:floorMax IS NULL OR r.floor <= :floorMax)")
    List<RealEstateObject> findByCriteria(
            @Param("name") String name,
            @Param("description") String description,
            @Param("areaMin") Integer areaMin,
            @Param("areaMax") Integer areaMax,
            @Param("capacityMin") Integer capacityMin,
            @Param("capacityMax") Integer capacityMax,
            @Param("internetSpeedMin") Integer internetSpeedMin,
            @Param("internetSpeedMax") Integer internetSpeedMax,
            @Param("floorMin") Integer floorMin,
            @Param("floorMax") Integer floorMax,
            Sort sort);

    List<RealEstateObject> findByIdIn(List<Long> objectIds);
}

