package VNCClient.protocol.auth;

import VNCClient.client.VNCSession;
import VNCClient.client.exceptions.AuthenticationRequiredException;
import VNCClient.client.exceptions.UnexpectedVncException;
import VNCClient.client.exceptions.BaseVncException;
import VNCClient.protocol.messages.SecurityResult;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.function.Supplier;

import static VNCClient.protocol.messages.SecurityType.VNC;
import static VNCClient.utils.ByteUtils.reverseBits;
import static java.lang.System.arraycopy;
import static javax.crypto.Cipher.ENCRYPT_MODE;

public class VncAuthenticationHandler implements SecurityHandler {

    /**
     * Authenticates the VNC session using the VNC authentication method.
     *
     * @param session the VNC session to authenticate.
     * @return a SecurityResult object containing the authentication result.
     * @throws BaseVncException if an unexpected VNC exception occurs.
     * @throws IOException      if an I/O error occurs.
     */
    @Override
    public SecurityResult authenticate(VNCSession session) throws BaseVncException, IOException {
        Supplier<String> passwordSupplier = session.getConfig().getPasswordSupplier();

        if (passwordSupplier == null) {
            throw new AuthenticationRequiredException();
        }

        InputStream in = session.getInputStream();
        OutputStream out = session.getOutputStream();

        try {
            if (!session.getProtocolVersion().equals(3, 3)) {
                requestVncAuthentication(out);
            }
            byte[] challenge = readChallenge(in);
            sendResponse(out, challenge, passwordSupplier.get());
        } catch (GeneralSecurityException e) {
            throw new UnexpectedVncException(e);
        }

        return SecurityResult.decode(in, session.getProtocolVersion());
    }

    /**
     * Sends a request for VNC authentication to the server.
     *
     * @param out the output stream to write the request to.
     * @throws IOException if an I/O error occurs.
     */
    private static void requestVncAuthentication(OutputStream out) throws IOException {
        out.write(VNC.getCode());
    }

    /**
     * Reads the challenge bytes sent by the server.
     *
     * @param in the input stream to read the challenge from.
     * @return the challenge bytes received from the server.
     * @throws IOException if an I/O error occurs.
     */
    private static byte[] readChallenge(InputStream in) throws IOException {
        DataInputStream data = new DataInputStream(in);
        byte[] challenge = new byte[16];
        data.readFully(challenge);
        return challenge;
    }


    /**
     * Sends the response to the server after encrypting it using DES encryption.
     *
     * @param out      the output stream to write the encrypted response to.
     * @param challenge the challenge bytes received from the server.
     * @param password the password to use for encryption.
     * @throws IOException              if an I/O error occurs.
     * @throws GeneralSecurityException if a security exception occurs during encryption.
     */
    private static void sendResponse(OutputStream out, byte[] challenge, String password) throws IOException, GeneralSecurityException {
        Cipher des = Cipher.getInstance("DES/ECB/NoPadding");
        des.init(ENCRYPT_MODE, buildKey(password));
        out.write(des.doFinal(challenge));
    }

    /**
     * Builds a DES SecretKeySpec using the password for encryption.
     *
     * @param password the password to use for generating the encryption key.
     * @return a SecretKeySpec object representing the encryption key.
     */
    private static SecretKeySpec buildKey(String password) {
        byte[] key = keyBytes(password);
        return new SecretKeySpec(key, 0, key.length, "DES");
    }

    /**
     * Generates key bytes from the password.
     *
     * @param password the password to convert into key bytes.
     * @return the key bytes derived from the password.
     */
    private static byte[] keyBytes(String password) {
        byte[] passwordBytes = password.getBytes(StandardCharsets.US_ASCII);
        byte[] key = new byte[8];
        arraycopy(passwordBytes, 0, key, 0, Math.min(passwordBytes.length, key.length));
        return reverseBits(key);
    }
}
