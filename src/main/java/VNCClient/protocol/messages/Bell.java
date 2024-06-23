package VNCClient.protocol.messages;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The Bell class represents a bell message in the VNC protocol.
 */
public class Bell {

    /**
     * Decodes a bell message from the input stream.
     *
     * @param in The input stream to read from.
     * @return A new instance of the Bell class.
     * @throws IOException If an I/O error occurs while reading from the input stream.
     */
    public static Bell decode(InputStream in) throws IOException {
        DataInputStream dataInput = new DataInputStream(in);
        dataInput.readFully(new byte[1]);
        return new Bell();
    }
}
