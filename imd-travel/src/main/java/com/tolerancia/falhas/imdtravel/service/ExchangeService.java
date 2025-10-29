package com.tolerancia.falhas.imdtravel.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExchangeService {
    
    private final RestTemplate restTemplate;
    private final String baseUrl;
    
    public ExchangeService(@Value("${exchange.url:http://localhost:8082}") String baseUrl) {
        this.restTemplate = new RestTemplate();
        this.baseUrl = baseUrl;
    }
    
    public Double getExchangeRate() {
        String url = baseUrl + "/api/convert";
        return restTemplate.getForObject(url, Double.class);
    }
}
