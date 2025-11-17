package com.tolerancia.falhas.imdtravel.service;

import com.tolerancia.falhas.imdtravel.dto.FlightInfo;
import com.tolerancia.falhas.imdtravel.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AirlinesHubService {
    
    private final RestTemplate restTemplate;
    private final String baseUrl;
    
    public AirlinesHubService(@Value("${airlines.hub.url:http://localhost:8081}") String baseUrl) {
        this.restTemplate = new RestTemplate();
        this.baseUrl = baseUrl;
    }
    
    public FlightInfo getFlightInfo(String flight, String day) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/api/flight")
                .queryParam("flight", flight)
                .queryParam("day", day)
                .toUriString();
        
        return restTemplate.getForObject(url, FlightInfo.class);
    }
    
    public TransactionResponse sellTicket(String flight, String day) {
        String url = baseUrl + "/api/sell";
        return restTemplate.postForObject(url, new SellRequest(flight, day), TransactionResponse.class);
    }

    public FlightInfo getFlightInfoWithRetry(String flight, String day, boolean ftEnabled) {
        if (!ftEnabled) return getFlightInfo(flight, day);
        RuntimeException last = null;
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                return getFlightInfo(flight, day);
            } catch (RuntimeException ex) {
                last = ex;
                try { Thread.sleep(200L * attempt); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            }
        }
        // Fallback: synthesize reasonable value to proceed
        return new FlightInfo(flight, day, 600.0);
    }

    public TransactionResponse sellTicketWithTimeout(String flight, String day, boolean ftEnabled) {
        if (!ftEnabled) return sellTicket(flight, day);
        // Create short-timeout RestTemplate to enforce 2s SLA
        org.springframework.http.client.SimpleClientHttpRequestFactory rf = new org.springframework.http.client.SimpleClientHttpRequestFactory();
        rf.setConnectTimeout(1000);
        rf.setReadTimeout(2000);
        RestTemplate rt = new RestTemplate(rf);
        String url = baseUrl + "/api/sell";
        return rt.postForObject(url, new SellRequest(flight, day), TransactionResponse.class);
    }
    
    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class SellRequest {
        private String flight;
        private String day;
    }
}
