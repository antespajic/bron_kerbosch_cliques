package hr.fer.projekt.clique.utility;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Utility class with common methods utilized during
 * graph traversal.
 */
public class GraphTraversalUtility {

    /**
     * Calculates degeneracy ordering for given graph and returns
     * one of, possibly multiple, solutions.
     *
     * @param graph graphs whose degeneracy ordering is required
     * @param <V>   vertex type parameter
     * @param <E>   edge type parameter
     * @return one of graph's degeneracy orderings
     * @throws IllegalArgumentException if graph given is null value
     */
    public static <V, E> Collection<V> getDegeneracyOrdering(Graph<V, E> graph) {

        if (graph == null) {
            throw new IllegalArgumentException("Graph given is null.");
        }

        Collection<V> vertices = new HashSet<>(graph.vertexSet());
        Collection<V> degeneracyOrdering = new ArrayList<>();

        while (!vertices.isEmpty()) {

            // Finding current degeneracy.
            int d = Integer.MAX_VALUE;
            for (V vertex : vertices) {
                int currentEdges = graph.edgesOf(vertex).size();
                if (currentEdges < d) {
                    d = currentEdges;
                }
            }

            V candidate = null;
            for (V vertex : vertices) {
                int currentEdges = graph.edgesOf(vertex).size();
                if (currentEdges == d) {
                    candidate = vertex;
                    break;
                }
            }

            vertices.remove(candidate);
            degeneracyOrdering.add(candidate);
        }

        return degeneracyOrdering;
    }

    /**
     * For given vertex finds neighbouring vertices. Considered
     * neighbouring vertices are pulled from collection of candidates
     * provided.
     *
     * @param graph      graph containing said vertices
     * @param vertex     vertex for which neighbouring vertices are calculated
     * @param candidates collection of considered neighbouring vertices for given vertex
     * @param <V>        vertex type parameter
     * @param <E>        edge type parameter
     * @return collection of neighbouring vertices
     * @throws IllegalArgumentException if any of passed values is null value
     */
    public static <V, E> Collection<V> getNeighbouringVertices(Graph<V, E> graph, V vertex, Collection<V> candidates) {

        if (graph == null) {
            throw new IllegalArgumentException("Graph given is null.");
        } else if (vertex == null) {
            throw new IllegalArgumentException("Vertex given is null.");
        } else if (candidates == null) {
            throw new IllegalArgumentException("Collection of candidates given is null.");
        }

        List<V> neighbouringVertices = new ArrayList<>();
        for (V candidate : candidates) {
            if (graph.containsEdge(vertex, candidate)) {
                neighbouringVertices.add(candidate);
            }
        }
        return neighbouringVertices;
    }
}
