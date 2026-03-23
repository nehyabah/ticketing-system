package com.pm.ticketing.repository;

import com.pm.ticketing.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    Optional<Booking> findByIdempotencyKey(String idempotencyKey);

    List<Booking> findByUserId(Long userId);
}
