package VNCClient.VNCClient.protocol.messages;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Represents a key event in the VNC protocol.
 */
public class KeyEvent implements Encodable {

    private final int keySym;
    private final boolean pressed;

    /**
     * Constructs a new KeyEvent object.
     *
     * @param keySym  the key symbol code
     * @param pressed true if the key is pressed, false if it is released
     */
    public KeyEvent(int keySym, boolean pressed) {
        this.keySym = keySym;
        this.pressed = pressed;
    }

    /**
     * Encodes the key event and writes it to the output stream.
     *
     * @param out the output stream to write the encoded key event to
     * @throws IOException if an I/O error occurs while writing to the stream
     */
    @Override
    public void encode(OutputStream out) throws IOException {
        DataOutput dataOutput = new DataOutputStream(out);
        dataOutput.writeByte(0x04);
        dataOutput.writeBoolean(pressed);
        dataOutput.write(new byte[]{0x00, 0x00});
        dataOutput.writeInt(keySym);
    }
}
