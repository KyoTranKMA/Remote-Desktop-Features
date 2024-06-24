package VNCClient.VNCClient.client.rendering.renderers;

import VNCClient.VNCClient.client.exceptions.UnexpectedVncException;
import VNCClient.VNCClient.client.exceptions.BaseVncException;
import VNCClient.VNCClient.client.rendering.model.Pixel;
import VNCClient.VNCClient.protocol.messages.PixelFormat;
import VNCClient.VNCClient.protocol.messages.Rectangle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import static VNCClient.VNCClient.utils.ByteUtils.mask;

/**
 * The HextileRenderer class is responsible for rendering the Hextile encoding in VNCClient.
 * It implements the Renderer interface.
 */
public class HextileRenderer implements Renderer {

    // Constants for sub-encoding masks
    private static final int SUB_ENCODING_MASK_RAW = 0x01;
    private static final int SUB_ENCODING_MASK_BACKGROUND_SPECIFIED = 0x02;
    private static final int SUB_ENCODING_MASK_FOREGROUND_SPECIFIED = 0x04;
    private static final int SUB_ENCODING_MASK_ANY_SUBRECTS = 0x08;
    private static final int SUB_ENCODING_MASK_SUBRECTS_COLORED = 0x10;

    // Tile size for rendering
    private static final int TILE_SIZE = 16;

    private final RawRenderer rawRenderer;
    private final PixelDecoder pixelDecoder;
    private final PixelFormat pixelFormat;

    /**
     * Constructs a new HextileRenderer object.
     *
     * @param rawRenderer   The RawRenderer object used for rendering raw tiles.
     * @param pixelDecoder  The PixelDecoder object used for decoding pixel data.
     * @param pixelFormat   The PixelFormat object representing the pixel format of the VNC session.
     */
    public HextileRenderer(RawRenderer rawRenderer, PixelDecoder pixelDecoder, PixelFormat pixelFormat) {
        this.pixelDecoder = pixelDecoder;
        this.rawRenderer = rawRenderer;
        this.pixelFormat = pixelFormat;
    }

    /**
     * Renders the Hextile encoding using the provided input stream and destination image.
     *
     * @param in            The input stream containing the Hextile encoding data.
     * @param destination   The destination BufferedImage object to render the encoding onto.
     * @param rectangle     The Rectangle object representing the area to render.
     * @throws BaseVncException if there is an error during rendering.
     */
    @Override
    public void render(InputStream in, BufferedImage destination, Rectangle rectangle) throws BaseVncException {
        try (DataInputStream dataInput = new DataInputStream(in)) {
            int horizontalTileCount = (rectangle.width() + TILE_SIZE - 1) / TILE_SIZE;
            int verticalTileCount = (rectangle.height() + TILE_SIZE - 1) / TILE_SIZE;

            Pixel lastBackground = null;
            Pixel lastForeground = null;
            Graphics2D g = (Graphics2D) destination.getGraphics();

            // Set rendering hint for antialiasing
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Iterate over each tile in the rectangle
            for (int tileY = 0; tileY < verticalTileCount; tileY++) {
                for (int tileX = 0; tileX < horizontalTileCount; tileX++) {
                    int tileTopLeftX = rectangle.x() + (tileX * TILE_SIZE);
                    int tileTopLeftY = rectangle.y() + (tileY * TILE_SIZE);
                    int tileWidth = getTileSize(tileX, horizontalTileCount, rectangle.width());
                    int tileHeight = getTileSize(tileY, verticalTileCount, rectangle.height());
                    int subEncoding = dataInput.readUnsignedByte();

                    if (mask(subEncoding, SUB_ENCODING_MASK_RAW)) {
                        // Render raw tile
                        rawRenderer.render(in, destination, tileTopLeftX, tileTopLeftY, tileWidth, tileHeight);
                    } else {
                        if (mask(subEncoding, SUB_ENCODING_MASK_BACKGROUND_SPECIFIED)) {
                            lastBackground = pixelDecoder.decode(in, pixelFormat);
                        }
                        if (mask(subEncoding, SUB_ENCODING_MASK_FOREGROUND_SPECIFIED)) {
                            lastForeground = pixelDecoder.decode(in, pixelFormat);
                        }

                        // Set background color and fill the tile
                        assert lastBackground != null;
                        g.setColor(new Color(lastBackground.red(), lastBackground.green(), lastBackground.blue()));
                        g.fillRect(tileTopLeftX, tileTopLeftY, tileWidth, tileHeight);

                        if (mask(subEncoding, SUB_ENCODING_MASK_ANY_SUBRECTS)) {
                            renderSubRectangles(dataInput, g, subEncoding, tileTopLeftX, tileTopLeftY, lastForeground);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new UnexpectedVncException(e);
        }
    }

    /**
     * Renders subRectangles for the current tile.
     *
     * @param dataInput       The DataInputStream object for reading subRectangle data.
     * @param g               The Graphics2D object used for drawing.
     * @param subEncoding     The subEncoding type.
     * @param tileTopLeftX    The X coordinate of the top-left corner of the tile.
     * @param tileTopLeftY    The Y coordinate of the top-left corner of the tile.
     * @param lastForeground  The last foreground color used.
     * @throws IOException if an I/O error occurs.
     */
    private void renderSubRectangles(
            DataInputStream dataInput, Graphics2D g, int subEncoding,
            int tileTopLeftX, int tileTopLeftY, Pixel lastForeground
    ) throws IOException {
        int subRectCount = dataInput.readUnsignedByte();
        boolean subRectsColored = mask(subEncoding, SUB_ENCODING_MASK_SUBRECTS_COLORED);

        for (int s = 0; s < subRectCount; s++) {
            Pixel subRectColor = subRectsColored ? pixelDecoder.decode(dataInput, pixelFormat) : lastForeground;
            int coords = dataInput.readUnsignedByte();
            int dimensions = dataInput.readUnsignedByte();
            int subRectX = coords >> 4;
            int subRectY = coords & 0x0F;
            int subRectWidth = (dimensions >> 4) + 1;
            int subRectHeight = (dimensions & 0x0F) + 1;
            int subRectTopLeftX = tileTopLeftX + subRectX;
            int subRectTopLeftY = tileTopLeftY + subRectY;

            g.setColor(new Color(subRectColor.red(), subRectColor.green(), subRectColor.blue()));
            g.fillRect(subRectTopLeftX, subRectTopLeftY, subRectWidth, subRectHeight);
        }
    }

    /**
     * Calculates the size of a tile based on its position and the number of tiles in the rectangle.
     *
     * @param tileNo          The index of the tile.
     * @param numberOfTiles   The total number of tiles in the rectangle.
     * @param rectangleSize   The size of the rectangle.
     * @return The size of the tile.
     */
    private static int getTileSize(int tileNo, int numberOfTiles, int rectangleSize) {
        int overlap = rectangleSize % TILE_SIZE;
        return (tileNo == numberOfTiles - 1 && overlap != 0) ? overlap : TILE_SIZE;
    }
}
