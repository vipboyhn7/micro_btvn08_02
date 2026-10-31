package com.example.appointmentservice.service;

import com.example.appointmentservice.dto.AppointmentRequest;
import com.example.appointmentservice.dto.AppointmentResponse;
import com.example.appointmentservice.dto.AddAppointmentRequest;
import com.example.appointmentservice.dto.AppointmentCreatedResponse;
import com.example.appointmentservice.dto.DoctorAvailabilityResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

    private final DoctorService doctorService;

    public AppointmentService(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    public ResponseEntity<Object> checkAvailability(AppointmentRequest request) {
        Object doctorResponse = doctorService.getDoctor(
                request.doctorId(), request.date(), request.time());
        if (!(doctorResponse instanceof DoctorAvailabilityResponse availability)) {
            return ResponseEntity.status(503).body(doctorResponse);
        }
        return ResponseEntity.ok(new AppointmentResponse(
                request.doctorId(),
                request.date(),
                request.time(),
                availability.available(),
                false,
                availability.message()));
    }

    public ResponseEntity<Object> addAppointment(AddAppointmentRequest request) {
        Object doctorResponse = doctorService.getDoctor(request.doctorId(), "", "");
        if (!(doctorResponse instanceof DoctorAvailabilityResponse)) {
            return ResponseEntity.status(503).body(doctorResponse);
        }
        return ResponseEntity.ok(new AppointmentCreatedResponse(
                request.patientName(),
                request.doctorId(),
                request.reason(),
                "Appointment created successfully"));
    }

}
