package VNCClient.VNCClient.client.exceptions;

/**
 * This exception is thrown when authentication steps are required to perform further actions.
 * It is a subclass of the BaseVncException class.
 */
public class AuthenticationRequiredException extends BaseVncException {

    public AuthenticationRequiredException() {
        super("Exception: authentication steps are required to perform further actions.");
    }
}
