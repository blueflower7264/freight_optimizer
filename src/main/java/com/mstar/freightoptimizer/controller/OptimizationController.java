package com.mstar.freightoptimizer.controller;

import com.mstar.freightoptimizer.model.*;
import com.mstar.freightoptimizer.repository.OrderRepository;
import com.mstar.freightoptimizer.repository.RouteRepository;
import com.mstar.freightoptimizer.repository.RouteStopRepository;
import com.mstar.freightoptimizer.repository.VehicleRepository;
import com.mstar.freightoptimizer.service.OptimizationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class OptimizationController {

    private final OptimizationService optimizationService;
    private final OrderRepository orderRepository;
    private final VehicleRepository vehicleRepository;
    private final RouteRepository routeRepository;
    private final RouteStopRepository routeStopRepository;

    public OptimizationController(OptimizationService optimizationService,
                                  OrderRepository orderRepository,
                                  VehicleRepository vehicleRepository,
                                  RouteRepository routeRepository,
                                  RouteStopRepository routeStopRepository) {
        this.optimizationService = optimizationService;
        this.orderRepository = orderRepository;
        this.vehicleRepository = vehicleRepository;
        this.routeRepository = routeRepository;
        this.routeStopRepository = routeStopRepository;
    }

    @PostMapping("/optimize")
    public List<RouteSolution> optimize(@RequestBody Map<String, List<?>> request) {
        // Deserialize raw input into entities
        List<OrderEntity> orders = (List<OrderEntity>) request.get("orders");
        List<VehicleEntity> vehicles = (List<VehicleEntity>) request.get("vehicles");

        // Persist to database
        orderRepository.saveAll(orders);
        vehicleRepository.saveAll(vehicles);

        // Run solver (stubbed for now)
        List<RouteSolution> solutions = optimizationService.optimizeEntities(orders, vehicles);

        // Save solutions into DB
        for (RouteSolution sol : solutions) {
            RouteEntity route = new RouteEntity();
            route.setVehicleId(sol.getVehicleId());
            route.setDistance(sol.getDistance());
            route.setDuration(sol.getDuration());
            route = routeRepository.save(route);

            int seq = 1;
            for (Long orderId : sol.getStops()) {
                RouteStopEntity stop = new RouteStopEntity();
                stop.setRoute(route);
                stop.setOrder(orderRepository.findById(orderId).orElseThrow());
                stop.setStopSequence(seq++);
                routeStopRepository.save(stop);
            }
        }

        return solutions;
    }
}
