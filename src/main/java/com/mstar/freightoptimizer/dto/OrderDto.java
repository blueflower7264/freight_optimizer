package com.mstar.freightoptimizer.dto;

import lombok.Data;

@Data
public class OrderDto {
    private int demand;
    private double latitude;   // North-South position
    private double longitude;  // East-West position
    private String startTime;  // Time window start (e.g., "09:00")
    private String endTime;    // Time window end (e.g., "12:00")
}
