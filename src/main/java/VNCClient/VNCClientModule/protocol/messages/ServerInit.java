package VNCClient.VNCClientModule.protocol.messages;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * The ServerInit class represents the server initialization message in the VNC protocol.
 * It contains information about the framebuffer dimensions, pixel format, and server name.
 */
public class ServerInit {

    private final int framebufferWidth;
    private final int framebufferHeight;
    private final PixelFormat pixelFormat;
    private final String name;

    /**
     * Constructs a ServerInit object with the specified parameters.
     *
     * @param framebufferWidth  the width of the server's framebuffer
     * @param framebufferHeight the height of the server's framebuffer
     * @param pixelFormat       the pixel format used by the server
     * @param name              the name of the server
     */
    private ServerInit(int framebufferWidth, int framebufferHeight, PixelFormat pixelFormat, String name) {
        this.framebufferWidth = framebufferWidth;
        this.framebufferHeight = framebufferHeight;
        this.pixelFormat = pixelFormat;
        this.name = name;
    }

    /**
     * Returns the width of the server's framebuffer.
     *
     * @return the framebuffer width
     */
    public int getFramebufferWidth() {
        return framebufferWidth;
    }

    /**
     * Returns the height of the server's framebuffer.
     *
     * @return the framebuffer height
     */
    public int getFramebufferHeight() {
        return framebufferHeight;
    }

    /**
     * Returns the pixel format used by the server.
     *
     * @return the pixel format
     */
    public PixelFormat getPixelFormat() {
        return pixelFormat;
    }

    /**
     * Returns the name of the server.
     *
     * @return the server name
     */
    public String getName() {
        return name;
    }

    /**
     * Decodes a ServerInit message from the given input stream.
     *
     * @param in the input stream to read from
     * @return the decoded ServerInit message
     * @throws IOException if an I/O error occurs
     */
    public static ServerInit decode(InputStream in) throws IOException {
        DataInputStream dataInput = new DataInputStream(in);
        int framebufferWidth = dataInput.readUnsignedShort();
        int framebufferHeight = dataInput.readUnsignedShort();
        PixelFormat pixelFormat = PixelFormat.decode(in);
        int nameLength = dataInput.readInt();
        byte[] nameBytes = new byte[nameLength];
        dataInput.readFully(nameBytes);
        String name = new String(nameBytes, Charset.forName("US-ASCII"));
        return new ServerInit(framebufferWidth, framebufferHeight, pixelFormat, name);
    }
}
