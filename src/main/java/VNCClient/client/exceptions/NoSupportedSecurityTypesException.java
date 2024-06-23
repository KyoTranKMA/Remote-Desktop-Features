package VNCClient.client.exceptions;

/**
 * This exception is thrown when the VNC server does not support any of the VNC security types
 * This exception extends the {@link BaseVncException} class.
 */
public class NoSupportedSecurityTypesException extends BaseVncException {

    public NoSupportedSecurityTypesException() {
        super("Server does not support any VNC security types supported by this client");
        logger.error("Server does not support any VNC security types supported by this client");
    }

}
