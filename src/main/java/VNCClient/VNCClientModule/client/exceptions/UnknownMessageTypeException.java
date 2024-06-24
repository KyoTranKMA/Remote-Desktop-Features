package VNCClient.VNCClient.client.exceptions;

import static java.lang.String.format;

/**
 * Exception thrown when an unknown message type is received.
 */
public class UnknownMessageTypeException extends BaseVncException {

    private final int messageType;

    public int getMessageType() {
        return messageType;
    }

    public UnknownMessageTypeException(int messageType) {
        super(format("Received unexpected message type: %s", messageType));
        logger.error("Received unexpected message type: {}", messageType);
        this.messageType = messageType;
    }

}
