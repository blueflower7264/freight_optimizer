package com.mstar.freightoptimizer.service;


import com.mstar.freightoptimizer.model.*;

import java.util.List;

public interface OptimizationService {

    List<RouteSolution> optimizeEntities(List<OrderEntity> orders, List<VehicleEntity> vehicles);
}
