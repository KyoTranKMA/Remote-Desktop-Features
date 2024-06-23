package VNCClient.protocol.messages;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Represents a framebuffer update request in the VNC protocol.
 * This class is used to request updates to a specific region of the remote framebuffer.
 */
public class FramebufferUpdateRequest implements Encodable {

    private final boolean incremental;
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    /**
     * Constructs a new FramebufferUpdateRequest object.
     *
     * @param incremental true if the update should be incremental, false otherwise
     * @param x           the x-coordinate of the top-left corner of the region to update
     * @param y           the y-coordinate of the top-left corner of the region to update
     * @param width       the width of the region to update
     * @param height      the height of the region to update
     */
    public FramebufferUpdateRequest(boolean incremental, int x, int y, int width, int height) {
        this.incremental = incremental;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Encodes the framebuffer update request into the provided output stream.
     *
     * @param out the output stream to write the encoded data to
     * @throws IOException if an I/O error occurs while writing to the output stream
     */
    @Override
    public void encode(OutputStream out) throws IOException {
        DataOutput dataOutput = new DataOutputStream(out);
        dataOutput.writeByte(0x03);
        dataOutput.writeBoolean(incremental);
        dataOutput.writeShort(x);
        dataOutput.writeShort(y);
        dataOutput.writeShort(width);
        dataOutput.writeShort(height);
    }
}
