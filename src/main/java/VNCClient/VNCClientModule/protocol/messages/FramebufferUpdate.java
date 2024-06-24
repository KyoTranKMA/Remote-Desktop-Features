package VNCClient.VNCClient.protocol.messages;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Represents a framebuffer update message.
 * This message contains the number of rectangles that will be updated in the framebuffer.
 */
public record FramebufferUpdate(int numberOfRectangles) {

    /**
     * Decodes a framebuffer update message from the given input stream.
     *
     * @param in The input stream to read the message from.
     * @return A new FramebufferUpdate instance with the decoded number of rectangles.
     * @throws IOException If an I/O error occurs while reading the input stream.
     */
    public static FramebufferUpdate decode(InputStream in) throws IOException {
        DataInputStream dataInput = new DataInputStream(in);
        dataInput.readFully(new byte[2]);
        int numberOfRectangles = dataInput.readUnsignedShort();
        return new FramebufferUpdate(numberOfRectangles);
    }
}
