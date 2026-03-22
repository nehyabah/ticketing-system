package com.pm.ticketing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookingRequest(
        @NotNull(message = "User ID is required")
        Long userId,

        @NotNull(message = "Seat ID is required")
        Long seatId,

        @NotBlank(message = "Idempotency Key is required")
        String idempotencyKey
) {
}
