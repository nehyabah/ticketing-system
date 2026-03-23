package com.pm.ticketing.dto;

import java.time.LocalDateTime;

public record BookingResponse(Long id,
                              String eventName,
                              String seatNumber,
                              double price,
                              String userName,
                              LocalDateTime bookingTime,
                              String status) {
}
