package com.example.appointmentservice.dto;

public record AppointmentCreatedResponse(
        String patientName,
        String doctorId,
        String reason,
        String message) {
}
