package hr.fer.projekt.clique;

import org.jgrapht.Graph;

import java.util.*;

public class BronKerbosch<V, E> {

    /** Graph. */
    private final Graph<V, E> graph;

    /** All maximal cliques found for given graph. */
    private Collection<Set<V>> cliques;

    /**
     * Specifies whether degeneracy ordering should be utilized at
     * the outermost level of recursion.
     */
    private boolean degeneracyOrdering;

    /**
     * Specifies whether pivot vertex should be calculated and
     * pivot environment utilized in recursive calls.
     */
    private boolean pivotEnvironment;

    public BronKerbosch(Graph<V, E> graph, boolean degeneracyOrdering, boolean pivotEnvironment) {
        if(graph == null) {
            throw new IllegalArgumentException("Graph passed can not be null.");
        }
        this.graph = graph;
        this.degeneracyOrdering = degeneracyOrdering;
        this.pivotEnvironment = pivotEnvironment;
    }

    public Collection<Set<V>> getAllMaximalCliques() {
        cliques = new ArrayList<>();
        List<V> potentialClique = new ArrayList<>();
        List<V> vertexCandidates = new ArrayList<>();
        List<V> vertexFound = new ArrayList<>();
        vertexCandidates.addAll(graph.vertexSet());
        findCliques(potentialClique, vertexCandidates, vertexFound);
        return cliques;
    }

    public Collection<Set<V>> getBiggestMaximalCliques() {
        getAllMaximalCliques();
        int maximum = 0;
        Collection<Set<V>> biggestCliques = new ArrayList<>();
        for (Set<V> clique : cliques) {
            if (maximum < clique.size()) {
                maximum = clique.size();
            }
        }
        for (Set<V> clique : cliques) {
            if (maximum == clique.size()) {
                biggestCliques.add(clique);
            }
        }
        return biggestCliques;
    }

    private void findCliques(List<V> potentialClique, List<V> vertexCandidates, List<V> vertexFound) {
        List<V> candidates = new ArrayList<>(vertexCandidates);
        if (!end(vertexCandidates, vertexFound)) {
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
                for (V new_found : vertexFound) {
                    if (graph.containsEdge(candidate, new_found)) {
                        newVertexFound.add(new_found);
                    }
                }

                // Condition for maximal clique:
                // If collection containing vertex candidates and vertexes
                // which were already found are both empty, potential clique
                // is indeed maximal clique.
                if (newVertexCandidates.isEmpty() && newVertexFound.isEmpty()) {
                    cliques.add(new HashSet<>(potentialClique));
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

    private boolean end(List<V> vertexCandidates, List<V> vertexFound) {
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
    private Collection<V> pivotEnvironment(Collection<V> vertexCandidates, Collection<V> pivotCandidates) {

        // Safety check.
        if (pivotCandidates.isEmpty()) {
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

        List<V> pivotEnvironment = new ArrayList<V>();
        for (V candidate : vertexCandidates) {
            if (graph.containsEdge(pivotCandidate, candidate)) {
                pivotEnvironment.add(candidate);
            }
        }
        return pivotEnvironment;
    }

    private Collection<V> getDegeneracyOrdering() {

        Collection<V> vertices = new HashSet<V>(graph.vertexSet());
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
}
