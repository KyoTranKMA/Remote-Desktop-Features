package VNCClient.VNCClient.protocol.messages;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Represents an error message in the VNC protocol.
 */
public class ErrorMessage {

    private final String message;

    /**
     * Constructs an ErrorMessage object with the specified message.
     *
     * @param message the error message
     */
    private ErrorMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the error message.
     *
     * @return the error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Decodes an ErrorMessage from the given input stream.
     *
     * @param input the input stream to decode from
     * @return the decoded ErrorMessage object
     * @throws IOException if an I/O error occurs
     */
    public static ErrorMessage decode(InputStream input) throws IOException {
        DataInputStream dataInput = new DataInputStream(input);
        int errorMessageLength = dataInput.readInt();
        byte[] errorMessageBytes = new byte[errorMessageLength];
        dataInput.readFully(errorMessageBytes);
        return new ErrorMessage(new String(errorMessageBytes, StandardCharsets.US_ASCII));
    }

}
