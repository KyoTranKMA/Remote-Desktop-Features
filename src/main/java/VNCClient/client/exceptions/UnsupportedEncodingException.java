package VNCClient.client.exceptions;

import static java.lang.String.format;

/**
 * This exception is thrown when an unsupported encoding type is encountered.
 */
public class UnsupportedEncodingException extends BaseVncException {

    private final int encodingType;

    /**
     * Constructs a new `UnsupportedEncodingException` with the specified encoding type.
     *
     * @param encodingType the unsupported encoding type
     */
    public UnsupportedEncodingException(int encodingType) {
        super(format("Unsupported encoding type: %d", encodingType));
        logger.error("Unsupported encoding type: {}", encodingType);
        this.encodingType = encodingType;
    }

    /**
     * Returns the unsupported encoding type.
     *
     * @return the encoding type
     */
    public int getEncodingType() {
        return encodingType;
    }
}
