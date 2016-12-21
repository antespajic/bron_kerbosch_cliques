package hr.fer.projekt.clique.output.implementation;

import hr.fer.projekt.clique.output.OutputEnvironment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Implementation of {@link OutputEnvironment} that outputs
 * algorithm steps to standard output and stores traversal
 * results in memory.
 *
 * @param <V> variable type of vertices in graph
 */
public class StandardOutputEnvironment<V> implements OutputEnvironment<V> {

    /**
     * Singleton StandardOutputEnvironment object.
     */
    private static StandardOutputEnvironment instance;

    /**
     * Maximal cliques found during graph traversal.
     */
    private Collection<Set<V>> maximalCliques = Collections.emptyList();

    /**
     * Maximum cliques found during graph traversal.
     */
    private Collection<Set<V>> maximumCliques = Collections.emptyList();

    /**
     * Private constructor ensures that only one instance of
     * StandardOutputEnvironment object exists and is utilized.
     */
    private StandardOutputEnvironment() {
    }

    /**
     * Returns singleton StandardOutputEnvironment object.
     *
     * @return StandardOutputEnvironment singleton
     */
    public static <V> StandardOutputEnvironment getInstance() {
        if (instance == null) {
            instance = new StandardOutputEnvironment<>();
        }
        return instance;
    }

    @Override
    public void outputStep(String step) {
        System.out.println(step);
    }

    @Override
    public Collection<Set<V>> getMaximalCliques() {
        return Collections.unmodifiableCollection(maximalCliques);
    }

    @Override
    public void setMaximalCliques(Collection<Set<V>> maximalCliques) {
        this.maximalCliques = new ArrayList<>(maximalCliques);
    }

    @Override
    public Collection<Set<V>> getMaximumCliques() {
        return Collections.unmodifiableCollection(maximumCliques);
    }

    @Override
    public void setMaximumCliques(Collection<Set<V>> maximumCliques) {
        this.maximumCliques = new ArrayList<>(maximumCliques);
    }
}
