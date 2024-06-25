package VNCClient.VNCClientModule.client.exceptions;

/**
 * This exception is thrown when the handshaking process fails during the VNC client connection.
 */
public class HandshakingFailedException extends BaseVncException {

    private final String serverMessage;

    /**
     * Constructs a new HandshakingFailedException with the specified server message.
     *
     * @param serverMessage the server message indicating the reason for the handshaking failure
     */
    public HandshakingFailedException(String serverMessage) {
        super("Failed to perform handshaking. Message: " + serverMessage);
        logger.error("Failed to perform handshaking. Message: {}", serverMessage, this);
        this.serverMessage = serverMessage;
    }

    /**
     * Returns the server message indicating the reason for the handshaking failure.
     *
     * @return the server message
     */
    public String getServerMessage() {
        return serverMessage;
    }

}
