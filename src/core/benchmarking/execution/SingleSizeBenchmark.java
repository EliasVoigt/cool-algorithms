package core.benchmarking.execution;

import core.Algorithm;
import core.benchmarking.results.SingleSizeResult;
import datastructures.InputGenerator;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SingleSizeBenchmark<I, R> {
    private static final int WARM_UP_ITERATIONS = 8;
    private final InputGenerator<I> generator;
    private final List<Algorithm<I, R>> algorithms;

    public SingleSizeBenchmark(InputGenerator<I> generator, List<Algorithm<I, R>> algorithms) {
        this.generator = generator;
        this.algorithms = algorithms;
    }

    private I[] generateInputs(int count) {
        @SuppressWarnings("unchecked")
        I[] result = (I[]) new Object[count];
        for (int i = 0; i < count; i++) {
            result[i] = generator.generateInput();
        }
        return result;
    }

    /**
     * Runs {@code numberOfExecutions} distinct random inputs, one execution each per algorithm.
     */
    public SingleSizeResult<I, R> run(int numberOfExecutions) {
        Map<Algorithm<I, R>, R[]> resultsByAlgorithm = new LinkedHashMap<>();
        Map<Algorithm<I, R>, long[]> timingsByAlgorithm = new LinkedHashMap<>();

        I[] warmupInputs = generateInputs(WARM_UP_ITERATIONS);
        I[] inputs = generateInputs(numberOfExecutions);

        for (Algorithm<I, R> algorithm : algorithms) {
            for (I warmupInput : warmupInputs) {
                algorithm.apply(warmupInput);
            }

            long[] timings = new long[numberOfExecutions];
            @SuppressWarnings("unchecked")
            R[] results = (R[]) new Object[numberOfExecutions];

            for (int i = 0; i < numberOfExecutions; i++) {
                Timer timer = new Timer();

                timer.start();
                R result = algorithm.apply(inputs[i]);
                timer.stop();

                results[i] = result;
                timings[i] = timer.elapsedNanos();
            }
            resultsByAlgorithm.put(algorithm, results);
            timingsByAlgorithm.put(algorithm, timings);
        }
        return new SingleSizeResult<>(inputs, numberOfExecutions, resultsByAlgorithm, timingsByAlgorithm);
    }
}