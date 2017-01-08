package hr.fer.projekt.clique.output.implementation;

import hr.fer.projekt.clique.output.OutputEnvironment;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Implementation of {@link OutputEnvironment} that outputs
 * algorithm steps to {@link JTextArea} of abstract GUI element
 * and stores traversal results in memory.
 */
public class GUIOutputEnvironment implements OutputEnvironment<String> {

    /**
     * Maximal cliques found during graph traversal.
     */
    private Collection<Set<String>> maximalCliques = Collections.emptyList();

    /**
     * Maximum cliques found during graph traversal.
     */
    private Collection<Set<String>> maximumCliques = Collections.emptyList();

    /**
     * {@link JTextArea} to which algorithm steps will be outputted.
     */
    private JTextArea jta;

    /**
     * Public constructor receives {@link JTextArea} to which
     * algorithm steps will be outputted.
     *
     * @param jta output {@link JTextArea}
     */
    public GUIOutputEnvironment(JTextArea jta) {
        if (jta == null) {
            throw new IllegalArgumentException("Text area given can not be null.");
        }
        this.jta = jta;
    }

    @Override
    public void outputStep(String step) {
        jta.append(step);
    }

    @Override
    public Collection<Set<String>> getMaximalCliques() {
        return Collections.unmodifiableCollection(maximalCliques);
    }

    @Override
    public void setMaximalCliques(Collection<Set<String>> maximalCliques) {
        this.maximalCliques = new ArrayList<>(maximalCliques);
    }

    @Override
    public Collection<Set<String>> getMaximumCliques() {
        return Collections.unmodifiableCollection(maximumCliques);
    }

    @Override
    public void setMaximumCliques(Collection<Set<String>> maximumCliques) {
        this.maximumCliques = new ArrayList<>(maximumCliques);
    }
}
