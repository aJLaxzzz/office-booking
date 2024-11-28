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
            "(:address IS NULL OR r.address LIKE %:address%) AND " +
            "(:description IS NULL OR r.description LIKE %:description%) AND " +
            "(:areaMin IS NULL OR r.area >= :areaMin) AND " +
            "(:areaMax IS NULL OR r.area <= :areaMax) AND " +
            "(:priceMin IS NULL OR r.price >= :priceMin) AND " +
            "(:priceMax IS NULL OR r.price <= :priceMax) AND " +
            "(:buildYearMin IS NULL OR r.buildYear >= :buildYearMin) AND " +
            "(:buildYearMax IS NULL OR r.buildYear <= :buildYearMax) AND " +
            "(:floorMin IS NULL OR r.floor >= :floorMin) AND " +
            "(:floorMax IS NULL OR r.floor <= :floorMax)")
    List<RealEstateObject> findByCriteria(
            @Param("address") String address,
            @Param("description") String description,
            @Param("areaMin") Integer areaMin,
            @Param("areaMax") Integer areaMax,
            @Param("priceMin") Integer priceMin,
            @Param("priceMax") Integer priceMax,
            @Param("buildYearMin") Integer buildYearMin,
            @Param("buildYearMax") Integer buildYearMax,
            @Param("floorMin") Integer floorMin,
            @Param("floorMax") Integer floorMax,
            Sort sort);

    List<RealEstateObject> findByIdIn(List<Long> objectIds);
}

