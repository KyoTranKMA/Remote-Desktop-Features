package VNCClient.client.rendering.renderers;

import VNCClient.protocol.messages.ColorMapEntry;
import VNCClient.protocol.messages.PixelFormat;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

/**
 * The PixelDecoder class is responsible for decoding pixel data from an input stream based on the provided pixel format.
 * It uses a color map to map pixel values to RGB colors.
 */
public class PixelDecoder {

    private static final ColorMapEntry BLACK = new ColorMapEntry(0, 0, 0);
    private final Map<Long, ColorMapEntry> colorMap;

    /**
     * Constructs a new PixelDecoder with the given color map.
     *
     * @param colorMap the color map used for mapping pixel values to RGB colors
     */
    public PixelDecoder(Map<Long, ColorMapEntry> colorMap) {
        this.colorMap = colorMap;
    }

    /**
     * Decodes a pixel from the input stream based on the provided pixel format.
     *
     * @param in          the input stream to read pixel data from
     * @param pixelFormat the pixel format used for decoding the pixel
     * @return the decoded pixel
     * @throws IOException if an I/O error occurs while reading from the input stream
     */
    public Pixel decode(InputStream in, PixelFormat pixelFormat) throws IOException {
        long value = readPixelValue(in, pixelFormat.getBytesPerPixel());

        if (pixelFormat.trueColor()) {
            return decodeTrueColor(value, pixelFormat);
        } else {
            return decodeColorMap(value);
        }
    }

    /**
     * Reads the pixel value from the input stream.
     *
     * @param in            the input stream to read from
     * @param bytesToRead   the number of bytes to read
     * @return the pixel value
     * @throws IOException if an I/O error occurs
     */
    private long readPixelValue(InputStream in, int bytesToRead) throws IOException {
        long value = 0L;
        for (int i = 0; i < bytesToRead; i++) {
            value = (value << 8) | in.read();
        }
        return value;
    }

    /**
     * Decodes a true color pixel value.
     *
     * @param value        the pixel value
     * @param pixelFormat  the pixel format
     * @return the decoded pixel
     */
    private Pixel decodeTrueColor(long value, PixelFormat pixelFormat) {
        int red = extractColorComponent(value, pixelFormat.redShift(), pixelFormat.redMax());
        int green = extractColorComponent(value, pixelFormat.greenShift(), pixelFormat.greenMax());
        int blue = extractColorComponent(value, pixelFormat.blueShift(), pixelFormat.blueMax());
        return new Pixel(red, green, blue);
    }

    /**
     * Extracts and stretches a color component from the pixel value.
     *
     * @param value        the pixel value
     * @param shift        the bit shift for the color component
     * @param max          the maximum value for the color component
     * @return the stretched color component
     */
    private int extractColorComponent(long value, int shift, int max) {
        int component = (int) (value >> shift) & max;
        return stretch(component, max);
    }

    /**
     * Decodes a pixel value using the color map.
     *
     * @param value the pixel value
     * @return the decoded pixel
     */
    private Pixel decodeColorMap(long value) {
        ColorMapEntry color = Optional.ofNullable(colorMap.get(value)).orElse(BLACK);
        int red = shrink(color.red());
        int green = shrink(color.green());
        int blue = shrink(color.blue());
        return new Pixel(red, green, blue);
    }

    /**
     * Stretches the given value based on the maximum value.
     * This is used for converting pixel values to the range of 0-255.
     *
     * @param value the value to stretch
     * @param max   the maximum value
     * @return the stretched value
     */
    private static int stretch(int value, int max) {
        return max == 255 ? value : (value * 255) / max;
    }

    /**
     * Shrinks the given color map value.
     * This is used for converting color map values to the range of 0-255.
     *
     * @param colorMapValue the color map value to shrink
     * @return the shrunk value
     */
    private static int shrink(int colorMapValue) {
        return colorMapValue / 257;
    }
}
