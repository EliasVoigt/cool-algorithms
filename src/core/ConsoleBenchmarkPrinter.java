package core;

public class ConsoleBenchmarkPrinter {
    public static <I, R> void print(Benchmark<I, R> benchmark) {
        for (Algorithm<I, R> algorithm : benchmark.getAlgorithms()) {
            Benchmark<I, R>.AlgorithmResult r = benchmark.getResult(algorithm);
            if (r == null) {
                throw new IllegalStateException("No results for " + algorithm.getName() + " — did you call run()?");
            }

            String header = "========================== " + benchmark.getProblem().getName().toUpperCase()
                    + " — " + algorithm.getName().toUpperCase() + " ==========================";
            System.out.println(header);

            I[] inputs = benchmark.getInputs();
            for (int i = 0; i < inputs.length; i++) {
                System.out.printf("input=%s, result=%s, time=%d ns%n",
                        benchmark.getProblem().formatInput(inputs[i]),
                        benchmark.getProblem().formatResult(r.getResults()[i]),
                        r.getTimings()[i]);
            }
            System.out.printf("%nStatistics:   min=%d ns, max=%d ns, avg=%.1f ns/%d%n",
                    r.min(), r.max(), r.average(), benchmark.getNumberOfExecutions());
            System.out.println("=".repeat(header.length()));
        }
    }
}
