package com.mstar.freightoptimizer.dto;

import com.mstar.freightoptimizer.model.OrderEntity;
import com.mstar.freightoptimizer.model.VehicleEntity;
import lombok.Data;

import java.util.List;

@Data
public class OptimizeRequest {

    private List<OrderEntity> orders;
    private List<VehicleEntity> vehicles;
}
