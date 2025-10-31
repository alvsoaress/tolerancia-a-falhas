package com.tolerancia.falhas.exchange.controller;

import com.tolerancia.falhas.exchange.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ExchangeController {
    
    @Autowired
    private ExchangeService exchangeService;
    
    @GetMapping("/convert")
    public ResponseEntity<Double> getExchangeRate() {
        Double rate = exchangeService.getExchangeRate();
        return ResponseEntity.ok(rate);
    }
}
