package hr.fer.projekt.clique;

import hr.fer.projekt.clique.input.GraphLoader;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

public class Application {

    public static void main(String[] args) {

        OutputEnvironment<String> outputEnvironment = StandardOutputEnvironment.getInstance();

        GraphLoader graphLoader = GraphLoader.getGraphLoader();

        Graph<String, DefaultEdge> testGraph = graphLoader.loadStringGraph("./examples/graph01.txt");

        BronKerbosch<String, DefaultEdge> bronKerbosch = new BronKerbosch<>(
                testGraph,
                true,
                true,
                outputEnvironment);

        bronKerbosch.performTraversal();

        System.out.println("Result:");
        System.out.println(outputEnvironment.getMaximumCliques());
    }
}
