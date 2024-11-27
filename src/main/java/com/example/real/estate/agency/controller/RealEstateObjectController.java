package com.example.real.estate.agency.controller;

import com.example.real.estate.agency.entity.RealEstateObject;
import com.example.real.estate.agency.repository.RealEstateObjectRepository;
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
            realEstateObjectRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public List<RealEstateObject> searchRealEstateObjects(@RequestParam MultiValueMap<String, String> params) {
        String address = params.getFirst("address");
        String description = params.getFirst("description");
        Integer areaMin = params.containsKey("areaMin") ? Integer.parseInt(params.getFirst("areaMin")) : null;
        Integer areaMax = params.containsKey("areaMax") ? Integer.parseInt(params.getFirst("areaMax")) : null;
        Integer priceMin = params.containsKey("priceMin") ? Integer.parseInt(params.getFirst("priceMin")) : null;
        Integer priceMax = params.containsKey("priceMax") ? Integer.parseInt(params.getFirst("priceMax")) : null;
        Integer buildYearMin = params.containsKey("buildYearMin") ? Integer.parseInt(params.getFirst("buildYearMin")) : null;
        Integer buildYearMax = params.containsKey("buildYearMax") ? Integer.parseInt(params.getFirst("buildYearMax")) : null;
        Integer minLivingRooms = params.containsKey("minLivingRooms") ? Integer.parseInt(params.getFirst("minLivingRooms")) : null;
        Integer minBathRooms = params.containsKey("minBathRooms") ? Integer.parseInt(params.getFirst("minBathRooms")) : null;

        // Handle sorting
        String sortBy = params.getFirst("sortBy");
        String sortDirection = params.getFirst("sortDirection");
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);

        List<RealEstateObject> result = realEstateObjectRepository.findByCriteria(
                address, description, areaMin, areaMax, priceMin, priceMax,
                buildYearMin, buildYearMax, minLivingRooms, minBathRooms, sort
        );

        return result;
    }

}
