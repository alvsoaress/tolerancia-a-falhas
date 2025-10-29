package com.tolerancia.falhas.imdtravel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightInfo {
    private String flight;
    private String day;
    private Double value;
}
