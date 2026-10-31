package com.example.appointmentservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;

@Configuration
public class DoctorServiceClientConfig {

    @Bean
    RestTemplate doctorServiceClient(
            @Value("${doctor-service.base-url}") String doctorServiceBaseUrl) {
        return new RestTemplateBuilder()
                .rootUri(doctorServiceBaseUrl)
                .build();
    }
}
