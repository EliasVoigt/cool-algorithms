package main.java.core.benchmarking.visualization;

import main.java.core.Algorithm;
import main.java.core.benchmarking.results.SingleSizeResult;

public class ConsoleBenchmarkPrinter {
    public static <I, R> void print(SingleSizeResult<I, R> result) {
        for (Algorithm<I, R> algorithm : result.resultsByAlgorithm().keySet()) {

            String header = "========================== " + algorithm.toString().toUpperCase() + " ==========================";
            System.out.println(header);

            I[] inputs = result.inputs();
            for (int i = 0; i < inputs.length; i++) {
                System.out.printf("input=%s, result=%s, time=%d ns%n",
                        Formatter.format(inputs[i]),
                        Formatter.format(result.getResults(algorithm)[i]),
                        result.getTimings(algorithm)[i]);
            }
            System.out.printf("%nStatistics:   min=%d ns, max=%d ns, avg=%.1f ns/%d%n",
                    result.min(algorithm), result.max(algorithm), result.average(algorithm), result.numberOfExecutions());
            System.out.println("=".repeat(header.length()));
        }
    }
}