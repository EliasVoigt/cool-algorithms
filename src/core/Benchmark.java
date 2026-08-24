package core;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Benchmark<I, R> {
    private static final int WARM_UP_ITERATIONS = 8;

    private final Problem<I, R> problem;
    private final Set<Algorithm<I, R>> algorithms;
    private final int numberOfExecutions;
    private I[] inputs;
    private I[] warmupInputs;

    private final Map<Algorithm<I, R>, AlgorithmResult> resultsByAlgorithm = new LinkedHashMap<>();

    public Benchmark(Problem<I, R> problem, Set<Algorithm<I, R>> algorithms, int numberOfExecutions) {
        this.problem = problem;
        this.algorithms = algorithms;
        this.numberOfExecutions = numberOfExecutions;
    }

    public void run() {
        generateWarmupInputs();
        generateInputs();

        for (Algorithm<I, R> algorithm : algorithms) {
            warmup(algorithm);

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
            resultsByAlgorithm.put(algorithm, new AlgorithmResult(results, timings));
        }
    }

    @SuppressWarnings("unchecked")
    private void generateInputs() {
        inputs = (I[]) new Object[numberOfExecutions];
        for (int i = 0; i < numberOfExecutions; i++) {
            inputs[i] = problem.generateInput();
        }
    }

    @SuppressWarnings("unchecked")
    private void generateWarmupInputs() {
        warmupInputs = (I[]) new Object[WARM_UP_ITERATIONS];
        for (int i = 0; i < WARM_UP_ITERATIONS; i++) {
            warmupInputs[i] = problem.generateInput();
        }
    }

    private void warmup(Algorithm<I, R> algorithm) {
        for (I input : warmupInputs) {
            algorithm.apply(input);
        }
    }

    public Problem<I, R> getProblem() {
        return problem;
    }

    public Set<Algorithm<I, R>> getAlgorithms() {
        return algorithms;
    }

    public int getNumberOfExecutions() {
        return numberOfExecutions;
    }

    public I[] getInputs() {
        return inputs;
    }

    public AlgorithmResult getResult(Algorithm<I, R> algorithm) {
        return resultsByAlgorithm.get(algorithm);
    }

    public class AlgorithmResult {
        private final R[] results;
        private final long[] timings;

        AlgorithmResult(R[] results, long[] timings) {
            this.results = results;
            this.timings = timings;
        }

        public R[] getResults() {
            return results;
        }

        public long[] getTimings() {
            return timings;
        }

        double average() {
            return Arrays.stream(timings).average().orElse(Double.NaN);
        }

        long min() {
            return Arrays.stream(timings).min().orElse(0);
        }

        long max() {
            return Arrays.stream(timings).max().orElse(0);
        }
    }
}