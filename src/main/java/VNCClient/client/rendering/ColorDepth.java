package VNCClient.client.rendering;

/**
 * The ColorDepth enum represents different color depths for rendering in a VNC client.
 * Each color depth has specific properties such as bits per pixel, depth, true color support,
 * maximum values for red, blue, and green channels, and shift values for each channel.
 */
public enum ColorDepth {

    /** 8 bits per pixel indexed color **/
    BPP_8_INDEXED(8, 8, false, 0, 0, 0, 0, 0, 0),

    /** 8 bits per pixel true color **/
    BPP_8_TRUE(8, 8, true, 7, 3, 7, 0, 6, 3),

    /** 16 bits per pixel true color **/
    BPP_16_TRUE(16, 16, true, 31, 63, 31, 11, 5, 0),

    /** 24 bits per pixel true color **/
    BPP_24_TRUE(32, 24, true, 255, 255, 255, 8, 24, 16);

    private final int bitsPerPixel;
    private final int depth;
    private final boolean trueColor;
    private final int redMax;
    private final int blueMax;
    private final int greenMax;
    private final int redShift;
    private final int blueShift;
    private final int greenShift;

    /**
     * Constructs a ColorDepth enum with the specified properties.
     *
     * @param bitsPerPixel The number of bits per pixel.
     * @param depth The depth of the color.
     * @param trueColor True if the color is true color, false if it is indexed color.
     * @param redMax The maximum value for the red channel.
     * @param blueMax The maximum value for the blue channel.
     * @param greenMax The maximum value for the green channel.
     * @param redShift The shift value for the red channel.
     * @param blueShift The shift value for the blue channel.
     * @param greenShift The shift value for the green channel.
     */
    ColorDepth(int bitsPerPixel, int depth, boolean trueColor, int redMax, int blueMax, int greenMax, int redShift, int blueShift, int greenShift) {
        this.bitsPerPixel = bitsPerPixel;
        this.depth = depth;
        this.trueColor = trueColor;
        this.redMax = redMax;
        this.blueMax = blueMax;
        this.greenMax = greenMax;
        this.redShift = redShift;
        this.blueShift = blueShift;
        this.greenShift = greenShift;
    }

    /**
     * Returns the number of bits per pixel for this color depth.
     *
     * @return The number of bits per pixel.
     */
    public int getBitsPerPixel() {
        return bitsPerPixel;
    }

    /**
     * Returns the depth of the color for this color depth.
     *
     * @return The depth of the color.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Returns true if the color is true color, false if it is indexed color.
     *
     * @return True if the color is true color, false if it is indexed color.
     */
    public boolean isTrueColor() {
        return trueColor;
    }

    /**
     * Returns the maximum value for the red channel for this color depth.
     *
     * @return The maximum value for the red channel.
     */
    public int getRedMax() {
        return redMax;
    }

    /**
     * Returns the maximum value for the blue channel for this color depth.
     *
     * @return The maximum value for the blue channel.
     */
    public int getBlueMax() {
        return blueMax;
    }

    /**
     * Returns the maximum value for the green channel for this color depth.
     *
     * @return The maximum value for the green channel.
     */
    public int getGreenMax() {
        return greenMax;
    }

    /**
     * Returns the shift value for the red channel for this color depth.
     *
     * @return The shift value for the red channel.
     */
    public int getRedShift() {
        return redShift;
    }

    /**
     * Returns the shift value for the blue channel for this color depth.
     *
     * @return The shift value for the blue channel.
     */
    public int getBlueShift() {
        return blueShift;
    }

    /**
     * Returns the shift value for the green channel for this color depth.
     *
     * @return The shift value for the green channel.
     */
    public int getGreenShift() {
        return greenShift;
    }
}
