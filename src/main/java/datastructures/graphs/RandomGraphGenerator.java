package main.java.datastructures.graphs;

import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Builds random {@link Graph}s. Configure via the fluent setters, then call {@link #generate}.
 */
public class RandomGraphGenerator<T> {
    private int numNodes;
    private int numEdges;
    private double edgeProbability = 0.1;
    private int numComponents = 1;
    private boolean directed = false;
    private boolean weighted = false;
    private int maxNumNodes;
    private int minNumNodes;
    private int maxNumEdges;
    private int minNumEdges;
    private double maxWeight = 1.0;
    private double minWeight = 1.0;
    private String name = "Random Graph";
    private Long seed;

    /**
     * Creates a new builder.
     **/
    public static <T> RandomGraphGenerator<T> builder() {
        return new RandomGraphGenerator<>();
    }

    /**
     * Sets an exact node count. Overrides {@link #nodeRange}.
     */
    public RandomGraphGenerator<T> numNodes(int numNodes) {
        this.numNodes = numNodes;
        return this;
    }

    /**
     * Sets an exact edge count. Takes priority over {@link #edgeRange} and {@link #edgeProbability}.
     */
    public RandomGraphGenerator<T> numEdges(int numEdges) {
        this.numEdges = numEdges;
        return this;
    }

    /**
     * Probability of keeping each candidate edge. Used only if {@link #numEdges}/{@link #edgeRange} aren't set. Default 0.1.
     */
    public RandomGraphGenerator<T> edgeProbability(double edgeProbability) {
        this.edgeProbability = edgeProbability;
        return this;
    }

    /**
     * Number of disconnected components to split the nodes into. Default 1.
     */
    public RandomGraphGenerator<T> numComponents(int numComponents) {
        this.numComponents = numComponents;
        return this;
    }

    /**
     * Sets whether generated edges are directed. Default {@code false}.
     */
    public RandomGraphGenerator<T> directed(boolean directed) {
        this.directed = directed;
        return this;
    }


    /**
     * Enables random edge weights within {@code [minWeight, maxWeight]}.
     */
    public RandomGraphGenerator<T> weighted(double minWeight, double maxWeight) {
        this.weighted = true;
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
        return this;
    }

    /**
     * Sets a random node count range, used if {@link #numNodes} isn't set.
     */
    public RandomGraphGenerator<T> nodeRange(int minNumNodes, int maxNumNodes) {
        this.minNumNodes = minNumNodes;
        this.maxNumNodes = maxNumNodes;
        return this;
    }

    /**
     * Sets a random edge count range. Used only if {@link #numEdges} isn't set.
     */
    public RandomGraphGenerator<T> edgeRange(int minNumEdges, int maxNumEdges) {
        this.minNumEdges = minNumEdges;
        this.maxNumEdges = maxNumEdges;
        return this;
    }

    /**
     * Sets the graph's display name. Default {@code "Random Graph"}.
     */
    public RandomGraphGenerator<T> name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the RNG seed for reproducible generation.
     */
    public RandomGraphGenerator<T> seed(long seed) {
        this.seed = seed;
        return this;
    }

    /**
     * Convenience for the common case: nodes valued 0..n-1.
     */
    public Graph<Integer> generate() {
        @SuppressWarnings("unchecked")
        RandomGraphGenerator<Integer> self = (RandomGraphGenerator<Integer>) this;
        return self.generate(i -> i);
    }

    /**
     * Generates a random {@link Graph} from the current settings. Guarantees each component is
     * connected (via a random spanning tree), then adds extra edges per {@link #numEdges},
     * {@link #edgeRange}, or {@link #edgeProbability} (checked in that priority order).
     *
     * @param nodeValueFactory produces each node's value from its index (0..n-1)
     */
    public Graph<T> generate(IntFunction<T> nodeValueFactory) {
        validate();
        Random rng = seed != null ? new Random(seed) : new Random();

        int resolvedNumNodes = resolveNumNodes(rng);
        if (numComponents < 1 || numComponents > resolvedNumNodes) {
            throw new IllegalArgumentException("numComponents must be between 1 and numNodes");
        }

        List<Node<T>> allNodes = IntStream.range(0, resolvedNumNodes)
                .mapToObj(i -> new Node<>(nodeValueFactory.apply(i)))
                .collect(Collectors.toList());
        Collections.shuffle(allNodes, rng);

        List<List<Node<T>>> components = partitionIntoComponents(allNodes, numComponents);

        // 1. Mandatory spanning-tree edges (guarantee connectivity + component count)
        Set<List<Node<T>>> spanningPairs = new HashSet<>();
        List<Edge<T>> spanningEdges = new ArrayList<>();
        for (List<Node<T>> component : components) {
            for (int i = 1; i < component.size(); i++) {
                Node<T> from = component.get(rng.nextInt(i));
                Node<T> to = component.get(i);
                spanningEdges.add(buildEdge(from, to, rng));
                spanningPairs.add(List.of(from, to));
            }
        }

        // 2. Remaining candidate edges (all other pairs within each component)
        List<Edge<T>> candidateEdges = new ArrayList<>();
        for (List<Node<T>> component : components) {
            for (int i = 0; i < component.size(); i++) {
                for (int j = i + 1; j < component.size(); j++) {
                    Node<T> a = component.get(i), b = component.get(j);
                    if (!spanningPairs.contains(List.of(a, b))) {
                        candidateEdges.add(buildEdge(a, b, rng));
                    }
                }
            }
        }

        Graph.Builder<T> builder = new Graph.Builder<T>()
                .name(name)
                .directed(directed);
        spanningEdges.forEach(builder::addEdge);

        // 3. Pick extra edges: numEdges > edgeRange > edgeProbability
        if (numEdges > 0) {
            addExactCount(builder, candidateEdges, numEdges - spanningEdges.size(), rng, "numEdges");
        } else if (minNumEdges > 0 && maxNumEdges > 0) {
            int target = minNumEdges + rng.nextInt(maxNumEdges - minNumEdges + 1);
            addExactCount(builder, candidateEdges, target - spanningEdges.size(), rng, "edgeRange");
        } else {
            for (Edge<T> candidate : candidateEdges) {
                if (rng.nextDouble() < edgeProbability) {
                    builder.addEdge(candidate);
                }
            }
        }

        return builder.build();
    }

    /**
     * Adds exactly {@code count} random edges from {@code candidates} to {@code builder}.
     */
    private void addExactCount(
            Graph.Builder<T> builder, List<Edge<T>> candidates, int count, Random rng, String label) {
        if (count < 0) {
            throw new IllegalArgumentException(label + " is smaller than the mandatory spanning-tree edge count");
        }
        if (count > candidates.size()) {
            throw new IllegalArgumentException(
                    label + " requires " + count + " extra edges, but only " + candidates.size() + " are possible");
        }
        Collections.shuffle(candidates, rng);
        for (int i = 0; i < count; i++) {
            builder.addEdge(candidates.get(i));
        }
    }

    /**
     * Splits {@code allNodes} round-robin into {@code numComponents} groups.
     */
    private List<List<Node<T>>> partitionIntoComponents(List<Node<T>> allNodes, int numComponents) {
        List<List<Node<T>>> components = new ArrayList<>();
        for (int i = 0; i < numComponents; i++) components.add(new ArrayList<>());
        for (int i = 0; i < allNodes.size(); i++) {
            components.get(i % numComponents).add(allNodes.get(i));
        }
        return components;
    }

    /**
     * Resolves the node count from {@link #numNodes} or {@link #nodeRange}.
     */
    private int resolveNumNodes(Random rng) {
        if (numNodes > 0) return numNodes;
        if (minNumNodes > 0 && maxNumNodes > 0) {
            return minNumNodes + rng.nextInt(maxNumNodes - minNumNodes + 1);
        }
        throw new IllegalArgumentException("Must specify either numNodes or nodeRange");
    }

    /**
     * Builds an edge between two nodes, with a random weight if {@link #weighted} is set.
     */
    private Edge<T> buildEdge(Node<T> from, Node<T> to, Random rng) {
        Optional<Double> weight = weighted
                ? Optional.of(minWeight + rng.nextDouble() * (maxWeight - minWeight))
                : Optional.empty();
        return new Edge<>(from, to, weight);
    }

    /**
     * Validates that the current settings are internally consistent.
     */
    private void validate() {
        if (minNumNodes > 0 && maxNumNodes > 0 && minNumNodes > maxNumNodes) {
            throw new IllegalArgumentException(
                    "minNumNodes (" + minNumNodes + ") > maxNumNodes (" + maxNumNodes + ")");
        }
        if (numNodes > 0 && minNumNodes > 0 && numNodes < minNumNodes) {
            throw new IllegalArgumentException(
                    "numNodes (" + numNodes + ") < minNumNodes (" + minNumNodes + ")");
        }
        if (numNodes > 0 && maxNumNodes > 0 && numNodes > maxNumNodes) {
            throw new IllegalArgumentException(
                    "numNodes (" + numNodes + ") > maxNumNodes (" + maxNumNodes + ")");
        }

        int effectiveMinNodes = numNodes > 0 ? numNodes : minNumNodes;
        if (effectiveMinNodes > 0 && numComponents > effectiveMinNodes) {
            throw new IllegalArgumentException(
                    "numComponents (" + numComponents + ") > available nodes (" + effectiveMinNodes + ")");
        }

        if (edgeProbability < 0.0 || edgeProbability > 1.0) {
            throw new IllegalArgumentException(
                    "edgeProbability must be in [0,1], was " + edgeProbability);
        }

        if (weighted && minWeight > maxWeight) {
            throw new IllegalArgumentException(
                    "minWeight (" + minWeight + ") > maxWeight (" + maxWeight + ")");
        }

        if (minNumEdges > 0 && maxNumEdges > 0 && minNumEdges > maxNumEdges) {
            throw new IllegalArgumentException(
                    "minNumEdges (" + minNumEdges + ") > maxNumEdges (" + maxNumEdges + ")");
        }
        if (numEdges > 0 && minNumEdges > 0 && numEdges < minNumEdges) {
            throw new IllegalArgumentException(
                    "numEdges (" + numEdges + ") < minNumEdges (" + minNumEdges + ")");
        }
        if (numEdges > 0 && maxNumEdges > 0 && numEdges > maxNumEdges) {
            throw new IllegalArgumentException(
                    "numEdges (" + numEdges + ") > maxNumEdges (" + maxNumEdges + ")");
        }
    }
}