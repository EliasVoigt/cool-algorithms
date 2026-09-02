package main.java.datastructures.graphs;

import java.util.Optional;

public record Edge<T>(Node<T> from, Node<T> to, Optional<Double> weight) {

    public Edge(Node<T> from, Node<T> to) {
        this(from, to, Optional.empty());
    }

    public Edge(Node<T> from, Node<T> to, double weight) {
        this(from, to, Optional.of(weight));
    }

    public double getWeightOrDefault(double defaultWeight) {
        return weight.orElse(defaultWeight);
    }
}