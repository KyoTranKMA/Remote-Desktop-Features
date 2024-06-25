package VNCClient.VNCClientModule.protocol.handshaking;

import VNCClient.VNCClientModule.client.VNCSession;
import VNCClient.VNCClientModule.client.exceptions.NoSupportedSecurityTypesException;
import VNCClient.VNCClientModule.client.exceptions.BaseVncException;
import VNCClient.VNCClientModule.protocol.auth.MsLogon2AuthenticationHandler;
import VNCClient.VNCClientModule.protocol.auth.NoSecurityHandler;
import VNCClient.VNCClientModule.protocol.auth.SecurityHandler;
import VNCClient.VNCClientModule.protocol.auth.VncAuthenticationHandler;
import VNCClient.VNCClientModule.protocol.messages.SecurityType;
import VNCClient.VNCClientModule.protocol.messages.ServerSecurityType;
import VNCClient.VNCClientModule.protocol.messages.ServerSecurityTypes;

import java.io.IOException;
import java.util.List;

import static java.util.Collections.singletonList;

public class SecurityTypeNegotiator {

    /**
     * Negotiates the security type with the VNC server.
     *
     * @param session the VNC session
     * @return the security handler for the negotiated security type
     * @throws IOException if an I/O error occurs
     * @throws BaseVncException if a VNC-specific error occurs
     */
    public SecurityHandler negotiate(VNCSession session) throws IOException, BaseVncException {
        List<SecurityType> securityTypes;

        if (session.getProtocolVersion().equals(3, 3)) {
            ServerSecurityType serverSecurityType = ServerSecurityType.decode(session.getInputStream());
            securityTypes = singletonList(serverSecurityType.getSecurityType());
        } else {
            ServerSecurityTypes serverSecurityTypes = ServerSecurityTypes.decode(session.getInputStream());
            securityTypes = serverSecurityTypes.getSecurityTypes();
        }

        return resolve(securityTypes);
    }

    private static SecurityHandler resolve(List<SecurityType> securityTypes) throws BaseVncException {
        for (SecurityType securityType : securityTypes) {
            switch (securityType) {
                case NONE:
                    return new NoSecurityHandler();
                case VNC:
                    return new VncAuthenticationHandler();
                case MS_LOGON_2:
                    return new MsLogon2AuthenticationHandler();
                default:
                    break;
            }
        }
        throw new NoSupportedSecurityTypesException();
    }
}
