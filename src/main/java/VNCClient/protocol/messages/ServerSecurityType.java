package VNCClient.protocol.messages;

import VNCClient.client.exceptions.HandshakingFailedException;
import VNCClient.client.exceptions.NoSupportedSecurityTypesException;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import static VNCClient.protocol.messages.SecurityType.resolve;

/**
 * Represents the security type of the VNC server.
 */
public class ServerSecurityType {

    private final SecurityType securityType;

    /**
     * Constructs a new ServerSecurityType object with the specified security type.
     *
     * @param securityType The security type of the VNC server.
     */
    private ServerSecurityType(SecurityType securityType) {
        this.securityType = securityType;
    }

    /**
     * Returns the security type of the VNC server.
     *
     * @return The security type.
     */
    public SecurityType getSecurityType() {
        return securityType;
    }

    /**
     * Decodes the server security type from the input stream.
     *
     * @param in The input stream to read from.
     * @return The decoded ServerSecurityType object.
     * @throws HandshakingFailedException      If the handshaking with the server fails.
     * @throws NoSupportedSecurityTypesException If no supported security types are found.
     * @throws IOException                     If an I/O error occurs while reading from the input stream.
     */
    public static ServerSecurityType decode(InputStream in) throws HandshakingFailedException, NoSupportedSecurityTypesException, IOException {
        DataInputStream dataInput = new DataInputStream(in);
        int type = dataInput.readInt();

        if (type == 0) {
            ErrorMessage errorMessage = ErrorMessage.decode(in);
            throw new HandshakingFailedException(errorMessage.getMessage());
        }

        return resolve(type).map(ServerSecurityType::new).orElseThrow(NoSupportedSecurityTypesException::new);
    }
}
