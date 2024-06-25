package VNCClient.VNCClientModule.client.exceptions;

/**
 * This exception is thrown when authentication fails during a VNC client connection.
 */
public class AuthenticationFailedException extends BaseVncException {

    private final String serverMessage;

    /**
     * Constructs a new AuthenticationFailedException with no server message.
     */
    public AuthenticationFailedException() {
        super("Authentication failed. Exception: no message is returned.");
        logger.error("Authentication failed. Exception: no message is returned.", this);

        serverMessage = null;
    }

    /**
     * Constructs a new AuthenticationFailedException with the specified server message.
     *
     * @param serverMessage the server message indicating the reason for authentication failure
     */
    public AuthenticationFailedException(String serverMessage) {
        super("Authentication failed. Exception: " + serverMessage);
        logger.error("Authentication failed. Exception: {}", serverMessage, this);
        this.serverMessage = serverMessage;
    }

    /**
     * Gets the server message indicating the reason for authentication failure.
     *
     * @return the server message
     */
    public String getServerMessage() {
        return serverMessage;
    }
}
