package com.mstar.freightoptimizer.model;

import lombok.Data;

@Data
public class Vehicle {
    private Long id;
    private int capacity;
    private String shiftStart;
    private String shiftEnd;
}
