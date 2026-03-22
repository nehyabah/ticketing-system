package com.pm.ticketing.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateEventRequest(
        @NotBlank(message = "Name is required")
        String name,
        @NotBlank(message = "Venue is required")
        String venue,
        @NotNull(message = "Date/Time is required")
        String dateTime,
        @Min(value = 1, message = "Must have at least 1 seat")
        int numberOfSeats,
        @Min(value = 1, message = "Price must be at least 1")
        double seatPrice

) {
}
