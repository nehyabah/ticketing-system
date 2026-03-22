package com.pm.ticketing.dto;

public record SeatResponse(
        Long id,
        String seatNumber,
        double price,
        String status
) {
}
