package benchmarking.arrays;

import datastructures.InputGenerator;
import algorithms.arrays.MajorityAlgorithm;
import benchmarking.Benchmark;
import datastructures.arrays.RandomIntegerArrayGenerator;

import java.util.OptionalInt;
import java.util.function.Function;

public class MajorityAlgorithmBenchmark {
    public static void main(String[] args) {
        InputGenerator<int[]> inputGenerator = new RandomIntegerArrayGenerator().length(10).valueRange(1, 3);
        Function<OptionalInt, String> resultFormatter = result ->
                result.isPresent()
                        ? String.valueOf(result.getAsInt())
                        : "none";

        Benchmark<int[], OptionalInt> benchmark = new Benchmark<>(new MajorityAlgorithm(), inputGenerator, resultFormatter, 30);

        benchmark.run();
        benchmark.printResults();
    }
}