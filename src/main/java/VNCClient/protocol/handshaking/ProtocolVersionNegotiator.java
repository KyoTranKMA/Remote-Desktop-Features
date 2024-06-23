package VNCClient.protocol.handshaking;

import VNCClient.client.VNCSession;
import VNCClient.client.exceptions.BaseVncException;
import VNCClient.client.exceptions.UnsupportedProtocolVersionException;
import VNCClient.protocol.messages.ProtocolVersion;

import java.io.IOException;

public class ProtocolVersionNegotiator {

    private static final int MAJOR_VERSION = 3;
    private static final int MIN_MINOR_VERSION = 3;
    private static final int MAX_MINOR_VERSION = 8;

    /**
     * Negotiates the protocol version with the VNC server.
     *
     * @param session the VNC session
     * @throws IOException if an I/O error occurs
     * @throws BaseVncException if a VNC-specific error occurs
     */
    public void negotiate(VNCSession session) throws IOException, BaseVncException {
        ProtocolVersion serverVersion = ProtocolVersion.decode(session.getInputStream());

        if (!serverVersion.atLeast(MAJOR_VERSION, MIN_MINOR_VERSION)) {
            throw new UnsupportedProtocolVersionException(
                    serverVersion.major(),
                    serverVersion.minor(),
                    MAJOR_VERSION,
                    MIN_MINOR_VERSION
            );
        }

        int selectedMinorVersion = Math.min(serverVersion.minor(), MAX_MINOR_VERSION);
        ProtocolVersion clientVersion = new ProtocolVersion(MAJOR_VERSION, selectedMinorVersion);
        session.setProtocolVersion(clientVersion);
        clientVersion.encode(session.getOutputStream());
    }
}
