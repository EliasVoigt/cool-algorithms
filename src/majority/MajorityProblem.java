package majority;

import core.Problem;
import datastructures.arrays.RandomIntegerArrayGenerator;

import java.util.Objects;
import java.util.OptionalInt;

public class MajorityProblem extends Problem<int[], OptionalInt> {

    private final RandomIntegerArrayGenerator generator;

    public MajorityProblem(RandomIntegerArrayGenerator generator) {
        this.generator = Objects.requireNonNull(generator);
    }

    @Override
    public int[] generateInput() {
        return generator.generateSample();
    }

    @Override
    public boolean isCorrect(int[] input, OptionalInt result) {
        if (result.isPresent()) {
            return countOccurrences(input, result.getAsInt()) > input.length / 2;
        }
        return noElementHasMajority(input);
    }

    private boolean noElementHasMajority(int[] input) {
        for (int candidate : input) {
            if (countOccurrences(input, candidate) > input.length / 2) {
                return false;
            }
        }
        return true;
    }

    private int countOccurrences(int[] input, int value) {
        int count = 0;
        for (int element : input) {
            if (element == value) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String formatInput(int[] input) {
        return RandomIntegerArrayGenerator.format(input);
    }

    @Override
    public String formatResult(OptionalInt result) {
        return result.isPresent() ? String.valueOf(result.getAsInt()) : "none";
    }

    @Override
    public String getName() {
        return "Majority Problem";
    }
}