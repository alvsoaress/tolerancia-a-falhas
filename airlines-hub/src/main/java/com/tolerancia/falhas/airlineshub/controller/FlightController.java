package com.tolerancia.falhas.airlineshub.controller;

import com.tolerancia.falhas.airlineshub.dto.FlightInfo;
import com.tolerancia.falhas.airlineshub.dto.SellRequest;
import com.tolerancia.falhas.airlineshub.dto.TransactionResponse;
import com.tolerancia.falhas.airlineshub.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class FlightController {
    
    @Autowired
    private FlightService flightService;
    
    @GetMapping("/flight")
    public ResponseEntity<FlightInfo> getFlightInfo(
            @RequestParam("flight") String flight,
            @RequestParam("day") String day) {
        FlightInfo flightInfo = flightService.getFlightInfo(flight, day);
        return ResponseEntity.ok(flightInfo);
    }
    
    @PostMapping("/sell")
    public ResponseEntity<TransactionResponse> sellTicket(@Valid @RequestBody SellRequest request) {
        String transactionId = flightService.sellTicket(request.getFlight(), request.getDay());
        return ResponseEntity.ok(new TransactionResponse(transactionId));
    }
}
