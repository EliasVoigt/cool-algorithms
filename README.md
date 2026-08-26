# Algorithms & Benchmarking

A collection of algorithms that I find interesting, implemented in Java and "benchmarked" against generated inputs.

## Implemented Algorithms

| Algorithm                      | Category | Complexity            |
|--------------------------------|----------|-----------------------|
| Majority Element (Boyer–Moore) | Arrays   | O(n) time, O(1) space |

More algorithms will be added over time.

## Benchmarking

The project includes a small benchmarking framework for comparing multiple algorithms against the same problem.

Each benchmark:

* Generates a shared set of inputs via a `Problem`, reused across every algorithm being compared
* Performs JVM warm-up iterations on a separate set of generated inputs
* Measures execution time in nanoseconds for each algorithm
* Stores inputs and results per algorithm
* Reports minimum, maximum, and average execution time per algorithm

> These benchmarks are intended for a rough understanding and comparison rather than precise scientific performance
> measurements.

## Project Structure

```text
src/
└── main/
    └── java/
        ├── core/
        │   ├── benchmarking/
        │   │   ├── execution/
        │   │   │   ├── ScalingBenchmark.java
        │   │   │   ├── SingleSizeBenchmark.java
        │   │   │   └── Timer.java
        │   │   ├── results/
        │   │   │   ├── ScalingResult.java
        │   │   │   └── SingleSizeResult.java
        │   │   └── visualization/
        │   │       ├── ConsoleBenchmarkPrinter.java
        │   │       ├── Formatter.java
        │   │       └── RuntimeChartApp.java
        │   └── Algorithm.java
        │
        ├── datastructures/
        │   └── arrays/
        │       └── RandomIntegerArrayGenerator.java
        │
        └── majority/
            ├── BoyerMooreVotingAlgorithm.java
            ├── Majority-Benchmark.png
            ├── MajorityBenchmark.java
            ├── MapBasedMajorityAlgorithm.java
            └── NaiveMajorityAlgorithm.java
```

### `core`

Contains the common abstractions and infrastructure shared by every algorithm and benchmark. `Algorithm` sits directly
in `core`. The `benchmarking` package is split into three subpackages: `execution`, which runs algorithms against
generated inputs and collects timing data (`ScalingBenchmark`, `SingleSizeBenchmark`, `Timer`); `results`, the value
types holding a benchmark run's outcome (`ScalingResult`, `SingleSizeResult`); and `visualization`, which turns those
results into output: console text or a chart (`ConsoleBenchmarkPrinter`, `Formatter`, `RuntimeChartApp`).

### `datastructures`

Contains reusable data types and their input generators, organized by data kind (e.g. `arrays`). These are shared across
any feature package that needs randomly generated sample input of that type.

### Feature packages (e.g. `majority`)

Each algorithmic problem gets its own package containing everything specific to it: one or more `Algorithm`
implementations (e.g. `BoyerMooreVotingAlgorithm`, `MapBasedMajorityAlgorithm`, `NaiveMajorityAlgorithm`), a small
runnable benchmark class (`MajorityBenchmark`), and any supporting assets such as a generated results chart
(`Majority-Benchmark.png`). As more algorithms are added, each one will get its own package.

## Algorithms I still have in mind

Not necessarily a roadmap - just algorithms that I think are interesting.

### Arrays & Searching

* [ ] Quickselect
* [ ] Median-of-Medians (Selection of 5)

### Sorting

* [ ] PowerSort
* [ ] PeekSort

### Approximation Algorithms

* [ ] Christofides
* [ ] Independent-Set-Based Coloring