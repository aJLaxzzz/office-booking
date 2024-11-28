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

    @GetMapping("object/{objectId}")
    public ResponseEntity<List<Booking>> getBookingsByObjectId(@PathVariable Long objectId) {
        List<Booking> bookings = bookingRepository.findByObjectId(objectId);
        System.out.println("Загружено бронирований: " + bookings.size());
        return ResponseEntity.ok(bookings);
    }


    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        // 1. Проверка, что дата начала меньше даты окончания
        if (booking.getStartDate().isAfter(booking.getEndDate())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);  // Неверный формат дат
        }

        // 2. Проверка на наличие бронирования для этого объекта недвижимости на этот период
        List<Booking> conflictingBookings = bookingRepository.findByObjectIdAndStartDateBeforeAndEndDateAfter(
                booking.getObjectId(), booking.getEndDate(), booking.getStartDate());

        // Дополнительная логика для учета пересечений, включая даты окончания (включительно)
        for (Booking existingBooking : conflictingBookings) {
            // Пересечение если:
            // - новое бронирование начинается до конца уже существующего и заканчивается после его начала
            // - новое бронирование начинается с даты окончания другого бронирования (когда даты совпадают)
            // То есть, дата начала нового бронирования не может быть той же, что и дата окончания существующего бронирования
            if (!(booking.getEndDate().isBefore(existingBooking.getStartDate()) ||
                    booking.getStartDate().isAfter(existingBooking.getEndDate()) ||
                    booking.getStartDate().isEqual(existingBooking.getEndDate()) || // не может начинаться в день окончания другого
                    booking.getEndDate().isEqual(existingBooking.getStartDate()))) {  // не может заканчиваться в день начала другого
                return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Конфликт бронирования
            }
        }

        // Если все проверки пройдены, сохраняем бронирование
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
