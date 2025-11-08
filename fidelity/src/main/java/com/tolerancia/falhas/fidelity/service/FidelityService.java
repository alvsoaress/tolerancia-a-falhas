package com.tolerancia.falhas.fidelity.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class FidelityService {

    private static final Logger log = LoggerFactory.getLogger(FidelityService.class);
    private static final double CRASH_PROBABILITY = 0.02;

    private final Map<Long, Integer> userPoints = new HashMap<>();

    public void addBonus(Long userId, Integer bonus) {
        simulateCrashIfNeeded();

        userPoints.merge(userId, bonus, Integer::sum);
        log.info("Added {} points to user {}. Total points: {}", bonus, userId, userPoints.get(userId));
    }

    public Integer getUserPoints(Long userId) {
        return userPoints.getOrDefault(userId, 0);
    }

    private void simulateCrashIfNeeded() {
        if (ThreadLocalRandom.current().nextDouble() < CRASH_PROBABILITY) {
            log.error("Simulated crash failure triggered for Fidelity /bonus request. Terminating service.");
            System.exit(1);
        }
    }
}
