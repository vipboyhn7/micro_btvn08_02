package com.example.appointmentservice.dto;

import javax.validation.constraints.NotBlank;

public record AddAppointmentRequest(
        @NotBlank String patientName,
        @NotBlank String doctorId,
        @NotBlank String reason) {
}
