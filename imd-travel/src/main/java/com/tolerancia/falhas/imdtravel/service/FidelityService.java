package com.tolerancia.falhas.imdtravel.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FidelityService {
    
    private final RestTemplate restTemplate;
    private final String baseUrl;
    
    public FidelityService(@Value("${fidelity.url:http://localhost:8083}") String baseUrl) {
        this.restTemplate = new RestTemplate();
        this.baseUrl = baseUrl;
    }
    
    public void addBonus(Long userId, Integer bonus) {
        String url = baseUrl + "/api/bonus";
        restTemplate.postForObject(url, new BonusRequest(userId, bonus), Void.class);
    }

    public void addBonusNonBlocking(Long userId, Integer bonus, boolean ftEnabled) {
        if (!ftEnabled) { addBonus(userId, bonus); return; }
        new Thread(() -> {
            try {
                addBonus(userId, bonus);
            } catch (Exception ignored) {
            }
        }, "bonus-dispatcher").start();
    }
    
    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class BonusRequest {
        private Long user;
        private Integer bonus;
    }
}
