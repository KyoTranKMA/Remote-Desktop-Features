package VNCClient.protocol.messages;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Represents a client cut text message in the VNC protocol.
 * This message is used to send text from the client to the server.
 */
public record ClientCutText(String text) implements Encodable {

    /**
     * Encodes the client cut text message and writes it to the output stream.
     *
     * @param out The output stream to write the encoded message to.
     * @throws IOException If an I/O error occurs while writing to the output stream.
     */
    @Override
    public void encode(OutputStream out) throws IOException {
        DataOutput dataOutput = new DataOutputStream(out);
        dataOutput.writeByte(0x06);
        dataOutput.write(new byte[3]);
        dataOutput.writeInt(text.length());
        dataOutput.write(text.getBytes(StandardCharsets.ISO_8859_1));
    }
}
