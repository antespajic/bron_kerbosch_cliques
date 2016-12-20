package hr.fer.projekt.clique.input;


import hr.fer.projekt.clique.input.exception.GraphLoaderException;
import org.jgrapht.Graph;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GraphLoader {

    private static final String supportedExtension = "txt";
    private static GraphLoader instance;

    private GraphLoader() {
    }

    public static GraphLoader getGraphLoader() {
        if (instance == null) {
            instance = new GraphLoader();
        }
        return instance;
    }

    public Graph<String, DefaultEdge> loadStringGraph(String filePath) {

        Path path = Paths.get(filePath);
        if (!Files.isRegularFile(path)) {
            throw new GraphLoaderException("Path does not lead to file.");
        }

        String fileName = path.getFileName().toString();
        int dotIndex = fileName.indexOf(".");
        if (dotIndex == -1
                || fileName.length() == (dotIndex + 1)
                || !fileName.substring(dotIndex + 1, fileName.length()).equals(supportedExtension)) {
            throw new GraphLoaderException("Unsupported file type.");
        }

        List<String> lines = null;
        try {
            lines = Files.readAllLines(path);
        } catch (IOException exception) {
            throw new GraphLoaderException("Exception occurred during graph definition read.", exception);
        }

        return parseFile(lines);
    }

    private Graph<String, DefaultEdge> parseFile(List<String> lines) {

        UndirectedGraph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        int state = 0;

        for (int i = 0, j = lines.size(); i < j; i++) {

            // Signals start of vertices definition.
            if (state == 0 && lines.get(i).equals("%Vertices%")) {
                state = 1;
            }
            // Signals start of connection definitions.
            else if (state == 1 && lines.get(i).equals("%Connections%")) {
                state = 2;
            }
            // Signals comment which should be discarded.
            else if (lines.get(i).startsWith("##")) {
                continue;
            }
            // Vertices definitions.
            else if (state == 1) {
                String vertexDef = lines.get(i);
                String[] crumbs = vertexDef.trim().split("\\s+");
                for (String crumb : crumbs) {
                    graph.addVertex(crumb);
                }
            }
            // Connection definitions.
            else if (state == 2) {
                String connectionDef = lines.get(i);
                String[] crumbs = connectionDef.trim().split("\\s+");
                for (String crumb : crumbs) {
                    String[] def = crumb.split("-");
                    if (def.length != 2) {
                        throw new GraphLoaderException("Malformed connection definition.");
                    }
                    graph.addEdge(def[0], def[1]);
                }
            }
            // Unsupported graph definition file format.
            else {
                throw new GraphLoaderException("Graph definition file malformed.");
            }
        }

        /**
         * After successful graph load, we should end up in 2nd state which
         * signals that edge definitions, connections, were given or at least
         * declared. We throw appropriate exception otherwise.
         */
        if (state != 2) {
            throw new GraphLoaderException("Graph definition file malformed.");
        }

        return graph;
    }
}
