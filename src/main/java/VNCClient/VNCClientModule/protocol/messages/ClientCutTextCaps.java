package VNCClient.VNCClientModule.protocol.messages;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Represents the ClientCutTextCaps message in the VNC protocol.
 * This message is used to advertise the client's capabilities for handling cut text.
 */
public class ClientCutTextCaps implements Encodable {

    private final Map<MessageHeaderFlags, Integer> sizes;

    /**
     * Constructs a new ClientCutTextCaps instance with the specified sizes.
     *
     * @param sizes a map containing the sizes for different message header flags
     */
    public ClientCutTextCaps(Map<MessageHeaderFlags, Integer> sizes) {
        this.sizes = sizes;
    }

    /**
     * Encodes the ClientCutTextCaps message and writes it to the specified output stream.
     *
     * @param out the output stream to write the encoded message to
     * @throws IOException if an I/O error occurs while writing the message
     */
    @Override
    public void encode(OutputStream out) throws IOException {
        int[] flags = new int[]{ MessageHeaderFlags.CAPS.code
                | MessageHeaderFlags.NOTIFY.code | MessageHeaderFlags.PEEK.code
                | MessageHeaderFlags.PROVIDE.code
                | MessageHeaderFlags.REQUEST.code
        };

        byte[] formatSizes = new byte[sizes.size() * 4];
        sizes.forEach((format, size) -> {
            flags[0] = flags[0] | format.code;
            int startByte = format.ordinal();
            byte[] sizeForCurrentFormat = ByteBuffer.allocate(4).putInt(size).array();
            System.arraycopy(sizeForCurrentFormat, 0, formatSizes, startByte, 4);
        });

        DataOutput dataOutput = new DataOutputStream(out);

        dataOutput.writeByte(0x06);
        dataOutput.write(new byte[3]);

        dataOutput.writeInt(formatSizes.length + 4);
        dataOutput.writeInt(flags[0]);
        dataOutput.write(formatSizes);
    }
}
