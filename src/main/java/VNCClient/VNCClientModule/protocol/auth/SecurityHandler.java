package VNCClient.VNCClientModule.protocol.auth;

import VNCClient.VNCClientModule.client.VNCSession;
import VNCClient.VNCClientModule.client.exceptions.BaseVncException;
import VNCClient.VNCClientModule.protocol.messages.SecurityResult;

import java.io.IOException;

public interface SecurityHandler {
    SecurityResult authenticate(VNCSession session) throws BaseVncException, IOException;
}
