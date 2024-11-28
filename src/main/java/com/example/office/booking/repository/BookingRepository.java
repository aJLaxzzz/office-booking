package com.example.office.booking.repository;

import com.example.office.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findRequestsByObjectId(Long objectId);

    // Метод для проверки, занят ли объект в указанный период
    List<Booking> findByObjectIdAndStartDateBeforeAndEndDateAfter(Long objectId, LocalDateTime endDate, LocalDateTime startDate);
}
