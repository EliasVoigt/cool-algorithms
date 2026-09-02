package main.java.core.benchmarking.execution;

import main.java.core.Algorithm;
import main.java.core.benchmarking.results.ScalingResult;
import main.java.datastructures.InputGenerator;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ScalingBenchmark<I, R> {
    private static final int SCALING_WARM_UP_ITERATIONS = 3;
    private final List<Algorithm<I, R>> algorithms;
    private final InputGenerator<I> generator;

    public ScalingBenchmark(InputGenerator<I> generator, List<Algorithm<I, R>> algorithms) {
        this.algorithms = algorithms;
        this.generator = generator;
    }

    /**
     * Runs {@code trialsPerSize} trials at each input size, recording the average timing per algorithm.
     */
    public ScalingResult<I, R> run(int[] inputSizes, int trialsPerSize) {
        Map<Algorithm<I, R>, double[]> avgTimingsBySize = new LinkedHashMap<>();

        for (Algorithm<I, R> algorithm : algorithms) {
            double[] avgTimings = new double[inputSizes.length];

            for (int s = 0; s < inputSizes.length; s++) {
                int size = inputSizes[s];

                for (int w = 0; w < SCALING_WARM_UP_ITERATIONS; w++) {
                    algorithm.apply(generator.generateInput(size));
                }

                long total = 0;
                for (int t = 0; t < trialsPerSize; t++) {
                    I input = generator.generateInput(size);
                    Timer timer = new Timer();
                    timer.start();
                    algorithm.apply(input);
                    timer.stop();
                    total += timer.elapsedNanos();
                }
                avgTimings[s] = (double) total / trialsPerSize;
            }
            avgTimingsBySize.put(algorithm, avgTimings);
        }
        return new ScalingResult<>(inputSizes, avgTimingsBySize);
    }
}
