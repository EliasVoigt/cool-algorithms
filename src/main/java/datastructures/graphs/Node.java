package main.java.datastructures.graphs;

public record Node<T>(T value) {

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
