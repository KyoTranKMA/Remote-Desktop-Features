package VNCClient.client.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The base class for all VNC exceptions.
 */
public abstract class BaseVncException extends Exception {
    protected static final Logger logger = LogManager.getLogger(UnexpectedVncException.class);

    /**
     * Constructs a new BaseVncException with no detail message.
     */
    public BaseVncException() {}

    /**
     * Constructs a new BaseVncException with the specified detail message.
     *
     * @param message the detail message
     */
    public BaseVncException(String message) {
        super(message);
    }

    /**
     * Constructs a new BaseVncException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public BaseVncException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new BaseVncException with the specified cause and a detail message of (cause==null ? null : cause.toString()) (which typically contains the class and detail message of cause).
     *
     * @param cause the cause
     */
    public BaseVncException(Throwable cause) {
        super(cause);
    }
}
