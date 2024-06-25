package VNCClient.VNCClientModule.client.rendering.renderers;

import VNCClient.VNCClientModule.client.exceptions.UnexpectedVncException;
import VNCClient.VNCClientModule.client.exceptions.BaseVncException;
import VNCClient.VNCClientModule.protocol.messages.Rectangle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import static VNCClient.VNCClientModule.utils.ByteUtils.bitAt;

/**
 * The `CursorRenderer` class is responsible for rendering the cursor image onto the destination image.
 * It implements the `Renderer` interface.
 */
public class CursorRenderer implements Renderer {

    private static final int TRANSPARENT = new Color(0, 0, 0, 0).getRGB();

    private final RawRenderer rawRenderer;

    /**
     * Constructs a new `CursorRenderer` object with the specified `RawRenderer`.
     *
     * @param rawRenderer The `RawRenderer` used to render the cursor image.
     */
    public CursorRenderer(RawRenderer rawRenderer) {
        this.rawRenderer = rawRenderer;
    }

    /**
     * Renders the cursor image onto the destination image within the specified rectangle.
     *
     * @param in           The input stream containing the cursor image data.
     * @param destination  The destination image where the cursor image will be rendered.
     * @param rectangle    The rectangle specifying the position and size of the cursor image within the destination image.
     * @throws BaseVncException If an error occurs during rendering.
     */
    @Override
    public void render(InputStream in, BufferedImage destination, Rectangle rectangle) throws BaseVncException {
        try {
            rawRenderer.render(in, destination, 0, 0, rectangle.width(), rectangle.height());

            byte[] bitmask = new byte[((rectangle.width() + 7) / 8) * rectangle.height()];
            new DataInputStream(in).readFully(bitmask);

            int x = 0;
            int y = 0;

            for (byte b : bitmask) {
                for (int i = 7; i >= 0; i--) {
                    boolean visible = bitAt(b, i);
                    if (!visible) {
                        destination.setRGB(x, y, TRANSPARENT);
                    }
                    if (++x == rectangle.width()) {
                        x = 0;
                        y++;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            throw new UnexpectedVncException(e);
        }
    }
}
