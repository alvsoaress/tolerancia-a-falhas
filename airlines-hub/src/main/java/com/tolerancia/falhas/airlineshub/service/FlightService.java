package com.tolerancia.falhas.airlineshub.service;

import com.tolerancia.falhas.airlineshub.dto.FlightInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class FlightService {

    private static final Logger log = LoggerFactory.getLogger(FlightService.class);
    private static final double OMISSION_PROBABILITY = 0.2;
    private static final double TIME_FAILURE_PROBABILITY = 0.1;
    private static final long TIME_FAILURE_DELAY_MILLIS = Duration.ofSeconds(5).toMillis();
    private static final long TIME_FAILURE_DURATION_MILLIS = Duration.ofSeconds(10).toMillis();

    private final Map<String, FlightInfo> flights = new HashMap<>();
    private final AtomicLong timeFailureActiveUntil = new AtomicLong(0);

    public FlightService() {
        flights.put("AA123-2024-01-15", new FlightInfo("AA123", "2024-01-15", 500.0));
        flights.put("BB456-2024-01-16", new FlightInfo("BB456", "2024-01-16", 750.0));
        flights.put("CC789-2024-01-17", new FlightInfo("CC789", "2024-01-17", 300.0));
        flights.put("DD321-2024-01-18", new FlightInfo("DD321", "2024-01-18", 900.0));
    }

    public FlightInfo getFlightInfo(String flight, String day) {
        simulateOmissionFailureIfNeeded();

        String key = flight + "-" + day;
        FlightInfo flightInfo = flights.get(key);

        if (flightInfo == null) {
            double randomValue = 200 + Math.random() * 800;
            flightInfo = new FlightInfo(flight, day, randomValue);
            flights.put(key, flightInfo);
        }

        return flightInfo;
    }

    public String sellTicket(String flight, String day) {
        simulateTimeFailureIfNeeded();

        String transactionId = UUID.randomUUID().toString();
        return transactionId;
    }

    private void simulateOmissionFailureIfNeeded() {
        if (shouldTrigger(OMISSION_PROBABILITY)) {
            log.warn("Simulated omission failure triggered for AirlinesHub /flight request.");
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Simulated omission failure on AirlinesHub /flight");
        }
    }

    private void simulateTimeFailureIfNeeded() {
        long now = System.currentTimeMillis();
        long activeUntil = timeFailureActiveUntil.get();

        if (activeUntil > now) {
            delayResponse();
            return;
        }

        if (shouldTrigger(TIME_FAILURE_PROBABILITY)) {
            long newUntil = now + TIME_FAILURE_DURATION_MILLIS;
            timeFailureActiveUntil.set(newUntil);
            log.warn("Simulated time failure triggered for AirlinesHub /sell request for the next {} ms.",
                    TIME_FAILURE_DURATION_MILLIS);
            delayResponse();
        }
    }

    private void delayResponse() {
        try {
            Thread.sleep(TIME_FAILURE_DELAY_MILLIS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private boolean shouldTrigger(double probability) {
        return ThreadLocalRandom.current().nextDouble() < probability;
    }
}
