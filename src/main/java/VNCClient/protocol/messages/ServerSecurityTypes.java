package VNCClient.protocol.messages;

import VNCClient.client.exceptions.HandshakingFailedException;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static VNCClient.protocol.messages.SecurityType.resolve;

public class ServerSecurityTypes {

    private final List<SecurityType> securityTypes;

    /**
     * Constructs a ServerSecurityTypes object with the specified security types.
     *
     * @param securityTypes The list of security types.
     */
    private ServerSecurityTypes(List<SecurityType> securityTypes) {
        this.securityTypes = securityTypes;
    }

    /**
     * Returns the list of security types.
     *
     * @return The list of security types.
     */
    public List<SecurityType> getSecurityTypes() {
        return securityTypes;
    }

    /**
     * Decodes the server security types from the input stream.
     *
     * @param in The input stream to read from.
     * @return A ServerSecurityTypes object containing the decoded security types.
     * @throws HandshakingFailedException If the handshaking process fails.
     * @throws IOException If an I/O error occurs while reading from the input stream.
     */
    public static ServerSecurityTypes decode(InputStream in) throws HandshakingFailedException, IOException {
        DataInputStream dataInput = new DataInputStream(in);
        byte typeCount = dataInput.readByte();

        if (typeCount == 0) {
            ErrorMessage errorMessage = ErrorMessage.decode(in);
            throw new HandshakingFailedException(errorMessage.getMessage());
        }

        List<SecurityType> types = new ArrayList<>();

        for (int i = 0; i < typeCount; i++) {
            byte type = dataInput.readByte();
            resolve(type).ifPresent(types::add);
        }

        return new ServerSecurityTypes(types);
    }
}
