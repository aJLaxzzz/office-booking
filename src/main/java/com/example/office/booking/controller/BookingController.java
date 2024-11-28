package com.example.office.booking.controller;

import com.example.office.booking.entity.Booking;
import com.example.office.booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/booking")
public class BookingController {
    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        return booking.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        // Проверка, что дата начала меньше даты окончания
        if (booking.getStartDate().isAfter(booking.getEndDate())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);  // Неверный формат дат
        }

        // Проверка на занятость объекта в указанный период
        List<Booking> conflictingBookings = bookingRepository.findByObjectIdAndStartDateBeforeAndEndDateAfter(
                booking.getObjectId(), booking.getEndDate(), booking.getStartDate());

        if (!conflictingBookings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Конфликт бронирования
        }

        Booking savedBooking = bookingRepository.save(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBooking);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking updatedBooking) {
        Optional<Booking> existingBookingOptional = bookingRepository.findById(id);
        if (existingBookingOptional.isPresent()) {
            updatedBooking.setId(id);

            // Проверка, что дата начала меньше даты окончания
            if (updatedBooking.getStartDate().isAfter(updatedBooking.getEndDate())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(null);  // Неверный формат дат
            }

            // Проверка на занятость объекта в указанный период
            List<Booking> conflictingBookings = bookingRepository.findByObjectIdAndStartDateBeforeAndEndDateAfter(
                    updatedBooking.getObjectId(), updatedBooking.getEndDate(), updatedBooking.getStartDate());

            if (!conflictingBookings.isEmpty()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Конфликт бронирования
            }

            Booking savedBooking = bookingRepository.save(updatedBooking);
            return ResponseEntity.ok(savedBooking);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isPresent()) {
            bookingRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
