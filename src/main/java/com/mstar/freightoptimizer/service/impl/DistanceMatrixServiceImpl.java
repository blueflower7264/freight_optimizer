package com.mstar.freightoptimizer.service.impl;

import com.mstar.freightoptimizer.service.DistanceMatrixService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.List;

@Service
public class DistanceMatrixServiceImpl implements DistanceMatrixService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    // Format coordinates with 6 decimals (OSRM is strict)
    private static final DecimalFormat df = new DecimalFormat("0.000000");

    @Override
    public long[][] buildDistanceMatrix(List<double[]> coordinates) {
        if (coordinates == null || coordinates.isEmpty()) {
            throw new IllegalArgumentException("Coordinates list cannot be empty");
        }

        StringBuilder coords = new StringBuilder();
        for (double[] loc : coordinates) {
            if (!coords.isEmpty()) coords.append(";");
            String lon = df.format(loc[1]);
            String lan = df.format(loc[0]);
            coords.append(lon).append(",").append(lan); // lon,lat
        }

        // Table (distance matrix): http://localhost:5000/table/v1/driving/{lon,lat};{lon,lat};...
        // Route (detailed path): http://localhost:5000/route/v1/driving/{lon,lat};{lon,lat}
        String url = "http://localhost:5000/table/v1/driving/" + coords;
        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(response);

            JsonNode durations = root.get("durations"); // default OSRM output
            int n = durations.size();

            long[][] matrix = new long[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] = durations.get(i).get(j).asLong(); // seconds
                }
            }
            return matrix;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching matrix from OSRM at: " + url, e);
        }
    }
}
