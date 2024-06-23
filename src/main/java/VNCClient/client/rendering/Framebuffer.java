package VNCClient.client.rendering;

import VNCClient.client.VNCSession;
import VNCClient.client.exceptions.UnexpectedVncException;
import VNCClient.client.exceptions.BaseVncException;
import VNCClient.client.rendering.renderers.*;

import VNCClient.protocol.messages.ColorMapEntry;
import VNCClient.protocol.messages.Encoding;
import VNCClient.protocol.messages.FramebufferUpdate;
import VNCClient.protocol.messages.SetColorMapEntries;
import VNCClient.protocol.messages.Rectangle;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static VNCClient.protocol.messages.Encoding.*;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class Framebuffer {

    private final VNCSession session;
    private final Map<Long, ColorMapEntry> colorMap = new ConcurrentHashMap<>();
    private final Map<Encoding, Renderer> renderers = new ConcurrentHashMap<>();
    private final CursorRenderer cursorRenderer;

    private BufferedImage frame;

    /**
     * Constructs a Framebuffer object for rendering VNC session updates.
     *
     * @param session The VncSession object representing the VNC session.
     */
    public Framebuffer(VNCSession session) {
        PixelDecoder pixelDecoder = new PixelDecoder(colorMap);
        RawRenderer rawRenderer = new RawRenderer(pixelDecoder, session.getPixelFormat());
        renderers.put(RAW, rawRenderer);
        renderers.put(COPYRECT, new CopyRectRenderer());
        renderers.put(RRE, new RRERenderer(pixelDecoder, session.getPixelFormat()));
        renderers.put(HEXTILE, new HextileRenderer(rawRenderer, pixelDecoder, session.getPixelFormat()));
        renderers.put(ZLIB, new ZLibRenderer(rawRenderer));
        cursorRenderer = new CursorRenderer(rawRenderer);

        frame = new BufferedImage(session.getFramebufferWidth(), session.getFramebufferHeight(), TYPE_INT_RGB);
        this.session = session;
    }

    /**
     * Processes a FramebufferUpdate received from the VNC server.
     *
     * @param update The FramebufferUpdate object representing the update.
     * @throws BaseVncException if there is an error processing the update.
     */
    public void processUpdate(FramebufferUpdate update) throws BaseVncException {
        InputStream in = session.getInputStream();
        try {
            for (int i = 0; i < update.numberOfRectangles(); i++) {
                Rectangle rectangle = Rectangle.decode(in);
                switch (rectangle.encoding()) {
                    case DESKTOP_SIZE:
                        resizeFramebuffer(rectangle);
                        break;
                    case CURSOR:
                        updateCursor(rectangle, in);
                        break;
                    default:
                        Renderer renderer = renderers.get(rectangle.encoding());
                        if (renderer != null) {
                            renderer.render(in, frame, rectangle);
                        }
                        break;
                }
            }
            paint();
            session.framebufferUpdated();
        } catch (IOException e) {
            throw new UnexpectedVncException(e);
        }
    }
    /**
     * Paints the framebuffer on the screen.
     */
    private void paint() {
        Consumer<Image> listener = session.getConfig().getScreenUpdateListener();
        if (listener != null) {
            ColorModel colorModel = frame.getColorModel();
            WritableRaster raster = frame.copyData(null);
            BufferedImage copy = new BufferedImage(colorModel, raster, colorModel.isAlphaPremultiplied(), null);
            listener.accept(copy);
        }
    }

    /**
     * Updates the color map with the provided SetColorMapEntries update.
     *
     * @param update The SetColorMapEntries object representing the update.
     */
    public void updateColorMap(SetColorMapEntries update) {
        int firstColor = update.firstColor();
        for (int i = 0; i < update.colors().size(); i++) {
            colorMap.put((long) firstColor + i, update.colors().get(i));
        }
    }

    /**
     * Resizes the framebuffer to the specified size.
     *
     * @param newSize The Rectangle object representing the new size.
     */
    private void resizeFramebuffer(Rectangle newSize) {
        int width = newSize.width();
        int height = newSize.height();
        session.setFramebufferWidth(width);
        session.setFramebufferHeight(height);
        BufferedImage resized = new BufferedImage(width, height, TYPE_INT_RGB);
        Graphics g = resized.getGraphics();
        g.drawImage(frame, 0, 0, null);
        g.dispose();
        frame = resized;
    }

    /**
     * Updates the cursor image with the provided Rectangle update.
     *
     * @param cursor The Rectangle object representing the cursor update.
     * @param in     The InputStream containing the cursor image data.
     * @throws BaseVncException if there is an error updating the cursor.
     */
    private void updateCursor(Rectangle cursor, InputStream in) throws BaseVncException {
        if (cursor.width() > 0 && cursor.height() > 0) {
            BufferedImage cursorImage = new BufferedImage(cursor.width(), cursor.height(), TYPE_INT_ARGB);
            cursorRenderer.render(in, cursorImage, cursor);
            BiConsumer<Image, Point> listener = session.getConfig().getMousePointerUpdateListener();
            if (listener != null) {
                listener.accept(cursorImage, new Point(cursor.x(), cursor.y()));
            }
        }
    }
}
