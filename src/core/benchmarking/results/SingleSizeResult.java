package core.benchmarking.results;

import core.Algorithm;

import java.util.Arrays;
import java.util.Map;

public record SingleSizeResult<I, R>(
        I[] inputs,
        int numberOfExecutions,
        Map<Algorithm<I, R>, R[]> resultsByAlgorithm,
        Map<Algorithm<I, R>, long[]> timingsByAlgorithm
) {
    public R[] getResults(Algorithm<I, R> algorithm) {
        return resultsByAlgorithm.get(algorithm);
    }

    public long[] getTimings(Algorithm<I, R> algorithm) {
        return timingsByAlgorithm.get(algorithm);
    }

    public double average(Algorithm<I, R> algorithm) {
        return Arrays.stream(getTimings(algorithm)).average().orElse(Double.NaN);
    }

    public long min(Algorithm<I, R> algorithm) {
        return Arrays.stream(getTimings(algorithm)).min().orElse(0);
    }

    public long max(Algorithm<I, R> algorithm) {
        return Arrays.stream(getTimings(algorithm)).max().orElse(0);
    }
}