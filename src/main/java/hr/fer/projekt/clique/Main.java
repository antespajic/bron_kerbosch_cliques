package hr.fer.projekt.clique;

import hr.fer.projekt.clique.input.GraphLoader;
import org.jgrapht.Graph;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class Main {

    public static void main(String[] args) {

        UndirectedGraph<String, DefaultEdge> testGraph = createStringGraph();
        OutputEnvironment<String> outputEnvironment = StandardOutputEnvironment.getInstance();

        GraphLoader graphLoader = GraphLoader.getGraphLoader();

        Graph<String, DefaultEdge> testGraph02 = graphLoader.loadStringGraph("./examples/graph01.txt");

        BronKerbosch<String, DefaultEdge> bronKerbosch = new BronKerbosch<>(
                testGraph02,
                true,
                false,
                outputEnvironment);

        bronKerbosch.performTraversal();

        System.out.println("Result:");
        System.out.println(outputEnvironment.getMaximumCliques());
    }

    private static UndirectedGraph<String, DefaultEdge> createStringGraph() {
        UndirectedGraph<String, DefaultEdge> g =
                new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);

        String v1 = "v1";
        String v2 = "v2";
        String v3 = "v3";
        String v4 = "v4";
        String v5 = "v5";
        String v6 = "v6";

        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);
        g.addVertex(v5);
        g.addVertex(v6);

        g.addEdge(v6, v4);
        g.addEdge(v4, v5);
        g.addEdge(v4, v3);
        g.addEdge(v2, v3);
        g.addEdge(v2, v5);
        g.addEdge(v1, v5);
        g.addEdge(v1, v2);

        return g;
    }
}
