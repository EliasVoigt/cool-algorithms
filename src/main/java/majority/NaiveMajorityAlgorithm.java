package main.java.majority;

import main.java.core.Algorithm;

import java.util.OptionalInt;

public class NaiveMajorityAlgorithm extends Algorithm<int[], OptionalInt> {
    @Override
    public OptionalInt apply(int[] arr) {
        for (int candidate : arr) {
            int occurrences = 0;
            for (int value : arr) {
                if (value == candidate) {
                    occurrences++;
                }
            }
            if (occurrences > arr.length / 2) {
                return OptionalInt.of(candidate);
            }
        }

        return OptionalInt.empty();
    }

    @Override
    public String toString() {
        return "Naive Majority Algorithm";
    }
}
