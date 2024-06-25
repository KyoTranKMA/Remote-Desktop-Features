package VNCClient.VNCClientModule.utils;

import static java.lang.System.arraycopy;

/**
 * The ByteUtils class provides utility methods for working with byte arrays and bits.
 */
public class ByteUtils {

    /**
     * Reverses the bits in each byte of a byte array.
     *
     * @param b the byte array to reverse the bits of
     * @return a new byte array with the bits reversed
     */
    public static byte[] reverseBits(byte[] b) {
        byte[] result = new byte[b.length];
        for (int i = 0; i < b.length; i++) {
            result[i] = reverseBits(b[i]);
        }
        return result;
    }

    /**
     * Reverses the bits in a single byte.
     *
     * @param input the byte to reverse the bits of
     * @return the byte with the bits reversed
     */
    public static byte reverseBits(byte input) {
        byte result = 0x00;
        for (int i = 0; i < 8; i++) {
            result |= (byte) ((byte) ((input & (0x01 << i)) >>> i) << 7 - i);
        }
        return result;
    }

    /**
     * Checks if a specific bit in an integer is set.
     *
     * @param input    the integer to check
     * @param position the position of the bit to check
     * @return true if the bit is set, false otherwise
     */
    public static boolean bitAt(int input, int position) {
        return (input & (0x01 << position)) != 0;
    }

    /**
     * Pads a byte array on the left with zeros to a specified length.
     *
     * @param input  the byte array to pad
     * @param length the desired length of the padded byte array
     * @return a new byte array with the input byte array padded on the left
     */
    public static byte[] padLeft(byte[] input, int length) {
        byte[] padded = new byte[length];
        arraycopy(input, 0, padded, length - input.length, input.length);
        return padded;
    }

    /**
     * Pads a byte array on the right with zeros to a specified length.
     *
     * @param input  the byte array to pad
     * @param length the desired length of the padded byte array
     * @return a new byte array with the input byte array padded on the right
     */
    public static byte[] padRight(byte[] input, int length) {
        byte[] padded = new byte[length];
        arraycopy(input, 0, padded, 0, input.length);
        return padded;
    }

    public static boolean mask(int input, int mask) {
        return (input & mask) != 0;
    }
}
