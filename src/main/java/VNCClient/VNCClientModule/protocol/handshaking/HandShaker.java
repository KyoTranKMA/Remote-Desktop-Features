package VNCClient.VNCClientModule.protocol.handshaking;

import VNCClient.VNCClientModule.client.VNCSession;
import VNCClient.VNCClientModule.client.exceptions.AuthenticationFailedException;
import VNCClient.VNCClientModule.client.exceptions.BaseVncException;
import VNCClient.VNCClientModule.protocol.auth.SecurityHandler;
import VNCClient.VNCClientModule.protocol.messages.SecurityResult;

import java.io.IOException;

public class HandShaker {

    private final ProtocolVersionNegotiator protocolVersionNegotiator;
    private final SecurityTypeNegotiator securityTypeNegotiator;

    public HandShaker() {
        this(new ProtocolVersionNegotiator(), new SecurityTypeNegotiator());
    }

    public HandShaker(ProtocolVersionNegotiator protocolVersionNegotiator, SecurityTypeNegotiator securityTypeNegotiator) {
        this.protocolVersionNegotiator = protocolVersionNegotiator;
        this.securityTypeNegotiator = securityTypeNegotiator;
    }

    /**
     * Performs the handshake process for a given VNC session.
     *
     * @param session the VNC session
     * @throws BaseVncException if a VNC-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public void handshake(VNCSession session) throws BaseVncException, IOException {
        protocolVersionNegotiator.negotiate(session);
        SecurityHandler securityHandler = securityTypeNegotiator.negotiate(session);
        SecurityResult securityResult = securityHandler.authenticate(session);

        if (!securityResult.isSuccess()) {
            throw new AuthenticationFailedException(securityResult.getErrorMessage());
        }
    }
}
