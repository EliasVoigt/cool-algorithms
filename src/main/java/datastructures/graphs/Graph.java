package main.java.datastructures.graphs;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record Graph<T>(String name, boolean directed, Set<Node<T>> nodes, Set<Edge<T>> edges) {

    /**
     * Renders one line per edge, e.g. {@code from -> to [weight]}.
     */
    @Override
    public String toString() {
        String arrow = directed ? "->" : "--";
        return edges.stream()
                .map(e -> e.from() + arrow + e.to() + e.weight().map(w -> " [" + w + "]").orElse(""))
                .collect(Collectors.joining("\n"));
    }


    /**
     * Builder for {@link Graph}. Adding an edge also adds its endpoint nodes.
     */
    public static class Builder<T> {
        private String name = "Graph";
        private boolean directed = false;
        private final Set<Node<T>> nodes = new HashSet<>();
        private final Set<Edge<T>> edges = new HashSet<>();

        /**
         * Adds a node.
         */
        public Builder<T> addNode(Node<T> node) {
            nodes.add(node);
            return this;
        }

        /**
         * Adds multiple nodes.
         */
        @SafeVarargs
        public final Builder<T> addNodes(Node<T>... nodes) {
            for (Node<T> n : nodes) addNode(n);
            return this;
        }

        /**
         * Adds an edge and its endpoint nodes.
         */
        public Builder<T> addEdge(Edge<T> edge) {
            edges.add(edge);
            nodes.add(edge.from());
            nodes.add(edge.to());
            return this;
        }

        /**
         * Adds multiple edges and their endpoint nodes.
         */
        @SafeVarargs
        public final Builder<T> addEdges(Edge<T>... edges) {
            for (Edge<T> e : edges) addEdge(e);
            return this;
        }

        /**
         * Sets the graph's name. Defaults to {@code "Graph"}.
         */
        public Builder<T> name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets whether the graph is directed. Defaults to {@code false}.
         */
        public Builder<T> directed(boolean directed) {
            this.directed = directed;
            return this;
        }

        /**
         * Builds the {@link Graph}.
         */
        public Graph<T> build() {
            return new Graph<>(name, directed, nodes, edges);
        }
    }
}