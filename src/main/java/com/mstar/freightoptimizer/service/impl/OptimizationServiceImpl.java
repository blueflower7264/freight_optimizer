package com.mstar.freightoptimizer.service.impl;

import com.mstar.freightoptimizer.model.*;
import com.mstar.freightoptimizer.repository.OrderRepository;
import com.mstar.freightoptimizer.service.OptimizationService;
import com.mstar.freightoptimizer.solver.OrToolsSolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptimizationServiceImpl implements OptimizationService {

    @Autowired
    private OrderRepository orderRepository;

    private final OrToolsSolver solver = new OrToolsSolver();

    @Override
    public void createOrder() {
        OrderEntity order = new OrderEntity();
        order.setDemand(15);
        order.setLatitude(40.7128);
        order.setLongitude(-74.0060);
        order.setStartTime("09:00");
        order.setEndTime("12:00");
        orderRepository.save(order);
    }

    @Override
    public List<RouteSolution> optimizeEntities(List<OrderEntity> orders, List<VehicleEntity> vehicles) {
        // Convert entities to a lightweight DTO if needed
        return solver.solveVRP(
                orders.stream().map(OrderEntity::getId).toList(),
                vehicles.stream().map(VehicleEntity::getId).toList()
        );
    }

}
