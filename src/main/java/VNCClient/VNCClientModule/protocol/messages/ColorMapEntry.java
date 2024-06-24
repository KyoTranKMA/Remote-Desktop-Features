package VNCClient.VNCClient.protocol.messages;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Represents a color map entry with red, green, and blue components.
 */
public record ColorMapEntry(int red, int green, int blue) {

    /**
     * Decodes a color map entry from the given input stream.
     *
     * @param in The input stream to read from.
     * @return A new ColorMapEntry object with the decoded red, green, and blue components.
     * @throws IOException If an I/O error occurs while reading from the input stream.
     */
    public static ColorMapEntry decode(InputStream in) throws IOException {
        DataInputStream dataInput = new DataInputStream(in);
        int red = dataInput.readUnsignedShort();
        int green = dataInput.readUnsignedShort();
        int blue = dataInput.readUnsignedShort();
        return new ColorMapEntry(red, green, blue);
    }
}
