package com.pm.ticketing.dto;

import java.time.LocalDateTime;

public record EventResponse(
        Long id,
        String name,
        String venue,
        LocalDateTime dateTime,
        int totalSeats,
        int availableSeats
) {
}
