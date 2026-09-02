package main.java.core.benchmarking.results;

import main.java.core.Algorithm;

import java.util.Map;

public record ScalingResult<I, R>(
        int[] inputSizes,
        Map<Algorithm<I, R>, double[]> averageTimingsByAlgorithm
) {
}
