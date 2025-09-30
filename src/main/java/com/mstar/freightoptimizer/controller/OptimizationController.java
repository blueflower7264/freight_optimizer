package com.mstar.freightoptimizer.controller;

import com.mstar.freightoptimizer.dto.OptimizeRequest;
import com.mstar.freightoptimizer.model.*;
import com.mstar.freightoptimizer.service.OptimizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OptimizationController {

    private final OptimizationService optimizationService;

    @Autowired
    public OptimizationController(OptimizationService optimizationService) {
        this.optimizationService = optimizationService;
    }

    @PostMapping("/optimize")
    public List<RouteSolution> optimize(@RequestBody OptimizeRequest request) {
        // ✅ Map OrderDto → OrderEntity
        List<OrderEntity> orderEntities = request.getOrders().stream()
                .map(dto -> {
                    OrderEntity o = new OrderEntity();
                    o.setDemand(dto.getDemand());
                    o.setLatitude(dto.getLatitude());
                    o.setLongitude(dto.getLongitude());
                    o.setStartTime(dto.getStartTime());
                    o.setEndTime(dto.getEndTime());
                    return o;
                })
                .toList();

        // ✅ Map VehicleDto → VehicleEntity
        List<VehicleEntity> vehicleEntities = request.getVehicles().stream()
                .map(dto -> {
                    VehicleEntity v = new VehicleEntity();
                    v.setCapacity(dto.getCapacity());
                    v.setShiftStart(dto.getShiftStart());
                    v.setShiftEnd(dto.getShiftEnd());
                    return v;
                })
                .toList();

        // ✅ Call service with mapped entities + depot
        return optimizationService.optimizeEntities(
                orderEntities,
                vehicleEntities,
                request.getDepot()
        );
    }
}
