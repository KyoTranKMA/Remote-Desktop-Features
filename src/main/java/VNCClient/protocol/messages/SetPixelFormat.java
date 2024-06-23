package VNCClient.protocol.messages;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Represents a message that sets the pixel format for the VNC client.
 */
public class SetPixelFormat implements Encodable {

    private final PixelFormat pixelFormat;

    /**
     * Constructs a new SetPixelFormat message with the specified pixel format.
     *
     * @param pixelFormat the pixel format to be set
     */
    public SetPixelFormat(PixelFormat pixelFormat) {
        this.pixelFormat = pixelFormat;
    }

    /**
     * Encodes the SetPixelFormat message and writes it to the output stream.
     *
     * @param out the output stream to write the encoded message to
     * @throws IOException if an I/O error occurs while writing to the output stream
     */
    @Override
    public void encode(OutputStream out) throws IOException {
        out.write(0x00);
        out.write(new byte[3]);
        pixelFormat.encode(out);
    }
}
