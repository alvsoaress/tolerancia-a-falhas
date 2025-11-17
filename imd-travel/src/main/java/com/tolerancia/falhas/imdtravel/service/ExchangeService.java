package com.tolerancia.falhas.imdtravel.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExchangeService {
    
    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final java.util.ArrayDeque<Double> lastRates = new java.util.ArrayDeque<>(10);
    
    public ExchangeService(@Value("${exchange.url:http://localhost:8082}") String baseUrl) {
        this.restTemplate = new RestTemplate();
        this.baseUrl = baseUrl;
    }
    
    public Double getExchangeRate() {
        String url = baseUrl + "/api/convert";
        return restTemplate.getForObject(url, Double.class);
    }

    public Double getExchangeRateWithFallback(boolean ftEnabled) {
        if (!ftEnabled) {
            Double r = getExchangeRate();
            remember(r);
            return r;
        }
        try {
            Double r = getExchangeRate();
            remember(r);
            return r;
        } catch (RuntimeException ex) {
            Double avg = average();
            if (avg != null) return avg;
            throw ex;
        }
    }

    private void remember(Double rate) {
        if (rate == null) return;
        if (lastRates.size() == 10) lastRates.removeFirst();
        lastRates.addLast(rate);
    }

    private Double average() {
        if (lastRates.isEmpty()) return null;
        double sum = 0.0;
        for (Double d : lastRates) sum += d;
        return sum / lastRates.size();
    }
}
