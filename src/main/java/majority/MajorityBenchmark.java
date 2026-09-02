package main.java.majority;

import main.java.core.Algorithm;
import main.java.core.benchmarking.execution.ScalingBenchmark;
import main.java.core.benchmarking.execution.SingleSizeBenchmark;
import main.java.core.benchmarking.results.ScalingResult;
import main.java.core.benchmarking.results.SingleSizeResult;
import main.java.core.benchmarking.visualization.ConsoleBenchmarkPrinter;
import main.java.core.benchmarking.visualization.RuntimeChartApp;
import main.java.datastructures.InputGenerator;
import main.java.datastructures.arrays.RandomIntegerArrayGenerator;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class MajorityBenchmark {
    static void main() {
        InputGenerator<int[]> generator = new RandomIntegerArrayGenerator()
                .length(100)
                .valueRange(1, 3);

        List<Algorithm<int[], OptionalInt>> algorithms = List.of(
                new NaiveMajorityAlgorithm(),
                new MapBasedMajorityAlgorithm(),
                new BoyerMooreVotingAlgorithm()
        );

        SingleSizeBenchmark<int[], OptionalInt> singleSizeBenchmark = new SingleSizeBenchmark<>(generator, algorithms);
        SingleSizeResult<int[], OptionalInt> singleSizeResult = singleSizeBenchmark.run(30);
        ConsoleBenchmarkPrinter.print(singleSizeResult);

        ScalingBenchmark<int[], OptionalInt> scalingBenchmark = new ScalingBenchmark<>(generator, algorithms);
        int[] sizes = IntStream.iterate(10, n -> n <= 3000, n -> n + 25).toArray();
        ScalingResult<int[], OptionalInt> scalingResult = scalingBenchmark.run(sizes, 300);
        RuntimeChartApp.showFigure(scalingResult, "Majority-Benchmark");
        RuntimeChartApp.saveFigure(scalingResult, "Majority-Benchmark");
    }
}