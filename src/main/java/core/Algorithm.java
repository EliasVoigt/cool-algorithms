package main.java.core;

import java.util.function.Function;

/**
 * Defines a generic algorithm that transforms an input of type {@code I}
 * into a result of type {@code R}.
 *
 * @param <I> the input type
 * @param <R> the result type
 */
public abstract class Algorithm<I, R> implements Function<I, R> {

    /**
     * Applies the algorithm to the given input.
     *
     * @param input the input to process
     * @return the result produced by the algorithm
     */
    @Override
    public abstract R apply(I input);

    /**
     * Returns the name of this algorithm.
     *
     * @return the algorithm name
     */
    public abstract String toString();
}