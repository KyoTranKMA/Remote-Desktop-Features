package VNCClient.VNCClient.protocol.messages;

import VNCClient.VNCClient.client.exceptions.UnsupportedEncodingException;

import static java.util.Arrays.stream;

/**
 * The Encoding class represents the different encoding types used in the VNC protocol.
 */
public enum Encoding {

    /**
     * Raw encoding type.
     */
    RAW(0),

    /**
     * CopyRect encoding type.
     */
    COPYRECT(1),

    /**
     * RRE encoding type.
     */
    RRE(2),

    /**
     * Hextile encoding type.
     */
    HEXTILE(5),

    /**
     * Zlib encoding type.
     */
    ZLIB(6),

    /**
     * Desktop size encoding type.
     */
    DESKTOP_SIZE(-223),

    /**
     * Cursor encoding type.
     */
    CURSOR(-239),

    /**
     * Extended clipboard encoding type.
     */
    EXTENDED_CLIPBOARD(0xC0A1E5CE);

    private final int code;

    /**
     * Constructs an Encoding object with the specified code.
     *
     * @param code The code representing the encoding type.
     */
    Encoding(int code) {
        this.code = code;
    }

    /**
     * Returns the code representing the encoding type.
     *
     * @return The code representing the encoding type.
     */
    public int getCode() {
        return code;
    }

    /**
     * Resolves the encoding type based on the given code.
     *
     * @param code The code representing the encoding type.
     * @return The Encoding object corresponding to the code.
     * @throws UnsupportedEncodingException If the code does not match any encoding type.
     */
    public static Encoding resolve(int code) throws UnsupportedEncodingException {
        return stream(values())
                .filter(e -> e.code == code)
                .findFirst()
                .orElseThrow(() -> new UnsupportedEncodingException(code));
    }
}
