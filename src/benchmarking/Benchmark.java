package benchmarking;

import algorithms.Algorithm;
import datastructures.InputGenerator;

import java.util.Arrays;
import java.util.function.Function;

public class Benchmark<I, R> {
    private static final int WARM_UP_ITERATIONS = 8;

    private final Algorithm<I, R> algorithm;
    private final InputGenerator<I> inputGenerator;
    private final Function<R, String> resultFormatter;
    private final int numberOfExecutions;

    private long[] timings;
    private Object[] inputs;  // I[]
    private Object[] results; // R[]

    public Benchmark(Algorithm<I, R> algorithm, InputGenerator<I> inputGenerator, Function<R, String> resultFormatter, int numberOfExecutions) {
        this.algorithm = algorithm;
        this.inputGenerator = inputGenerator;
        this.resultFormatter = resultFormatter;
        this.numberOfExecutions = numberOfExecutions;
    }

    public void run() {
        warmup();

        timings = new long[numberOfExecutions];
        inputs = new Object[numberOfExecutions];
        results = new Object[numberOfExecutions];

        for (int i = 0; i < numberOfExecutions; i++) {
            I input = inputGenerator.generateSample();
            Timer timer = new Timer();

            timer.start();
            R result = algorithm.apply(input);
            timer.stop();

            inputs[i] = input;
            results[i] = result;
            timings[i] = timer.elapsedNanos();
        }
    }

    private void warmup() {
        for (int w = 0; w < WARM_UP_ITERATIONS; w++) {
            algorithm.apply(inputGenerator.generateSample());
        }
    }

    @SuppressWarnings("unchecked")
    public I getInput(int i) {
        return (I) inputs[i];
    }

    @SuppressWarnings("unchecked")
    public R getResult(int i) {
        return (R) results[i];
    }

    public double averageNanos() {
        return Arrays.stream(timings).average().orElse(Double.NaN);
    }

    public long minNanos() {
        return Arrays.stream(timings).min().orElse(0);
    }

    public long maxNanos() {
        return Arrays.stream(timings).max().orElse(0);
    }

    public void printResults() {
        System.out.println("========================== " + algorithm.getName().toUpperCase() + " ==========================");
        for (int i = 0; i < numberOfExecutions; i++) {
            System.out.printf("input=%s, result=%s, time=%d ns%n",
                    inputGenerator.format(getInput(i)), resultFormatter.apply(getResult(i)), timings[i]);
        }
        System.out.printf("\nStatistics:   min=%d ns, max=%d ns, avg=%.1f ns%n", minNanos(), maxNanos(), averageNanos());
        System.out.println("===========================" + "=".repeat(algorithm.getName().length()) + "===========================");
    }
}