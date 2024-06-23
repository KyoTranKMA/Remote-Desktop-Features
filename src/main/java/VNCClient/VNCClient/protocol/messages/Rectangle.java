package VNCClient.VNCClient.protocol.messages;


import VNCClient.VNCClient.client.exceptions.UnsupportedEncodingException;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Represents a rectangle with position, size, and encoding information.
 */
public record Rectangle(int x, int y, int width, int height, Encoding encoding) {

    /**
     * Decodes a rectangle from the given input stream.
     *
     * @param in The input stream to read from.
     * @return The decoded rectangle.
     * @throws UnsupportedEncodingException If the encoding is not supported.
     * @throws IOException                  If an I/O error occurs while reading from the stream.
     */
    public static Rectangle decode(InputStream in) throws UnsupportedEncodingException, IOException {
        DataInputStream dataInput = new DataInputStream(in);
        int x = dataInput.readUnsignedShort();
        int y = dataInput.readUnsignedShort();
        int width = dataInput.readUnsignedShort();
        int height = dataInput.readUnsignedShort();
        int encodingType = dataInput.readInt();
        Encoding encoding = Encoding.resolve(encodingType);
        return new Rectangle(x, y, width, height, encoding);
    }
}
