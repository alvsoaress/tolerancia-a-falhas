package com.tolerancia.falhas.airlineshub.service;

import com.tolerancia.falhas.airlineshub.dto.FlightInfo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class FlightService {
    
    private final Map<String, FlightInfo> flights = new HashMap<>();
    
    public FlightService() {
        flights.put("AA123-2024-01-15", new FlightInfo("AA123", "2024-01-15", 500.0));
        flights.put("BB456-2024-01-16", new FlightInfo("BB456", "2024-01-16", 750.0));
        flights.put("CC789-2024-01-17", new FlightInfo("CC789", "2024-01-17", 300.0));
        flights.put("DD321-2024-01-18", new FlightInfo("DD321", "2024-01-18", 900.0));
    }
    
    public FlightInfo getFlightInfo(String flight, String day) {
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
        String transactionId = UUID.randomUUID().toString();
        return transactionId;
    }
}
