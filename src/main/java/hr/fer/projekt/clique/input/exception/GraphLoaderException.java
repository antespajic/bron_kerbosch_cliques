package hr.fer.projekt.clique.input.exception;

/**
 * Exception thrown by GraphLoader during runtime in situations
 * where textual file with graph definition could not be loaded
 * or it's content was malformed.
 */
public class GraphLoaderException extends RuntimeException {

    /**
     * Constructor which receives exception description as argument.
     *
     * @param message exception description
     */
    public GraphLoaderException(String message) {
        super(message);
    }

    /**
     * Constructor which receives exception description and underlying
     * exception which caused runtime error as arguments.
     *
     * @param message   exception description
     * @param exception underlying runtime exception
     */
    public GraphLoaderException(String message, Exception exception) {
        super(message, exception);
    }
}
