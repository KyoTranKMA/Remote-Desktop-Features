package VNCClient.VNCClient.client.exceptions;

/**
 * Represents an exception that is thrown when an unexpected exception occurs in the VNC client.
 * This exception extends the {@link BaseVncException} class.
 */
public class UnexpectedVncException extends BaseVncException {
    public UnexpectedVncException(Throwable cause) {
        super("An unexpected exception occurred: " + cause.getClass().getSimpleName() + " " + cause.getMessage(), cause);
        logger.error("An unexpected exception occurred: {} {}", cause.getClass().getSimpleName(), cause.getMessage(), cause);
    }

}
