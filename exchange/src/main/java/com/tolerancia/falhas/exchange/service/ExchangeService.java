package com.tolerancia.falhas.exchange.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ExchangeService {

    private static final Logger log = LoggerFactory.getLogger(ExchangeService.class);
    private static final double ERROR_PROBABILITY = 0.1;
    private static final long ERROR_DURATION_MILLIS = Duration.ofSeconds(5).toMillis();

    private final AtomicLong errorFailureActiveUntil = new AtomicLong(0);

    public Double getExchangeRate() {
        handleErrorFailureIfNeeded();

        double minRate = 5.0;
        double maxRate = 6.0;
        return minRate + Math.random() * (maxRate - minRate);
    }

    private void handleErrorFailureIfNeeded() {
        long now = System.currentTimeMillis();
        long activeUntil = errorFailureActiveUntil.get();

        if (activeUntil > now) {
            log.warn("Simulated error failure active for Exchange /convert request ({} ms remaining).",
                    activeUntil - now);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Simulated error failure on Exchange /convert");
        }

        if (shouldTrigger(ERROR_PROBABILITY)) {
            long newUntil = now + ERROR_DURATION_MILLIS;
            errorFailureActiveUntil.set(newUntil);
            log.warn("Simulated error failure triggered for Exchange /convert request for the next {} ms.",
                    ERROR_DURATION_MILLIS);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Simulated error failure on Exchange /convert");
        }
    }

    private boolean shouldTrigger(double probability) {
        return ThreadLocalRandom.current().nextDouble() < probability;
    }
}
