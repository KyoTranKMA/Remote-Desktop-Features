package VNCClient.VNCClient.protocol.messages;

import java.io.*;

/**
 * Represents the pixel format used in the VNC protocol.
 */
public record PixelFormat(
    int bitsPerPixel, int depth, boolean bigEndian, boolean trueColor, int redMax, int greenMax,
    int blueMax, int redShift, int greenShift, int blueShift) implements Encodable {

    /**
     * Gets the number of bytes per pixel.
     *
     * @return The number of bytes per pixel.
     */
    public int getBytesPerPixel() {
    return bitsPerPixel / 8;
    }

    /**
     * Encodes the pixel format and writes it to the output stream.
     *
     * @param out The output stream to write to.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void encode(OutputStream out) throws IOException {
    DataOutput dataOutput = new DataOutputStream(out);
    dataOutput.writeByte(bitsPerPixel);
    dataOutput.writeByte(depth);
    dataOutput.writeBoolean(bigEndian);
    dataOutput.writeBoolean(trueColor);
    dataOutput.writeShort(redMax);
    dataOutput.writeShort(greenMax);
    dataOutput.writeShort(blueMax);
    dataOutput.writeByte(redShift);
    dataOutput.writeByte(greenShift);
    dataOutput.writeByte(blueShift);
    dataOutput.write(new byte[3]);
    }

    /**
     * Decodes the pixel format from the input stream.
     *
     * @param in The input stream to read from.
     * @return The decoded pixel format.
     * @throws IOException If an I/O error occurs.
     */
    public static PixelFormat decode(InputStream in) throws IOException {
    DataInputStream dataInput = new DataInputStream(in);
    int bpp = dataInput.readUnsignedByte();
    int depth = dataInput.readUnsignedByte();
    boolean bigEndian = dataInput.readBoolean();
    boolean trueColor = dataInput.readBoolean();
    int readMax = dataInput.readUnsignedShort();
    int greenMax = dataInput.readUnsignedShort();
    int blueMax = dataInput.readUnsignedShort();
    int redShift = dataInput.readUnsignedByte();
    int greenShift = dataInput.readUnsignedByte();
    int blueShift = dataInput.readUnsignedByte();
    dataInput.readFully(new byte[3]);
    return new PixelFormat(bpp, depth, bigEndian, trueColor, readMax, greenMax, blueMax, redShift, greenShift, blueShift);
    }
}
