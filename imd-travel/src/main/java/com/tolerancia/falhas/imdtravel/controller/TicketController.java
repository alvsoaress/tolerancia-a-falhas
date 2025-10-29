package com.tolerancia.falhas.imdtravel.controller;

import com.tolerancia.falhas.imdtravel.dto.BuyTicketRequest;
import com.tolerancia.falhas.imdtravel.dto.BuyTicketResponse;
import com.tolerancia.falhas.imdtravel.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TicketController {
    
    @Autowired
    private TicketService ticketService;
    
    @PostMapping("/buyTicket")
    public ResponseEntity<BuyTicketResponse> buyTicket(@Valid @RequestBody BuyTicketRequest request) {
        BuyTicketResponse response = ticketService.buyTicket(request);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
