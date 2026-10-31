package com.example.appointmentservice.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CircuitBreakerLoggingConfig {

    private static final Logger log = LoggerFactory.getLogger(CircuitBreakerLoggingConfig.class);

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public CircuitBreakerLoggingConfig(CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    @PostConstruct
    void registerStateLogger() {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("doctorServiceCB");
        circuitBreaker.getEventPublisher()
                .onStateTransition(event -> log.warn(
                        "CircuitBreaker '{}' changed state from {} to {}",
                        event.getCircuitBreakerName(),
                        event.getStateTransition().getFromState(),
                        event.getStateTransition().getToState()))
                .onError(event -> log.debug(
                        "CircuitBreaker '{}' recorded an error: {}",
                        event.getCircuitBreakerName(),
                        event.getThrowable().toString()))
                .onCallNotPermitted(event -> log.warn(
                        "CircuitBreaker '{}' recorded a call which was not permitted",
                        event.getCircuitBreakerName()));
    }
}
