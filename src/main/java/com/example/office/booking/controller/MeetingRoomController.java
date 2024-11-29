package com.example.office.booking.controller;

import com.example.office.booking.entity.Booking;
import com.example.office.booking.entity.MeetingRoom;
import com.example.office.booking.repository.BookingRepository;
import com.example.office.booking.repository.MeetingRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/meetingrooms")
public class MeetingRoomController {
    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping
    public List<MeetingRoom> getAllMeetingRooms() {
        return meetingRoomRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeetingRoom> getMeetingRoomById(@PathVariable Long id) {
        Optional<MeetingRoom> MeetingRoom = meetingRoomRepository.findById(id);
        return MeetingRoom.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MeetingRoom> createMeetingRoom(@RequestBody MeetingRoom MeetingRoom) {
        MeetingRoom savedObject = meetingRoomRepository.save(MeetingRoom);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedObject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MeetingRoom> updateMeetingRoom(@PathVariable Long id, @RequestBody MeetingRoom updatedObject) {
        Optional<MeetingRoom> existingObjectOptional = meetingRoomRepository.findById(id);
        if (existingObjectOptional.isPresent()) {
            updatedObject.setId(id);
            MeetingRoom savedObject = meetingRoomRepository.save(updatedObject);
            return ResponseEntity.ok(savedObject);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeetingRoom(@PathVariable Long id) {
        Optional<MeetingRoom> MeetingRoomOptional = meetingRoomRepository.findById(id);
        if (MeetingRoomOptional.isPresent()) {
            List<Booking> bookings = bookingRepository.findByObjectId(id);
            if (!bookings.isEmpty()) {
                bookingRepository.deleteAll(bookings);
            }
            meetingRoomRepository.deleteById(id);

            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public List<MeetingRoom> searchMeetingRooms(@RequestParam MultiValueMap<String, String> params) {
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

        String sortBy = params.getFirst("sortBy");
        String sortDirection = params.getFirst("sortDirection");
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);

        List<MeetingRoom> result = meetingRoomRepository.findByCriteria(
                name, description, areaMin, areaMax, capacityMin, capacityMax,
                internetSpeedMin, internetSpeedMax, floorMin, floorMax, sort
        );

        return result;
    }

}
