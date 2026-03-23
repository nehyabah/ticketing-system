package com.pm.ticketing.dto;

public record UserResponse(
        Long id,
        String name,
        String email,
        double walletBalance
) {
}
