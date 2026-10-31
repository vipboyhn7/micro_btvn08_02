package com.example.appointmentservice.dto;

import java.time.Instant;

public record ApiResponseError(
        String message,
        int status,
        String error,
        Instant timestamp) {
}
