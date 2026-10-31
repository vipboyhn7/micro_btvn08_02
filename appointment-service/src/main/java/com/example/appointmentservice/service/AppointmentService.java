package com.example.appointmentservice.service;

import com.example.appointmentservice.dto.AppointmentRequest;
import com.example.appointmentservice.dto.AppointmentResponse;
import com.example.appointmentservice.dto.DoctorAvailabilityResponse;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

@Service
public class AppointmentService {

    private final RestTemplate doctorServiceClient;
    private final CircuitBreaker doctorServiceCircuitBreaker;

    public AppointmentService(
            RestTemplate doctorServiceClient,
            CircuitBreakerRegistry circuitBreakerRegistry) {
        this.doctorServiceClient = doctorServiceClient;
        this.doctorServiceCircuitBreaker = circuitBreakerRegistry.circuitBreaker("doctorServiceCB");
    }

    public ResponseEntity<AppointmentResponse> checkAvailability(AppointmentRequest request) {
        try {
            DoctorAvailabilityResponse doctorResponse = doctorServiceCircuitBreaker.executeSupplier(
                    () -> requestDoctorAvailability(request));
            return ResponseEntity.ok(new AppointmentResponse(
                    request.doctorId(),
                    request.date(),
                    request.time(),
                    doctorResponse.available(),
                    false,
                    doctorResponse.message()));
        } catch (CallNotPermittedException | RestClientException | IllegalStateException exception) {
            return checkAvailabilityFallback(request);
        }
    }

    private DoctorAvailabilityResponse requestDoctorAvailability(AppointmentRequest request) {
        DoctorAvailabilityResponse doctorResponse = doctorServiceClient.getForObject(
                "/api/doctors/{doctorId}/availability?date={date}&time={time}",
                DoctorAvailabilityResponse.class,
                request.doctorId(),
                request.date(),
                request.time());

        if (doctorResponse == null) {
            throw new IllegalStateException("Doctor-Service returned an empty response");
        }
        return doctorResponse;
    }

    private ResponseEntity<AppointmentResponse> checkAvailabilityFallback(AppointmentRequest request) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new AppointmentResponse(
                        request.doctorId(),
                        request.date(),
                        request.time(),
                        false,
                        true,
                        "Doctor-Service is currently unavailable. Please try again later."));
    }
}
