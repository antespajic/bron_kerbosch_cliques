package hr.fer.projekt.clique.input;


import hr.fer.projekt.clique.input.exception.GraphLoaderException;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Singleton class which offers graph retrieval through textual
 * file containing graph definition.
 */
public class GraphLoader {

    /**
     * Supported extension for files with graph definitions.
     */
    public static final String SUPPORTED_EXTENSION = "txt";

    /**
     * Singleton GraphLoader object.
     */
    private static GraphLoader instance;

    /**
     * Private constructor ensures that only one instance of
     * GraphLoader object exists and is utilized.
     */
    private GraphLoader() {
    }

    /**
     * Returns singleton GraphLoader object.
     *
     * @return GraphLoader singleton
     */
    public static GraphLoader getGraphLoader() {
        if (instance == null) {
            instance = new GraphLoader();
        }
        return instance;
    }

    /**
     * Loads undirected string graph - graph whose vertices are String
     * objects and connections between two vertices DefaultEdges. Graph
     * definition is given through textual file at the given path. Textual
     * file with graph definition needs to conform to following rules:
     * <p>
     * Comments start with '##' and are ignored.
     * <p>
     * Empty lines are ignored.
     * <p>
     * Vertices definition starts with single line and '%Vertices%' declaration.
     * Vertices definition can span through one or more lines after declaration
     * line and vertex names are separated by one or more space characters.
     * <p>
     * Connections definition starts with single line and '%Connections%' declaration.
     * Connections definition can span through one or more lines after declaration
     * line and connection declarations are separated by one or more space
     * characters. Connection declaration needs to be in format:
     * '<origin-vertex-name>-<destination-vertex-name>'
     * <p>
     * Graph definition needs to conform to order of defining vertices first,
     * and connections after vertices. Lines which do not conform to specified
     * format will be considered malformed and graph will not be loaded.
     *
     * @param path path to textual file with graph definition
     * @return undirected String graph
     * @throws GraphLoaderException If path given is null, does not lead
     *                              to file or is of unsupported type. Furthermore, exception is thrown
     *                              if file has malformed structure which does not comply to
     *                              aforementioned rules.
     */
    public UndirectedGraph<String, DefaultEdge> loadStringGraph(Path path) {

        if (path == null) {
            throw new GraphLoaderException("Path given is null.");
        }

        if (!Files.isRegularFile(path)) {
            throw new GraphLoaderException("Path does not lead to file.");
        }

        String fileName = path.getFileName().toString();
        String fileExtension = Utility.getFileExtension(fileName);

        if (fileExtension == null || !fileExtension.equals(SUPPORTED_EXTENSION)) {
            throw new GraphLoaderException("Unsupported file type.");
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(path);
        } catch (IOException exception) {
            throw new GraphLoaderException("Exception occurred during graph definition read.", exception);
        }

        return parseFile(lines);
    }

    /**
     * Performs actual data parsing on textual file with graph
     * definition. Textual file needs to conform to rules defined
     * in calling method.
     *
     * @param lines lines of textual file with graph definition
     * @return undirected String graph
     * @throws GraphLoaderException If graph definition file is malformed.
     */
    private UndirectedGraph<String, DefaultEdge> parseFile(List<String> lines) {

        UndirectedGraph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        int state = 0;

        for (int i = 0, j = lines.size(); i < j; i++) {

            // Comments and empty lines should be discarded.
            if (lines.get(i).isEmpty() || lines.get(i).startsWith("##")) {
                continue;
            }
            // Signals start of vertices definition.
            else if (state == 0 && lines.get(i).equals("%Vertices%")) {
                state = 1;
            }
            // Signals start of connection definitions.
            else if (state == 2 && lines.get(i).equals("%Connections%")) {
                state = 3;
            }
            // Vertices definitions.
            else if (state == 1 || state == 2) {
                if (state == 1) {
                    state = 2;
                }
                String vertexDef = lines.get(i);
                String[] crumbs = vertexDef.trim().split("\\s+");
                for (String crumb : crumbs) {
                    graph.addVertex(crumb);
                }
            }
            // Connection definitions.
            else if (state == 3 || state == 4) {
                if (state == 3) {
                    state = 4;
                }
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

        /*
         * After successful graph load, we should end up in 4th state which
         * signals that edge definitions, connections, were given.
         * We throw appropriate exception otherwise.
         */
        if (state != 4) {
            throw new GraphLoaderException("Graph definition file malformed.");
        }

        return graph;
    }
}
