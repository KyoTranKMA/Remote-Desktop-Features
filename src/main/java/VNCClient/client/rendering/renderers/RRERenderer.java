package VNCClient.client.rendering.renderers;


import VNCClient.client.exceptions.UnexpectedVncException;
import VNCClient.client.exceptions.BaseVncException;
import VNCClient.protocol.messages.PixelFormat;
import VNCClient.protocol.messages.Rectangle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The RRERenderer class is an implementation of the Renderer interface that handles the rendering of pixel data
 * encoded in the RRE (Rising Rectangle Run-length) format. RRE is a type of encoding used in the VNC (Virtual Network Computing) protocol.
 * <p>
 * This class uses a PixelDecoder to decode the pixel data and a PixelFormat to understand the format of the pixel data.
 */
public class RRERenderer implements Renderer {

    private final PixelDecoder pixelDecoder;
    private final PixelFormat pixelFormat;

    /**
     * Constructs a new RRERenderer object with the specified pixel decoder and pixel format.
     *
     * @param pixelDecoder The pixel decoder used to decode the pixel data.
     * @param pixelFormat  The pixel format of the raw pixel data.
     */
    public RRERenderer(PixelDecoder pixelDecoder, PixelFormat pixelFormat) {
        this.pixelDecoder = pixelDecoder;
        this.pixelFormat = pixelFormat;
    }

    /**
     * Renders the RRE-encoded pixel data from the input stream onto the destination BufferedImage within the specified rectangle.
     * <p>
     * The method first reads the number of sub-rectangles from the input stream. Then it decodes the background color and fills the entire rectangle with it.
     * After that, for each sub-rectangle, it decodes the color, reads the position and dimensions, and fills the corresponding area on the destination image with the decoded color.
     *
     * @param in          The input stream containing the RRE-encoded pixel data.
     * @param destination The BufferedImage to render the pixel data onto.
     * @param rectangle   The rectangle specifying the area within the destination image to render the pixel data.
     * @throws BaseVncException If an error occurs during rendering.
     */
    @Override
    public void render(InputStream in, BufferedImage destination, Rectangle rectangle) throws BaseVncException {
        try {
            DataInput dataInput = new DataInputStream(in);
            int numberOfSubRectangles = dataInput.readInt();
            Pixel bgColor = pixelDecoder.decode(in, pixelFormat);

            Graphics2D graphic = (Graphics2D) destination.getGraphics();
            graphic.setColor(new Color(bgColor.red(), bgColor.green(), bgColor.blue()));
            graphic.fillRect(rectangle.x(), rectangle.y(), rectangle.width(), rectangle.height());

            for (int i = 0; i < numberOfSubRectangles; i++) {
                Pixel color = pixelDecoder.decode(in, pixelFormat);
                int x = dataInput.readUnsignedShort();
                int y = dataInput.readUnsignedShort();
                int width = dataInput.readUnsignedShort();
                int height = dataInput.readUnsignedShort();
                graphic.setColor(new Color(color.red(), color.green(), color.blue()));
                graphic.fillRect(x + rectangle.x(), y + rectangle.y(), width, height);
            }
        } catch (IOException e) {
            throw new UnexpectedVncException(e);
        }
    }
}
