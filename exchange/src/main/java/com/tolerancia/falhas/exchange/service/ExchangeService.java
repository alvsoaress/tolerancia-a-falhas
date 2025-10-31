package com.tolerancia.falhas.exchange.service;

import org.springframework.stereotype.Service;

@Service
public class ExchangeService {
    
    public Double getExchangeRate() {
        double minRate = 5.0;
        double maxRate = 6.0;
        return minRate + Math.random() * (maxRate - minRate);
    }
}
