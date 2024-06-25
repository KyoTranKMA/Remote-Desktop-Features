package VNCClient.VNCClientModule.client.exceptions;

import static java.lang.String.format;

/**
 * This exception is thrown when the server sends an invalid message.
 */
public class InvalidMessageException extends BaseVncException {

    private final String messageType;

    /**
     * Constructs a new InvalidMessageException with the specified message type.
     *
     * @param messageType the type of the invalid message
     */
    public InvalidMessageException(String messageType) {
        super(format("The server sent an invalid '%s' message", messageType));
        logger.error("The server sent an invalid '{}' message", messageType);
        this.messageType = messageType;
    }

    /**
     * Returns the type of the invalid message.
     *
     * @return the message type
     */
    public String getMessageType() {
        return messageType;
    }
}
