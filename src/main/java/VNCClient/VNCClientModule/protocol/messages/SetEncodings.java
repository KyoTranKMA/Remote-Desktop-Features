package VNCClient.VNCClientModule.protocol.messages;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * The SetEncodings class represents a message that sets the encodings for the VNC client.
 * It implements the Encodable interface, allowing it to be encoded and sent over the network.
 */
public class SetEncodings implements Encodable {

    private final List<Encoding> encodings;

    /**
     * Constructs a SetEncodings object with the specified encodings.
     *
     * @param encodings the list of encodings to be set
     */
    public SetEncodings(Encoding... encodings) {
        this.encodings = asList(encodings);
    }

    /**
     * Constructs a SetEncodings object with the specified encodings.
     *
     * @param encodings the list of encodings to be set
     */
    public SetEncodings(List<Encoding> encodings) {
        this.encodings = encodings;
    }

    /**
     * Encodes the SetEncodings object and writes it to the specified output stream.
     *
     * @param out the output stream to write the encoded data to
     * @throws IOException if an I/O error occurs while writing the data
     */
    @Override
    public void encode(OutputStream out) throws IOException {
        DataOutputStream dataOutput = new DataOutputStream(out);
        dataOutput.writeByte(0x02);
        dataOutput.writeByte(0x00);
        dataOutput.writeShort(encodings.size());
        for (Encoding encoding : encodings) {
            dataOutput.writeInt(encoding.getCode());
        }
    }
}
