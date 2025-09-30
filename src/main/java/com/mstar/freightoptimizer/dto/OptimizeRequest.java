package com.mstar.freightoptimizer.dto;

import lombok.Data;

import java.util.List;

@Data
public class OptimizeRequest {

    private DepotDto depot;
    private List<OrderDto> orders;
    private List<VehicleDto> vehicles;
}
