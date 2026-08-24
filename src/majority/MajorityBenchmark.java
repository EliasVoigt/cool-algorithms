package majority;

import core.Benchmark;
import core.ConsoleBenchmarkPrinter;
import datastructures.arrays.RandomIntegerArrayGenerator;

import java.util.OptionalInt;
import java.util.Set;

public class MajorityBenchmark {
    public static void main(String[] args) {
        RandomIntegerArrayGenerator generator = new RandomIntegerArrayGenerator()
                .length(7)
                .valueRange(1, 3);

        MajorityProblem problem = new MajorityProblem(generator);

        Set<core.Algorithm<int[], OptionalInt>> algorithms = Set.of(
                new MajorityAlgorithm()
        );

        Benchmark<int[], OptionalInt> benchmark = new Benchmark<>(problem, algorithms, 30);

        benchmark.run();
        ConsoleBenchmarkPrinter.print(benchmark);
    }
}