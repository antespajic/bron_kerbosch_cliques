package hr.fer.projekt.clique;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class StandardOutputEnvironment<V> implements OutputEnvironment<V> {

    private static StandardOutputEnvironment instance;
    private Collection<Set<V>> maximalCliques = Collections.emptyList();
    private Collection<Set<V>> maximumCliques = Collections.emptyList();

    private StandardOutputEnvironment() {
    }

    public static <V> StandardOutputEnvironment getInstance() {
        if(instance == null) {
            instance = new StandardOutputEnvironment();
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
