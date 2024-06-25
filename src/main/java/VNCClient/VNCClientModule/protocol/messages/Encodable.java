package VNCClient.VNCClientModule.protocol.messages;

import java.io.IOException;
import java.io.OutputStream;

public interface Encodable {
    /**
     * Encodes the object and writes it to the specified output stream.
     *
     * @param out the output stream to write the encoded object to
     * @throws IOException if an I/O error occurs while writing to the output stream
     */
    void encode(OutputStream out) throws IOException;
}
