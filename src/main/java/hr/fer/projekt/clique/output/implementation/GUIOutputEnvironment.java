package hr.fer.projekt.clique.output.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import javax.swing.JTextArea;

import hr.fer.projekt.clique.output.OutputEnvironment;

public class GUIOutputEnvironment implements OutputEnvironment<String> {

	/**
	 * Singleton StandardOutputEnvironment object.
	 */
	private static GUIOutputEnvironment instance;

	/**
	 * Maximal cliques found during graph traversal.
	 */
	private Collection<Set<String>> maximalCliques = Collections.emptyList();

	/**
	 * Maximum cliques found during graph traversal.
	 */
	private Collection<Set<String>> maximumCliques = Collections.emptyList();

	private JTextArea jta;

	private GUIOutputEnvironment(JTextArea jta) {
		this.jta = jta;
	}

	public static GUIOutputEnvironment getInstance(JTextArea outputArea) {
		if (instance == null) {
			instance = new GUIOutputEnvironment(outputArea);
		}
		return instance;
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
