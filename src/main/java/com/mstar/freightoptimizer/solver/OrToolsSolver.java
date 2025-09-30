package com.mstar.freightoptimizer.solver;

import com.mstar.freightoptimizer.model.RouteSolution;
import com.google.ortools.Loader;
import com.google.ortools.constraintsolver.*;
import com.mstar.freightoptimizer.service.DistanceMatrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class OrToolsSolver {

    private final DistanceMatrixService distanceMatrixService;

    @Autowired
    public OrToolsSolver(DistanceMatrixService distanceMatrixService) {
        this.distanceMatrixService = distanceMatrixService;
    }

    public List<RouteSolution> solveVRP(List<Long> orderIds,
                                        List<Long> vehicleIds,
                                        List<double[]> coordinates) {
        Loader.loadNativeLibraries();

        int numLocations = coordinates.size();
        int numVehicles = vehicleIds.size();
        int depot = 0; // first coordinate = depot

        // ✅ Build distance matrix from OSRM
        long[][] distanceMatrix = distanceMatrixService.buildDistanceMatrix(coordinates);

        // Dummy demands: one unit per order
        int[] demands = new int[numLocations];
        demands[0] = 0; // depot
        for (int i = 1; i < numLocations; i++) {
            demands[i] = 10;
        }

        long[] vehicleCapacities = new long[numVehicles];
        Arrays.fill(vehicleCapacities, 15);

        RoutingIndexManager manager =
                new RoutingIndexManager(numLocations, numVehicles, depot);

        RoutingModel routing = new RoutingModel(manager);

        final int transitCallbackIndex = routing.registerTransitCallback((fromIndex, toIndex) -> {
            int fromNode = manager.indexToNode(fromIndex);
            int toNode = manager.indexToNode(toIndex);
            return distanceMatrix[fromNode][toNode];
        });
        routing.setArcCostEvaluatorOfAllVehicles(transitCallbackIndex);

        final int demandCallbackIndex = routing.registerUnaryTransitCallback(fromIndex -> {
            int fromNode = manager.indexToNode(fromIndex);
            return demands[fromNode];
        });
        routing.addDimensionWithVehicleCapacity(demandCallbackIndex, 0, vehicleCapacities, true, "Capacity");

        RoutingSearchParameters searchParameters =
                main.defaultRoutingSearchParameters()
                        .toBuilder()
                        .setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC)
                        .setLocalSearchMetaheuristic(LocalSearchMetaheuristic.Value.GUIDED_LOCAL_SEARCH)
                        .setTimeLimit(com.google.protobuf.Duration.newBuilder().setSeconds(5).build())
                        .build();

        Assignment solution = routing.solveWithParameters(searchParameters);

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
                    // crude assumption: 30km/h speed
                    double hours = routeDistance / 30000.0;
                    int durationMinutes = (int) Math.round(hours * 60);

                    // Format into HH:mm
                    int hh = durationMinutes / 60;
                    int mm = durationMinutes % 60;
                    String durationFormatted = hh > 0
                            ? String.format("%02d:%02d", hh, mm)
                            : mm + " min";

                    // Format distance
                    double km = routeDistance / 1000.0;
                    String distanceFormatted = String.format("%.1f km", km);

                    RouteSolution rs = new RouteSolution();
                    rs.setVehicleIndex(v);
                    rs.setVehicleId(vehicleIds.size() > v ? vehicleIds.get(v) : null);
                    rs.setStops(stops);

                    rs.setDistanceMeters(routeDistance);
                    rs.setDistanceFormatted(distanceFormatted);

                    rs.setDurationMinutes(durationMinutes);
                    rs.setDurationFormatted(durationFormatted);

                    routes.add(rs);
                }
            }
        }

        return routes;
    }
}
