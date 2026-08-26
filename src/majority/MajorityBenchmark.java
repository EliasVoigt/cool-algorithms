package majority;

import core.*;
import core.benchmarking.execution.ScalingBenchmark;
import core.benchmarking.execution.SingleSizeBenchmark;
import core.benchmarking.results.ScalingResult;
import core.benchmarking.results.SingleSizeResult;
import core.benchmarking.visualization.ConsoleBenchmarkPrinter;
import core.benchmarking.visualization.RuntimeChartApp;
import datastructures.InputGenerator;
import datastructures.arrays.RandomIntegerArrayGenerator;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class MajorityBenchmark {
    public static void main(String[] args) {
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