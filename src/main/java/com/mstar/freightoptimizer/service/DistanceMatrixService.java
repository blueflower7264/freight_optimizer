package com.mstar.freightoptimizer.service;

import java.util.List;

public interface DistanceMatrixService {

    long[][] buildDistanceMatrix(List<double[]> locations);
}
