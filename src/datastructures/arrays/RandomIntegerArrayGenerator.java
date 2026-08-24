package datastructures.arrays;

import java.util.Arrays;
import java.util.Random;

/**
 * Generates random {@code int[]} arrays.
 *
 * <p>Configure the generator using the fluent methods and call
 * {@link #generateSample()} to generate an array.</p>
 *
 * <p>Standalone utility — not tied to {@code Problem} or {@code Benchmark}.
 * Compose it into a {@code Problem} implementation that needs randomly
 * generated {@code int[]} input.</p>
 */
public class RandomIntegerArrayGenerator {

    private final Random random = new Random();
    private int length = -1;
    private int minLength = -1;
    private int maxLength = -1;
    private int minValue = 0;
    private int maxValue = 100;

    public RandomIntegerArrayGenerator length(int length) {
        this.length = length;
        return this;
    }

    public RandomIntegerArrayGenerator lengthRange(int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        return this;
    }

    public RandomIntegerArrayGenerator valueRange(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        return this;
    }

    public RandomIntegerArrayGenerator seed(long seed) {
        random.setSeed(seed);
        return this;
    }

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
            throw new IllegalArgumentException("Must specify either length or lengthRange");
        }
        if (length >= 0 && minLength >= 0 && length < minLength) {
            throw new IllegalArgumentException("length must not be smaller than minLength");
        }
        if (length >= 0 && maxLength >= 0 && length > maxLength) {
            throw new IllegalArgumentException("length must not be larger than maxLength");
        }
        if (minLength >= 0 && maxLength >= 0 && minLength > maxLength) {
            throw new IllegalArgumentException("minLength must not be larger than maxLength");
        }
        if (minValue > maxValue) {
            throw new IllegalArgumentException("minValue must not be larger than maxValue");
        }
    }

    /**
     * Formats an input array as a string.
     *
     * @param input the array to format
     * @return the string representation of the array
     */
    public static String format(int[] input) {
        return Arrays.toString(input);
    }
}