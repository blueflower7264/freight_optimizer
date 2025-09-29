package com.mstar.freightoptimizer.model;

import lombok.Data;

import java.util.List;

@Data
public class RouteSolution {
    private Long vehicleId;
    private List<Long> stops;
    private double distance;
    private String duration;
}
