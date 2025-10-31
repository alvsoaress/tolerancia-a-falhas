package com.tolerancia.falhas.fidelity.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FidelityService {
    
    private final Map<Long, Integer> userPoints = new HashMap<>();
    
    public void addBonus(Long userId, Integer bonus) {
        userPoints.merge(userId, bonus, Integer::sum);
        System.out.println("Added " + bonus + " points to user " + userId + 
                          ". Total points: " + userPoints.get(userId));
    }
    
    public Integer getUserPoints(Long userId) {
        return userPoints.getOrDefault(userId, 0);
    }
}
