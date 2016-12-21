package hr.fer.projekt.clique;

import hr.fer.projekt.clique.utility.CollectionUtility;
import hr.fer.projekt.clique.utility.GraphTraversalUtility;
import org.jgrapht.Graph;

import java.util.*;

public class BronKerbosch<V, E> {

    /**
     * Graph traversed.
     */
    private final Graph<V, E> graph;

    /**
     * Maximal cliques found for given graph.
     */
    private Collection<Set<V>> maximalCliques;

    /**
     * Maximum cliques found for given graph.
     */
    private Collection<Set<V>> maximumCliques;

    /**
     * Specifies whether degeneracy ordering should be utilized at
     * the outermost level of recursion.
     */
    private boolean utilizeDegeneracyOrdering;

    /**
     * Specifies whether pivot vertex should be calculated and
     * pivot environment utilized in recursive calls.
     */
    private boolean utilizePivotEnvironment;

    /**
     * Output environment served during and after algorithm
     * computation.
     */
    private OutputEnvironment<V> outputEnvironment;

    public BronKerbosch(
            Graph<V, E> graph,
            boolean utilizeDegeneracyOrdering,
            boolean utilizePivotEnvironment,
            OutputEnvironment<V> outputEnvironment) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph passed can not be null.");
        } else if (outputEnvironment == null) {
            throw new IllegalArgumentException("Output environment passed can not be null.");
        }
        this.graph = graph;
        this.utilizeDegeneracyOrdering = utilizeDegeneracyOrdering;
        this.utilizePivotEnvironment = utilizePivotEnvironment;
        this.outputEnvironment = outputEnvironment;
    }

    public void performTraversal() {
        findMaximalCliques();
        findMaximumCliques();

        outputEnvironment.setMaximalCliques(maximalCliques);
        outputEnvironment.setMaximumCliques(maximumCliques);
    }

    private void findMaximalCliques() {

        maximalCliques = new ArrayList<>();

        List<V> potentialClique = new ArrayList<>();
        List<V> vertexCandidates = new ArrayList<>(graph.vertexSet());
        List<V> vertexFound = new ArrayList<>();

        if (utilizeDegeneracyOrdering) {
            degeneracyOrdering(potentialClique, vertexCandidates, vertexFound);
        } else {
            findCliques(potentialClique, vertexCandidates, vertexFound);
        }
    }

    private void findMaximumCliques() {
        maximumCliques = new ArrayList<>();
        int maximum = 0;
        for (Set<V> clique : maximalCliques) {
            if (maximum < clique.size()) {
                maximum = clique.size();
            }
        }
        for (Set<V> clique : maximalCliques) {
            if (maximum == clique.size()) {
                maximumCliques.add(clique);
            }
        }
    }

    private void degeneracyOrdering(
            Collection<V> potentialClique,
            Collection<V> vertexCandidates,
            Collection<V> vertexFound) {

        Collection<V> degeneracyOrdering = GraphTraversalUtility.getDegeneracyOrdering(graph);
        for (V vertex : degeneracyOrdering) {
            Collection<V> neighbours = GraphTraversalUtility.getNeighbouringVertices(graph, vertex, vertexCandidates);

            // Updating collections.
            List<V> newPotentialClique = new ArrayList<>(potentialClique);
            newPotentialClique.add(vertex);
            Collection<V> newVertexCandidates = CollectionUtility.intersection(vertexCandidates, neighbours);
            Collection<V> newVertexFound = CollectionUtility.intersection(vertexFound, neighbours);

            findCliques(newPotentialClique, newVertexCandidates, newVertexFound);

            vertexCandidates.remove(vertex);
            vertexFound.add(vertex);
        }
    }

    private void findCliques(Collection<V> potentialClique, Collection<V> vertexCandidates, Collection<V> vertexFound) {
        if (!end(vertexCandidates, vertexFound)) {

            Collection<V> candidates;
            if (utilizePivotEnvironment) {
                candidates = CollectionUtility.removeAll(vertexCandidates, pivotEnvironment(vertexCandidates, vertexFound));
            } else {
                candidates = new ArrayList<>(vertexCandidates);
            }

            for (V candidate : candidates) {

                // Collections needed for recursion call.
                List<V> newVertexCandidates = new ArrayList<>();
                List<V> newVertexFound = new ArrayList<>();

                // Move candidate node to potential clique.
                potentialClique.add(candidate);
                vertexCandidates.remove(candidate);

                // Creating new vertexCandidates collection by removing all vertexes
                // in present collection not connected to vertex candidate.
                for (V newCandidate : vertexCandidates) {
                    if (graph.containsEdge(candidate, newCandidate)) {
                        newVertexCandidates.add(newCandidate);
                    }
                }

                // Creating new vertexFound collection by removing all vertexes
                // in present collection not connected to vertex candidate.
                for (V newFound : vertexFound) {
                    if (graph.containsEdge(candidate, newFound)) {
                        newVertexFound.add(newFound);
                    }
                }

                // Condition for maximal clique:
                // If collection containing vertex candidates and vertexes
                // which were already found are both empty, potential clique
                // is indeed maximal clique.
                if (newVertexCandidates.isEmpty() && newVertexFound.isEmpty()) {
                    maximalCliques.add(new HashSet<>(potentialClique));
                } else {
                    findCliques(potentialClique, newVertexCandidates, newVertexFound);
                }

                // Moving vertex candidate from potential clique to collection
                // of vertexes which were already found.
                vertexFound.add(candidate);

                // Remove candidate node from potential clique.
                // This ensures that potential clique is not altered in recursion call.
                potentialClique.remove(candidate);
            }
        }
    }

    private boolean end(Collection<V> vertexCandidates, Collection<V> vertexFound) {
        boolean end = false;
        int edgeCounter;
        for (V found : vertexFound) {
            edgeCounter = 0;
            for (V candidate : vertexCandidates) {
                if (graph.containsEdge(found, candidate)) {
                    edgeCounter++;
                }
            }
            if (edgeCounter == vertexCandidates.size()) {
                end = true;
            }
        }
        return end;
    }

    // Strategy utilizing pivot environment.
    private Collection<V> pivotEnvironment(Collection<V> vertexCandidates, Collection<V> vertexFound) {
        Collection<V> pivotCandidates = CollectionUtility.union(vertexCandidates, vertexFound);

        if(pivotCandidates.isEmpty()) {
            return Collections.emptyList();
        }

        // Searching for pivot vertex - one with the most connections
        // to other pivot candidates.
        int edgeCounter = 0;
        V pivotCandidate = null;
        for (V candidate : pivotCandidates) {
            int currentEdges = graph.edgesOf(candidate).size();
            if (currentEdges > edgeCounter) {
                edgeCounter = currentEdges;
                pivotCandidate = candidate;
            }
        }

        return GraphTraversalUtility.getNeighbouringVertices(graph, pivotCandidate, vertexCandidates);
    }
}
