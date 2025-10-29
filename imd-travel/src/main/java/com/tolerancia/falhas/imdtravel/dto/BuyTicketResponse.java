package com.tolerancia.falhas.imdtravel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyTicketResponse {
    private boolean success;
    private String transactionId;
    private String message;
}
