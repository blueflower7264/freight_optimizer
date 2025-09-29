package com.mstar.freightoptimizer.solver;

import com.mstar.freightoptimizer.model.RouteSolution;
import com.google.ortools.Loader;
import com.google.ortools.constraintsolver.*;

import java.util.ArrayList;
import java.util.List;

public class OrToolsSolver {

    public List<RouteSolution> solveVRP(List<Long> orderIds, List<Long> vehicleIds) {

        Loader.loadNativeLibraries();

        // Number of locations = orders + depot (index 0 is depot)
        int numLocations = orderIds.size() + 1;
        int numVehicles = vehicleIds.size();
        int depot = 0;

        // Dummy distance matrix (real use: integrate OSRM/GraphHopper)
        long[][] distanceMatrix = new long[numLocations][numLocations];
        for (int i = 0; i < numLocations; i++) {
            for (int j = 0; j < numLocations; j++) {
                distanceMatrix[i][j] = (i == j) ? 0 : Math.abs(i - j) * 10;
            }
        }

        // Demands (index 0 = depot)
        int[] demands = new int[numLocations];
        demands[0] = 0; // depot
        for (int i = 1; i < numLocations; i++) {
            demands[i] = 10; // example demand
        }

        // Vehicle capacities
        long[] vehicleCapacities = new long[numVehicles];
        for (int v = 0; v < numVehicles; v++) {
            vehicleCapacities[v] = 100;
        }

        // Create Routing Index Manager
        RoutingIndexManager manager =
                new RoutingIndexManager(numLocations, numVehicles, depot);

        // Routing Model
        RoutingModel routing = new RoutingModel(manager);

        // Distance Callback
        final int transitCallbackIndex = routing.registerTransitCallback((fromIndex, toIndex) -> {
            int fromNode = manager.indexToNode(fromIndex);
            int toNode = manager.indexToNode(toIndex);
            return distanceMatrix[fromNode][toNode];
        });

        routing.setArcCostEvaluatorOfAllVehicles(transitCallbackIndex);

        // Capacity constraints
        final int demandCallbackIndex = routing.registerUnaryTransitCallback(fromIndex -> {
            int fromNode = manager.indexToNode(fromIndex);
            return demands[fromNode];
        });
        routing.addDimensionWithVehicleCapacity(demandCallbackIndex, 0, vehicleCapacities, true, "Capacity");

        // Search parameters
        RoutingSearchParameters searchParameters =
                main.defaultRoutingSearchParameters()
                        .toBuilder()
                        .setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC)
                        .build();

        // Solve
        Assignment solution = routing.solveWithParameters(searchParameters);

        // Parse solution
        List<RouteSolution> routes = new ArrayList<>();
        if (solution != null) {
            for (int v = 0; v < numVehicles; v++) {
                List<Long> stops = new ArrayList<>();
                long routeDistance = 0;
                for (long index = routing.start(v);
                     !routing.isEnd(index);
                     index = solution.value(routing.nextVar(index))) {
                    int node = manager.indexToNode(index);
                    if (node != 0) {
                        stops.add(orderIds.get(node - 1));
                    }
                    long nextIndex = solution.value(routing.nextVar(index));
                    routeDistance += routing.getArcCostForVehicle(index, nextIndex, v);
                }

                if (!stops.isEmpty()) {
                    RouteSolution rs = new RouteSolution();
                    rs.setVehicleId(vehicleIds.get(v));
                    rs.setStops(stops);
                    rs.setDistance(routeDistance);
                    rs.setDuration(routeDistance / 30.0 + "h"); // dummy duration
                    routes.add(rs);
                }
            }
        }

        return routes;
    }
}
