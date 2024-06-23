package VNCClient.protocol.messages;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Represents a client initialization message.
 * This message is used to inform the server whether the client wants to share its desktop or not.
 */
public record ClientInit(boolean shared) implements Encodable {

    /**
     * Encodes the client initialization message and writes it to the output stream.
     *
     * @param out The output stream to write the encoded message to.
     * @throws IOException If an I/O error occurs while writing to the output stream.
     */
    @Override
    public void encode(OutputStream out) throws IOException {
        out.write(shared ? new byte[]{1} : new byte[]{0});
    }
}
