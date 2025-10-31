package com.tolerancia.falhas.airlineshub.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellRequest {
    @NotBlank(message = "Flight number is required")
    private String flight;
    
    @NotBlank(message = "Day is required")
    private String day;
}
