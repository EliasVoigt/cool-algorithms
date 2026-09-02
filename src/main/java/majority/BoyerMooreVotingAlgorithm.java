package main.java.majority;

import main.java.core.Algorithm;

import java.util.OptionalInt;

public final class BoyerMooreVotingAlgorithm extends Algorithm<int[], OptionalInt> {
    @Override
    public OptionalInt apply(int[] arr) {
        if (arr.length == 0) {
            return OptionalInt.empty();
        }

        int candidate = arr[0];
        int count = 1;
        for (int i = 1; i < arr.length; i++) {
            if (count == 0) {
                candidate = arr[i];
                count = 1;
            } else if (arr[i] == candidate) {
                count++;
            } else {
                count--;
            }
        }

        int occurrences = 0;
        for (int value : arr) {
            if (value == candidate) {
                occurrences++;
            }
        }

        return occurrences > arr.length / 2 ? OptionalInt.of(candidate) : OptionalInt.empty();
    }

    @Override
    public String toString() {
        return "Boyer-Moore Voting Algorithm";
    }
}