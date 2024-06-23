package VNCClient.protocol.messages;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Represents the result of a security negotiation in the VNC protocol.
 */
public class SecurityResult {

    private final boolean success;
    private final String errorMessage;

    /**
     * Constructs a SecurityResult object with the specified success status and no error message.
     *
     * @param success true if the security negotiation was successful, false otherwise
     */
    public SecurityResult(boolean success) {
        this(success, null);
    }

    private SecurityResult(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    /**
     * Returns whether the security negotiation was successful.
     *
     * @return true if the security negotiation was successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Returns the error message associated with the security negotiation, if any.
     *
     * @return the error message, or null if there is no error
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Decodes a SecurityResult object from the given input stream.
     *
     * @param in      the input stream to read from
     * @param version the protocol version
     * @return the decoded SecurityResult object
     * @throws IOException if an I/O error occurs while reading from the input stream
     */
    public static SecurityResult decode(InputStream in, ProtocolVersion version) throws IOException {
        DataInputStream dataInput = new DataInputStream(in);
        int resultCode = dataInput.readInt();
        String errorMessage = null;
        if (resultCode == 1) {
            if (version.equals(3, 8)) {
                errorMessage = ErrorMessage.decode(in).getMessage();
            }
        }
        return new SecurityResult(resultCode != 1, errorMessage);
    }

}
