# Algorithms & Benchmarking

A collection of algorithms that I find interesting, implemented in Java and "benchmarked" against generated inputs.

## Implemented Algorithms

| Algorithm                      | Category | Complexity            |
| ------------------------------ | -------- | --------------------- |
| Majority Element (Boyer–Moore) | Arrays   | O(n) time, O(1) space |

More algorithms will be added over time.

## Benchmarking

The project includes a small benchmarking framework for measuring algorithm execution times.

Each benchmark:

* Generates inputs using an `InputGenerator`
* Performs JVM warm-up iterations
* Measures execution time in nanoseconds
* Stores inputs and results
* Reports minimum, maximum, and average execution time

> These benchmarks are intended for a rough understanding and comparison rather than precise scientific performance measurements.

## Project Structure

```text
src/
└── main/
    └── java/
        ├── algorithms/
        │   ├── Algorithm.java
        │   └── arrays/
        │       └── MajorityAlgorithm.java
        │
        ├── benchmarking/
        │   ├── Benchmark.java
        │   ├── Timer.java
        │   └── arrays/
        │       └── MajorityAlgorithmBenchmark.java
        │
        └── datastructures/
            ├── InputGenerator.java
            └── arrays/
                └── RandomIntegerArrayGenerator.java
```

### `algorithms`

Contains the algorithms and common abstractions used by them.

### `benchmarking`

Contains the benchmarking infrastructure used to execute and measure algorithms.

### `datastructures`

Contains data structures and input generators used by the algorithms and benchmarks.

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
