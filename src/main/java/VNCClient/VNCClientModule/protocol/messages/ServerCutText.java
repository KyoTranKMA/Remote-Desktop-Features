package VNCClient.VNCClientModule.protocol.messages;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;


/**
 * Represents a ServerCutText message in the VNC protocol.
 * This message is used to notify the client that the server has cut text from the clipboard.
 */
public record ServerCutText(String text) {

    /**
     * Decodes a ServerCutText message from the given input stream.
     *
     * @param in The input stream to read the message from.
     * @return The decoded ServerCutText message.
     * @throws IOException If an I/O error occurs while reading the message.
     */
    public static ServerCutText decode(InputStream in) throws IOException {
        DataInputStream dataInput = new DataInputStream(in);
        dataInput.readFully(new byte[4]);

        int textLength = dataInput.readInt();

        if (textLength < 0) {
            return decodeExtendedMessageFormat(dataInput, -textLength);
        }

        return decodeOriginalFormat(dataInput, textLength);
    }

    /**
     * Decodes a ServerCutText message in the original format.
     *
     * @param dataInput   The data input stream to read the message from.
     * @param textLength  The length of the text.
     * @return The decoded ServerCutText message.
     * @throws IOException If an I/O error occurs while reading the message.
     */
    private static ServerCutText decodeOriginalFormat(DataInputStream dataInput, int textLength) throws IOException {
        byte[] textBytes = new byte[textLength];
        dataInput.readFully(textBytes);
        String text = new String(textBytes, StandardCharsets.ISO_8859_1);
        return new ServerCutText(text);
    }

    /**
     * Decodes a ServerCutText message in the extended message format.
     *
     * @param dataInput   The data input stream to read the message from.
     * @param textLength  The length of the text.
     * @return The decoded ServerCutText message.
     * @throws IOException If an I/O error occurs while reading the message.
     */
    private static ServerCutText decodeExtendedMessageFormat(DataInputStream dataInput, int textLength) throws IOException {
        Charset charset = StandardCharsets.UTF_8;

        int flags = dataInput.readInt();

        byte[] textBytes = new byte[textLength - 4];
        dataInput.readFully(textBytes);

        if ((flags & MessageHeaderFlags.CAPS.code) != 0) {
            return new ServerCutText("");
        }

        if ((flags & MessageHeaderFlags.PROVIDE.code) != 0) {
            return decompressText(textBytes, charset);
        }

        return new ServerCutText("");
    }

    /**
     * Decompresses the text using the provided charset.
     *
     * @param textBytes  The compressed text bytes.
     * @param charset    The charset to use for decoding the text.
     * @return The decompressed ServerCutText message.
     * @throws IOException If an I/O error occurs during decompression.
     */
    private static ServerCutText decompressText(byte[] textBytes, Charset charset) throws IOException {
        Inflater inflater = new Inflater();
        inflater.setInput(textBytes);
        byte[] result = new byte[20 * 1024 * 1024]; // 20 MB buffer
        int length;

        try {
            length = inflater.inflate(result);
        } catch (DataFormatException e) {
            throw new IOException("Data format exception during decompression", e);
        } finally {
            inflater.end();
        }

        if (length <= 4) {
            throw new IOException("Decompressed data is too short to contain valid length information");
        }

        int textLength = new BigInteger(Arrays.copyOfRange(result, 0, 4)).intValue();
        if (textLength < 0 || textLength > result.length - 4) {
            throw new IOException("Invalid decompressed text length");
        }

        String text = new String(result, 4, textLength - 1, charset);
        return new ServerCutText(text);
    }
}