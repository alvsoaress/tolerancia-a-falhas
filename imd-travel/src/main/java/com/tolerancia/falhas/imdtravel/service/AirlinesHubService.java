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
    
    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class SellRequest {
        private String flight;
        private String day;
    }
}
