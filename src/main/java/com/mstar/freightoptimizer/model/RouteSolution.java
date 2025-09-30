package com.mstar.freightoptimizer.model;

import lombok.Data;

import java.util.List;

@Data
public class RouteSolution {
    private Long vehicleId;             // DB id of the vehicle (maybe null if not persisted)
    private int vehicleIndex;           // Solver index (0 = first vehicle, 1 = second, etc.)
    private List<Long> stops;           // Order IDs served by this vehicle

    private long distanceMeters;        // Raw distance in meters
    private String distanceFormatted;   // Pretty distance (e.g., "3.4 km")

    private int durationMinutes;        // Duration in minutes (raw, for calculations)
    private String durationFormatted;   // Pretty string (e.g., "7 min", "00:07")
}
