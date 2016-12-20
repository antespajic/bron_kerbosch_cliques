package hr.fer.projekt.clique.input.exception;

public class GraphLoaderException extends RuntimeException {

    public GraphLoaderException(String message) {
        super(message);
    }

    public GraphLoaderException(String message, Exception exception) {
        super(message, exception);
    }
}
