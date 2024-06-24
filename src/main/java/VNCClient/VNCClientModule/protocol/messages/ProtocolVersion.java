package VNCClient.VNCClient.protocol.messages;

import VNCClient.VNCClient.client.exceptions.BaseVncException;
import VNCClient.VNCClient.client.exceptions.InvalidMessageException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;

/**
 * Represents a protocol version in the VNC client.
 * This class provides methods for encoding and decoding protocol versions,
 * as well as checking for equality and minimum version requirements.
 */
public record ProtocolVersion(int major, int minor) implements Encodable {

    private static final Pattern PROTOCOL_VERSION_MESSAGE = Pattern.compile("RFB (\\d{3})\\.(\\d{3})");
    /**
     * Encodes the protocol version and writes it to the specified output stream.
     *
     * @param out The output stream to write the encoded version to.
     * @throws IOException If an I/O error occurs while writing to the stream.
     */
    @Override
    public void encode(OutputStream out) throws IOException {
        out.write(format("RFB %03d.%03d\n", major, minor).getBytes(StandardCharsets.US_ASCII));
    }

    /**
     * Checks if the protocol version is equal to the specified major and minor versions.
     *
     * @param major The major version to compare.
     * @param minor The minor version to compare.
     * @return {@code true} if the protocol version is equal to the specified versions, {@code false} otherwise.
     */
    public boolean equals(int major, int minor) {
        return this.major == major && this.minor == minor;
    }

    /**
     * Checks if the protocol version is at least the specified major and minor versions.
     *
     * @param major The major version to compare.
     * @param minor The minor version to compare.
     * @return {@code true} if the protocol version is at least the specified versions, {@code false} otherwise.
     */
    public boolean atLeast(int major, int minor) {
        return this.major >= major && this.minor >= minor;
    }

    /**
     * Decodes a protocol version from the specified input stream.
     *
     * @param in The input stream to read the protocol version from.
     * @return The decoded protocol version.
     * @throws BaseVncException If a VNC-related exception occurs during decoding.
     * @throws IOException      If an I/O error occurs while reading from the stream.
     * @throws InvalidMessageException If the protocol version message is invalid.
     */
    public static ProtocolVersion decode(InputStream in) throws BaseVncException, IOException, InvalidMessageException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String message = br.readLine();
        Matcher matcher = PROTOCOL_VERSION_MESSAGE.matcher(message);
        if (matcher.matches()) {
            String major = matcher.group(1);
            String minor = matcher.group(2);
            return new ProtocolVersion(parseInt(major), parseInt(minor));
        } else {
            throw new InvalidMessageException("ProtocolVersion");
        }
    }

}
