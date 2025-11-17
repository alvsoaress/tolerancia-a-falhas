package com.tolerancia.falhas.imdtravel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyTicketRequest {
    @NotBlank(message = "Flight number is required")
    private String flight;
    
    @NotBlank(message = "Day is required")
    private String day;
    
    @NotNull(message = "User ID is required")
    private Long user;

    private Boolean ft = Boolean.FALSE;
}
