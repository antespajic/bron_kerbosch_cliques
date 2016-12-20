package hr.fer.projekt.clique;

import java.util.Collection;
import java.util.Set;

public interface OutputEnvironment<V> {

    void outputStep(String step);

    Collection<Set<V>> getMaximalCliques();

    void setMaximalCliques(Collection<Set<V>> maximalCliques);

    Collection<Set<V>> getMaximumCliques();

    void setMaximumCliques(Collection<Set<V>> maximumCliques);
}
