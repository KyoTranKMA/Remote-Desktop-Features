package VNCClient.VNCClientModule.client.rendering.renderers;

import VNCClient.VNCClientModule.client.exceptions.BaseVncException;
import VNCClient.VNCClientModule.client.exceptions.UnexpectedVncException;
import VNCClient.VNCClientModule.client.rendering.model.Pixel;
import VNCClient.VNCClientModule.protocol.messages.PixelFormat;
import VNCClient.VNCClientModule.protocol.messages.Rectangle;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * The RawRenderer class is responsible for rendering raw pixel data onto a BufferedImage.
 * It implements the Renderer interface.
 */
public class RawRenderer implements Renderer {

    private final PixelDecoder pixelDecoder;
    private final PixelFormat pixelFormat;

    /**
     * Constructs a new RawRenderer object with the specified pixel decoder and pixel format.
     *
     * @param pixelDecoder The pixel decoder used to decode the pixel data.
     * @param pixelFormat  The pixel format of the raw pixel data.
     */
    public RawRenderer(PixelDecoder pixelDecoder, PixelFormat pixelFormat) {
        this.pixelDecoder = pixelDecoder;
        this.pixelFormat = pixelFormat;
    }

    /**
     * Renders the raw pixel data from the input stream onto the destination BufferedImage within the specified rectangle.
     *
     * @param in          The input stream containing the raw pixel data.
     * @param destination The BufferedImage to render the pixel data onto.
     * @param rectangle   The rectangle specifying the area within the destination image to render the pixel data.
     * @throws BaseVncException If an error occurs during rendering.
     */
    @Override
    public void render(InputStream in, BufferedImage destination, Rectangle rectangle) throws BaseVncException {
        render(in, destination, rectangle.x(), rectangle.y(), rectangle.width(), rectangle.height());
    }

    /**
     * Renders the raw pixel data from the input stream onto the destination BufferedImage within the specified region.
     *
     * @param in      The input stream containing the raw pixel data.
     * @param destination The BufferedImage to render the pixel data onto.
     * @param x       The x-coordinate of the top-left corner of the region.
     * @param y       The y-coordinate of the top-left corner of the region.
     * @param width   The width of the region.
     * @param height  The height of the region.
     * @throws BaseVncException If an error occurs during rendering.
     */
    void render(InputStream in, BufferedImage destination, int x, int y, int width, int height) throws BaseVncException {
        try {
            int sx = x;
            int sy = y;
            for (int i = 0; i < width * height; i++) {
                Pixel pixel = pixelDecoder.decode(in, pixelFormat);
                int rgb = (pixel.red() << 16) | (pixel.green() << 8) | pixel.blue();
                destination.setRGB(sx, sy, rgb);
                sx++;
                if (sx == x + width) {
                    sx = x;
                    sy++;
                }
            }
        } catch (IOException e) {
            throw new UnexpectedVncException(e);
        }
    }
}
