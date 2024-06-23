package VNCClient.VNCClient.client.rendering.renderers;


import VNCClient.VNCClient.client.exceptions.UnexpectedVncException;
import VNCClient.VNCClient.client.exceptions.BaseVncException;
import VNCClient.VNCClient.protocol.messages.Rectangle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The CopyRectRenderer class is responsible for rendering a copy rectangle operation
 * on the destination image.
 */
public class CopyRectRenderer implements Renderer {

    /**
     * Renders the specified rectangle from the input stream onto the destination image.
     *
     * @param inStream the input stream containing the image data
     * @param destination the destination image onto which the rectangle will be rendered
     * @param rectangle the rectangle specifying the region to be rendered
     * @throws BaseVncException if an error occurs during rendering
     */
    @Override
    public void render(InputStream inStream, BufferedImage destination, Rectangle rectangle) throws BaseVncException {
        try (DataInputStream dataInput = new DataInputStream(inStream)) {
            int srcX = dataInput.readUnsignedShort();
            int srcY = dataInput.readUnsignedShort();
            int width = rectangle.width();
            int height = rectangle.height();
            int destX = rectangle.x();
            int destY = rectangle.y();

            if (srcX + width > destination.getWidth() || srcY + height > destination.getHeight()) {
                throw new UnexpectedVncException(new IOException("Source rectangle is out of bounds."));
            }

            BufferedImage src = destination.getSubimage(srcX, srcY, width, height);

            Graphics g = destination.getGraphics();
            try {
                g.drawImage(src, destX, destY, null);
            } finally {
                g.dispose();
            }
        } catch (IOException e) {
            throw new UnexpectedVncException(e);
        }
    }
}
