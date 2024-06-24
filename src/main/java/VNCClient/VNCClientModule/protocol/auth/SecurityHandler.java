package VNCClient.VNCClient.protocol.auth;

import VNCClient.VNCClient.client.VNCSession;
import VNCClient.VNCClient.client.exceptions.BaseVncException;
import VNCClient.VNCClient.protocol.messages.SecurityResult;

import java.io.IOException;

public interface SecurityHandler {
    SecurityResult authenticate(VNCSession session) throws BaseVncException, IOException;
}
