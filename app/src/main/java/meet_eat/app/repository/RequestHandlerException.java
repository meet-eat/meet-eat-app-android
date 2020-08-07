package meet_eat.app.repository;

/**
 * Thrown to indicate that an error occurred when requesting the repository.
 */
public class RequestHandlerException extends Exception {

    /**
     * Creates an {@code RequestHandlerException} with no detail message.
     */
    public RequestHandlerException() {
        super();
    }

    /**
     * Creates an {@code RequestHandlerException} with the specified detail message.
     *
     * @param message the detail message
     */
    public RequestHandlerException(String message) {
        super(message);
    }
}
