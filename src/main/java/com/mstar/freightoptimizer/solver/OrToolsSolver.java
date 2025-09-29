package com.mstar.freightoptimizer.solver;

import com.mstar.freightoptimizer.model.RouteSolution;

import java.util.Collections;
import java.util.List;

public class OrToolsSolver {

    public List<RouteSolution> solveVRP(List<Long> orderIds, List<Long> vehicleIds) {
        // TODO: Integrate OR-Tools / Gurobi
        // Dummy solution for now
        RouteSolution solution = new RouteSolution();
        solution.setVehicleId(vehicleIds.get(0));
        solution.setStops(Collections.singletonList(orderIds.get(0)));
        solution.setDistance(12.5);
        solution.setDuration("00:45");
        return List.of(solution);
    }
}
