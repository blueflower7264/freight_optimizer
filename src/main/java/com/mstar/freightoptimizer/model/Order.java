package com.mstar.freightoptimizer.model;

import lombok.Data;

@Data
public class Order {
    private Long id;
    private int demand;
    private double latitude;
    private double longitude;
    private String startTime;
    private String endTime;
}
