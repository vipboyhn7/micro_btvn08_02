package com.example.appointmentservice.dto;

public record AppointmentResponse(
        String doctorId,
        String date,
        String time,
        boolean available,
        boolean fallback,
        String message) {
}
