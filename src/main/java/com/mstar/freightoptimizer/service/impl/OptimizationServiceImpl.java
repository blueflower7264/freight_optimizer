package com.mstar.freightoptimizer.service.impl;

import com.mstar.freightoptimizer.dto.DepotDto;
import com.mstar.freightoptimizer.model.*;
import com.mstar.freightoptimizer.repository.OrderRepository;
import com.mstar.freightoptimizer.repository.VehicleRepository;
import com.mstar.freightoptimizer.service.OptimizationService;
import com.mstar.freightoptimizer.solver.OrToolsSolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OptimizationServiceImpl implements OptimizationService {

    private final OrToolsSolver solver;
    private final OrderRepository orderRepository;
    private final VehicleRepository vehicleRepository;

    @Autowired
    public OptimizationServiceImpl(OrToolsSolver solver, OrderRepository orderRepository, VehicleRepository vehicleRepository) {
        this.solver = solver;
        this.orderRepository = orderRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public List<RouteSolution> optimizeEntities(
            List<OrderEntity> orders,
            List<VehicleEntity> vehicles,
            DepotDto depot
    ) {
        // Save orders + vehicles so IDs exist
        List<OrderEntity> savedOrders = orderRepository.saveAll(orders);
        List<VehicleEntity> savedVehicles = vehicleRepository.saveAll(vehicles);

        List<Long> orderIds = savedOrders.stream()
                .map(OrderEntity::getId)
                .collect(Collectors.toList());

        List<Long> vehicleIds = savedVehicles.stream()
                .map(VehicleEntity::getId)
                .collect(Collectors.toList());

        // Build coordinates list in [lat, lon]
        List<double[]> coordinates = new ArrayList<>();
        coordinates.add(new double[]{depot.getLatitude(), depot.getLongitude()}); // depot
        savedOrders.forEach(o ->
                coordinates.add(new double[]{o.getLatitude(), o.getLongitude()})
        );

        return solver.solveVRP(orderIds, vehicleIds, coordinates);
    }
}
