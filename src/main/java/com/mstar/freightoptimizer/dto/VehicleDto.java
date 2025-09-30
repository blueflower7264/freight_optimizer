package com.mstar.freightoptimizer.dto;

import lombok.Data;

@Data
public class VehicleDto {
    private int capacity;
    private String shiftStart;
    private String shiftEnd;
}
