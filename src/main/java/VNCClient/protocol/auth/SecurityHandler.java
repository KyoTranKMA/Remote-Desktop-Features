package VNCClient.protocol.auth;

import VNCClient.client.VNCSession;
import VNCClient.client.exceptions.BaseVncException;
import VNCClient.protocol.messages.SecurityResult;

import java.io.IOException;

public interface SecurityHandler {
    SecurityResult authenticate(VNCSession session) throws BaseVncException, IOException;
}
