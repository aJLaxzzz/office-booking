package com.example.office.booking.repository;

import com.example.office.booking.entity.Booking;
import com.example.office.booking.entity.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByObjectId(Long objectId);
    List<Booking> findByObjectIdAndStartDateBeforeAndEndDateAfter(
            Long objectId,
            LocalDateTime endDate,
            LocalDateTime startDate);
    List<Booking> findByUserId(Long userId);
    List<Booking> findByUserIdAndStartDateBeforeAndEndDateAfter(
            Long userId,
            LocalDateTime endDate,
            LocalDateTime startDate);

}


