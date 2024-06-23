package VNCClient.VNCClient.client.rendering.renderers;

import VNCClient.VNCClient.client.exceptions.BaseVncException;
import VNCClient.VNCClient.protocol.messages.Rectangle;

import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * The Renderer interface represents a rendering component that is responsible for rendering
 * a portion of an image onto a destination image.
 */
public interface Renderer {
    /**
     * Renders the specified portion of an image onto the destination image.
     *
     * @param in           the input stream containing the image data
     * @param destination  the destination image onto which the portion of the image will be rendered
     * @param rectangle    the rectangle representing the portion of the image to be rendered
     * @throws BaseVncException if there is an error during the rendering process
     */
    void render(InputStream in, BufferedImage destination, Rectangle rectangle) throws BaseVncException;
}
