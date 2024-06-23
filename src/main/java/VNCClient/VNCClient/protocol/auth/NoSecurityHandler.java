package VNCClient.VNCClient.protocol.auth;

import VNCClient.VNCClient.client.VNCSession;
import VNCClient.VNCClient.protocol.messages.ProtocolVersion;
import VNCClient.VNCClient.protocol.messages.SecurityResult;

import java.io.DataOutputStream;
import java.io.IOException;

import static VNCClient.VNCClient.protocol.messages.SecurityType.NONE;

/**
 * The NoSecurityHandler class handles the "No Security" authentication mechanism for a VNC session.
 * It implements the SecurityHandler interface.
 */
public class NoSecurityHandler implements SecurityHandler {

    /**
     * Authenticates the VNC session using the "No Security" authentication mechanism.
     *
     * @param session the VNC session to authenticate.
     * @return a SecurityResult object containing the authentication result.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public SecurityResult authenticate(VNCSession session) throws IOException {
        ProtocolVersion protocolVersion = session.getProtocolVersion();
        if (!protocolVersion.equals(3, 3)) {
            new DataOutputStream(session.getOutputStream()).writeByte(NONE.getCode());
        }
        if (protocolVersion.equals(3, 8)) {
            return SecurityResult.decode(session.getInputStream(), session.getProtocolVersion());
        } else {
            return new SecurityResult(true);
        }
    }
}
