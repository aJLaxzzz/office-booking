package com.example.office.booking.controller;

import com.example.office.booking.entity.Booking;
import com.example.office.booking.entity.RealEstateObject;
import com.example.office.booking.repository.BookingRepository;
import com.example.office.booking.repository.RealEstateObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/realestate")
public class RealEstateObjectController {
    @Autowired
    private RealEstateObjectRepository realEstateObjectRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping
    public List<RealEstateObject> getAllRealEstateObjects() {
        return realEstateObjectRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RealEstateObject> getRealEstateObjectById(@PathVariable Long id) {
        Optional<RealEstateObject> realEstateObject = realEstateObjectRepository.findById(id);
        return realEstateObject.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RealEstateObject> createRealEstateObject(@RequestBody RealEstateObject realEstateObject) {
        RealEstateObject savedObject = realEstateObjectRepository.save(realEstateObject);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedObject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RealEstateObject> updateRealEstateObject(@PathVariable Long id, @RequestBody RealEstateObject updatedObject) {
        Optional<RealEstateObject> existingObjectOptional = realEstateObjectRepository.findById(id);
        if (existingObjectOptional.isPresent()) {
            updatedObject.setId(id);
            RealEstateObject savedObject = realEstateObjectRepository.save(updatedObject);
            return ResponseEntity.ok(savedObject);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRealEstateObject(@PathVariable Long id) {
        Optional<RealEstateObject> realEstateObjectOptional = realEstateObjectRepository.findById(id);
        if (realEstateObjectOptional.isPresent()) {
            // Получаем все бронирования, связанные с объектом недвижимости
            List<Booking> bookings = bookingRepository.findByObjectId(id);

            // Удаляем все связанные бронирования
            if (!bookings.isEmpty()) {
                bookingRepository.deleteAll(bookings);
            }

            // Удаляем сам объект недвижимости
            realEstateObjectRepository.deleteById(id);

            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public List<RealEstateObject> searchRealEstateObjects(@RequestParam MultiValueMap<String, String> params) {
        String name = params.getFirst("name");
        String description = params.getFirst("description");
        Integer areaMin = params.containsKey("areaMin") ? Integer.parseInt(params.getFirst("areaMin")) : null;
        Integer areaMax = params.containsKey("areaMax") ? Integer.parseInt(params.getFirst("areaMax")) : null;
        Integer capacityMin = params.containsKey("capacityMin") ? Integer.parseInt(params.getFirst("capacityMin")) : null;
        Integer capacityMax = params.containsKey("capacityMax") ? Integer.parseInt(params.getFirst("capacityMax")) : null;
        Integer internetSpeedMin = params.containsKey("internetSpeedMin") ? Integer.parseInt(params.getFirst("internetSpeedMin")) : null;
        Integer internetSpeedMax = params.containsKey("internetSpeedMax") ? Integer.parseInt(params.getFirst("internetSpeedMax")) : null;
        Integer floorMin = params.containsKey("floorMin") ? Integer.parseInt(params.getFirst("floorMin")) : null;
        Integer floorMax = params.containsKey("floorMax") ? Integer.parseInt(params.getFirst("floorMax")) : null;

        // Handle sorting
        String sortBy = params.getFirst("sortBy");
        String sortDirection = params.getFirst("sortDirection");
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);

        List<RealEstateObject> result = realEstateObjectRepository.findByCriteria(
                name, description, areaMin, areaMax, capacityMin, capacityMax,
                internetSpeedMin, internetSpeedMax, floorMin, floorMax, sort
        );

        return result;
    }

}
