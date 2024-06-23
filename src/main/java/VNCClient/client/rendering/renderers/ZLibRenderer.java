package VNCClient.client.rendering.renderers;

import VNCClient.client.exceptions.UnexpectedVncException;
import VNCClient.client.exceptions.BaseVncException;
import VNCClient.protocol.messages.Rectangle;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * The ZLibRenderer class is responsible for rendering compressed rectangles using the ZLib compression algorithm.
 * It implements the Renderer interface.
 */
public class ZLibRenderer implements Renderer {

    private final RawRenderer rawRenderer;
    private final Inflater inflater;

    /**
     * Constructs a new ZLibRenderer object with the specified RawRenderer.
     *
     * @param rawRenderer the RawRenderer to use for rendering decompressed rectangles
     */
    public ZLibRenderer(RawRenderer rawRenderer) {
        this.rawRenderer = rawRenderer;
        this.inflater = new Inflater();
    }

    /**
     * Renders the compressed rectangle from the input stream onto the destination image using the specified rectangle dimensions.
     *
     * @param in          the input stream containing the compressed rectangle data
     * @param destination the destination image to render onto
     * @param rectangle   the dimensions of the rectangle to render
     * @throws BaseVncException if an unexpected exception occurs during rendering
     */
    @Override
    public void render(InputStream in, BufferedImage destination, Rectangle rectangle) throws BaseVncException {
        try {
            DataInput dataInput = new DataInputStream(in);
            int compressedLength = dataInput.readInt();
            byte[] compressedData = new byte[compressedLength];
            dataInput.readFully(compressedData);
            inflater.setInput(compressedData);

            int read;
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((read = inflater.inflate(buffer)) != 0) {
                baos.write(buffer, 0, read);
            }

            byte[] decompressedData = baos.toByteArray();

            rawRenderer.render(
                    new ByteArrayInputStream(decompressedData),
                    destination,
                    rectangle.x(),
                    rectangle.y(),
                    rectangle.width(),
                    rectangle.height());

        } catch (IOException | DataFormatException e) {
            throw new UnexpectedVncException(e);
        }
    }
}
