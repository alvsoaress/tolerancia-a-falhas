package com.tolerancia.falhas.fidelity.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BonusRequest {
    @NotNull(message = "User ID is required")
    private Long user;
    
    @NotNull(message = "Bonus value is required")
    @Positive(message = "Bonus value must be positive")
    private Integer bonus;
}
