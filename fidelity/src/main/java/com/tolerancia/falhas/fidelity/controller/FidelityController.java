package com.tolerancia.falhas.fidelity.controller;

import com.tolerancia.falhas.fidelity.dto.BonusRequest;
import com.tolerancia.falhas.fidelity.service.FidelityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class FidelityController {
    
    @Autowired
    private FidelityService fidelityService;
    
    @PostMapping("/bonus")
    public ResponseEntity<Void> addBonus(@Valid @RequestBody BonusRequest request) {
        fidelityService.addBonus(request.getUser(), request.getBonus());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
