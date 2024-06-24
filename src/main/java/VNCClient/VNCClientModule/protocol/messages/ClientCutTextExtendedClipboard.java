package VNCClient.VNCClient.protocol.messages;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.zip.Deflater;

import static VNCClient.VNCClient.protocol.messages.MessageHeaderFlags.PROVIDE;
import static VNCClient.VNCClient.protocol.messages.MessageHeaderFlags.TEXT;


/**
 * Represents a message for sending extended clipboard text from the client to the server.
 */
public record ClientCutTextExtendedClipboard(String text) implements Encodable {

    /**
     * Encodes the message and writes it to the provided output stream.
     *
     * @param out The output stream to write the encoded message to.
     * @throws IOException If an I/O error occurs while writing the message.
     */
    @Override
    public void encode(OutputStream out) throws IOException {
        int flags = PROVIDE.code | TEXT.code;

        byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
        byte[] testLengthInBytes = ByteBuffer.allocate(4).putInt(textBytes.length + 1).array();
        byte[] input = new byte[4 + textBytes.length + 1];

        System.arraycopy(testLengthInBytes, 0, input, 0, testLengthInBytes.length);
        System.arraycopy(textBytes, 0, input, testLengthInBytes.length, textBytes.length);

        byte[] output = new byte[20 * 1024 * 1024];

        Deflater compressor = new Deflater();
        compressor.setInput(input);
        compressor.finish();
        int compressedDataLength = compressor.deflate(output);

        DataOutput dataOutput = new DataOutputStream(out);

        dataOutput.writeByte(0x06);
        dataOutput.write(new byte[3]);

        dataOutput.writeInt(-(compressedDataLength + 4));
        dataOutput.writeInt(flags);

        byte[] result = Arrays.copyOfRange(output, 0, compressedDataLength);

        dataOutput.write(result);
    }
}
