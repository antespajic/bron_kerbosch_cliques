package hr.fer.projekt.clique;

import hr.fer.projekt.clique.algorithm.BronKerbosch;
import hr.fer.projekt.clique.input.GraphLoader;
import hr.fer.projekt.clique.output.OutputEnvironment;
import hr.fer.projekt.clique.output.implementation.StandardOutputEnvironment;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Command line application which calculates maximal and maximum cliques
 * for undirected String graph provided through textual file using
 * Bron-Kerbosch algorithm, while outputting algorithm steps. Application
 * supports variations of original algorithm - utilization of degeneracy
 * ordering and utilization of pivot environment, which can be set up through
 * flags provided at application startup.
 */
public class Application {

    /**
     * Entry point of a program. Three command line arguments are expected:
     * 1. path to textual file with graph definition
     * 2. 'true' or 'false' - utilization of degeneracy ordering
     * 3. 'true' or 'false' - utilization of pivot environment
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {

        if (args.length != 3) {
            System.err.println("Three command line arguments expected. ");
            System.exit(1);
        }

        Path path = Paths.get(args[0]);
        boolean utilizeDegeneracyOrdering = Boolean.parseBoolean(args[1]);
        boolean utilizePivotEnvironment = Boolean.parseBoolean(args[2]);

        OutputEnvironment<String> outputEnvironment = StandardOutputEnvironment.getInstance();

        GraphLoader graphLoader = GraphLoader.getGraphLoader();
        Graph<String, DefaultEdge> graph = graphLoader.loadStringGraph(path);

        BronKerbosch<String, DefaultEdge> bronKerbosch = new BronKerbosch<>(
                graph,
                utilizeDegeneracyOrdering,
                utilizePivotEnvironment,
                outputEnvironment);

        bronKerbosch.performTraversal();
    }
}
