package datastructures;

/**
 * Defines a generator for creating sample inputs for an algorithm.
 *
 * @param <I> the type of input generated
 */
public abstract class InputGenerator<I> {

    /**
     * Generates a sample input.
     *
     * @return a generated input
     */
    public abstract I generateSample();

    /**
     * Formats an input as a human-readable string.
     *
     * @param input the input to format
     * @return the formatted representation of the input
     */
    public abstract String format(I input);
}