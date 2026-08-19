package datastructures.arrays;

import datastructures.InputGenerator;

import java.util.Arrays;
import java.util.Random;

/**
 * Generates random {@code int[]} arrays.
 *
 * <p>Configure the generator using the fluent methods and call
 * {@link #generateSample()} to generate an array.</p>
 */
public class RandomIntegerArrayGenerator extends InputGenerator<int[]> {

    private final Random random = new Random();
    private int length = -1;
    private int minLength = -1;
    private int maxLength = -1;
    private int minValue = 0;
    private int maxValue = 100;

    /**
     * Sets an exact array length.
     *
     * @param length the array length
     * @return this generator
     */
    public RandomIntegerArrayGenerator length(int length) {
        this.length = length;
        return this;
    }

    /**
     * Sets the range from which the array length is randomly selected.
     *
     * @param minLength minimum length, inclusive
     * @param maxLength maximum length, inclusive
     * @return this generator
     */
    public RandomIntegerArrayGenerator lengthRange(int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        return this;
    }

    /**
     * Sets the inclusive range of possible array values.
     *
     * @param minValue minimum value, inclusive
     * @param maxValue maximum value, inclusive
     * @return this generator
     */
    public RandomIntegerArrayGenerator valueRange(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        return this;
    }

    /**
     * Sets the random number generator seed.
     *
     * <p>Using the same seed produces the same sequence of generated arrays.</p>
     *
     * @param seed the random number generator seed
     * @return this generator
     */
    public RandomIntegerArrayGenerator seed(long seed) {
        random.setSeed(seed);
        return this;
    }

    /**
     * Generates a random {@code int[]} using the current configuration.
     *
     * @return a randomly generated array
     */
    @Override
    public int[] generateSample() {
        validate();

        int resolvedLength = resolveLength();

        int[] result = new int[resolvedLength];

        for (int i = 0; i < resolvedLength; i++) {
            result[i] = random.nextInt(minValue, maxValue + 1);
        }

        return result;
    }

    private int resolveLength() {
        if (length >= 0) {
            return length;
        }

        return random.nextInt(minLength, maxLength + 1);
    }

    private void validate() {
        if (length < 0 && (minLength < 0 || maxLength < 0)) {
            throw new IllegalArgumentException(
                    "Must specify either length or lengthRange");
        }

        if (length >= 0 && minLength >= 0 && length < minLength) {
            throw new IllegalArgumentException(
                    "length must not be smaller than minLength");
        }

        if (length >= 0 && maxLength >= 0 && length > maxLength) {
            throw new IllegalArgumentException(
                    "length must not be larger than maxLength");
        }

        if (minLength >= 0 && maxLength >= 0 && minLength > maxLength) {
            throw new IllegalArgumentException(
                    "minLength must not be larger than maxLength");
        }

        if (minValue > maxValue) {
            throw new IllegalArgumentException(
                    "minValue must not be larger than maxValue");
        }
    }

    /**
     * Formats an input array as a string.
     *
     * @param input the array to format
     * @return the string representation of the array
     */
    @Override
    public String format(int[] input) {
        return Arrays.toString(input);
    }
}