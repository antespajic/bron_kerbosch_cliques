package hr.fer.projekt.clique.output;

import java.util.Collection;
import java.util.Set;

/**
 * Defines abstract output environment used during graph traversal
 * with different algorithms. Environment offers output of algorithm
 * steps, as well as result storage and retrieval.
 *
 * @param <V> variable type of vertices in graph
 */
public interface OutputEnvironment<V> {

    /**
     * Outputs detailed description of algorithm step,
     * during graph traversal, to OutputEnvironment.
     * Used for educative purposes and identifying
     * differences between algorithm nuances.
     *
     * @param step description of algorithm step
     * @throws IllegalArgumentException if value passed is null
     */
    void outputStep(String step);

    /**
     * Maximal cliques found during graph traversal.
     *
     * @return maximal cliques
     */
    Collection<Set<V>> getMaximalCliques();

    /**
     * Sets maximal cliques found during graph traversal.
     *
     * @param maximalCliques maximal cliques found
     * @throws IllegalArgumentException if value passed is null
     */
    void setMaximalCliques(Collection<Set<V>> maximalCliques);

    /**
     * Maximum cliques found during graph traversal.
     *
     * @return maximum cliques
     */
    Collection<Set<V>> getMaximumCliques();

    /**
     * Sets maximum cliques found during graph traversal.
     *
     * @param maximumCliques maximum cliques found
     * @throws IllegalArgumentException if value passed is null
     */
    void setMaximumCliques(Collection<Set<V>> maximumCliques);
}
