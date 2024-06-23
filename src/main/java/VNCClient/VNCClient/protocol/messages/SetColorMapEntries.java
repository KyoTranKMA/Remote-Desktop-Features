package VNCClient.VNCClient.protocol.messages;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public record SetColorMapEntries(int firstColor, List<ColorMapEntry> colors) {

    /**
     * Decodes the SetColorMapEntries message from the input stream.
     *
     * @param in The input stream to read from.
     * @return The decoded SetColorMapEntries message.
     * @throws IOException If an I/O error occurs.
     */
    public static SetColorMapEntries decode(InputStream in) throws IOException {
        DataInputStream dataInput = new DataInputStream(in);
        dataInput.readFully(new byte[2]);
        int firstColor = dataInput.readUnsignedShort();
        int numberOfColors = dataInput.readUnsignedShort();
        List<ColorMapEntry> colors = new ArrayList<>();
        for (int i = 0; i < numberOfColors; i++) {
            colors.add(ColorMapEntry.decode(in));
        }
        return new SetColorMapEntries(firstColor, colors);
    }
}
