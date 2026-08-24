# Algorithms & Benchmarking

A collection of algorithms that I find interesting, implemented in Java and "benchmarked" against generated inputs.

## Implemented Algorithms

| Algorithm                      | Category | Complexity            |
| ------------------------------ | -------- | ---------------------- |
| Majority Element (Boyer–Moore) | Arrays   | O(n) time, O(1) space  |

More algorithms will be added over time.

## Benchmarking

The project includes a small benchmarking framework for comparing multiple algorithms against the same problem.

Each benchmark:

* Generates a shared set of inputs via a `Problem`, reused across every algorithm being compared
* Performs JVM warm-up iterations on a separate set of generated inputs
* Measures execution time in nanoseconds for each algorithm
* Stores inputs and results per algorithm
* Reports minimum, maximum, and average execution time per algorithm

> These benchmarks are intended for a rough understanding and comparison rather than precise scientific performance measurements.

## Project Structure

```text
src/
└── main/
    └── java/
        ├── core/
        │   ├── Algorithm.java
        │   ├── Problem.java
        │   ├── Benchmark.java
        │   ├── ConsoleBenchmarkPrinter.java
        │   └── Timer.java
        │
        ├── datastructures/
        │   ├── arrays/
        │   │   └── RandomIntegerArrayGenerator.java
        │
        └── majority/
            ├── MajorityAlgorithm.java
            ├── MajorityProblem.java
            └── MajorityBenchmark.java
```

### `core`

Contains the common abstractions and infrastructure shared by every algorithm and benchmark: `Algorithm` (defines what it means to solve a problem), `Problem` (defines input generation, correctness checking, and formatting for a class of algorithms), `Benchmark` (runs one or more algorithms against shared generated inputs and collects timing data), `Timer`, and `ConsoleBenchmarkPrinter` (prints benchmark results to the console).

### `datastructures`

Contains reusable data types and their input generators, organized by data kind (e.g. `arrays`). These are shared across any feature package that needs randomly generated sample input of that type.

### Feature packages (e.g. `majority`)

Each algorithmic problem gets its own package containing everything specific to it: the `Algorithm` implementation(s), the `Problem` definition (wiring up a generator from `datastructures` plus correctness/formatting logic), and a small runnable benchmark class. As more algorithms are added, each one will get its own package.

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