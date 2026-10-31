package com.example.appointmentservice.dto;

import javax.validation.constraints.NotBlank;

public record AppointmentRequest(
        @NotBlank String doctorId,
        @NotBlank String date,
        @NotBlank String time) {
}
