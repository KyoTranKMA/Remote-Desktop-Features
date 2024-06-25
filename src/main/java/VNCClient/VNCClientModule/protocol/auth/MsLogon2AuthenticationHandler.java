package VNCClient.VNCClientModule.protocol.auth;

import VNCClient.VNCClientModule.client.VNCSession;
import VNCClient.VNCClientModule.client.exceptions.AuthenticationRequiredException;
import VNCClient.VNCClientModule.client.exceptions.UnexpectedVncException;
import VNCClient.VNCClientModule.client.exceptions.BaseVncException;
import VNCClient.VNCClientModule.protocol.messages.SecurityResult;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.function.Supplier;

import static VNCClient.VNCClientModule.protocol.messages.SecurityType.MS_LOGON_2;
import static VNCClient.VNCClientModule.utils.ByteUtils.*;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static javax.crypto.Cipher.ENCRYPT_MODE;

/**
 * The MsLogon2AuthenticationHandler class is responsible for handling the MS-Logon 2 authentication protocol in a VNC session.
 * It implements the SecurityHandler interface.
 */
public class MsLogon2AuthenticationHandler implements SecurityHandler {

    private static final long DH_KEY_MAX_BITS = 31;
    private static final long MAX_DH_KEY_VALUE = 1L << DH_KEY_MAX_BITS;

    private final Random random;

    /**
     * Constructs a new MsLogon2AuthenticationHandler object with a secure random number generator.
     *
     * @throws BaseVncException if an unexpected VNC exception occurs.
     */
    public MsLogon2AuthenticationHandler() throws BaseVncException {
        try {
            this.random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new UnexpectedVncException(e);
        }
    }

    /**
     * Constructs a new MsLogon2AuthenticationHandler object with the specified random number generator.
     *
     * @param random the random number generator to use.
     */
    MsLogon2AuthenticationHandler(Random random) {
        this.random = random;
    }

    /**
     * Authenticates the VNC session using the MS-Logon 2 authentication protocol.
     *
     * @param session the VNC session to authenticate.
     * @return a SecurityResult object containing the authentication result.
     * @throws BaseVncException          if an unexpected VNC exception occurs.
     * @throws IOException               if an I/O error occurs.
     * @throws AuthenticationRequiredException if the username or password supplier is null.
     */
    @Override
    public SecurityResult authenticate(VNCSession session) throws BaseVncException, IOException {
        Supplier<String> usernameSupplier = session.getConfig().getUsernameSupplier();
        Supplier<String> passwordSupplier = session.getConfig().getPasswordSupplier();

        if (usernameSupplier == null || passwordSupplier == null) {
            throw new AuthenticationRequiredException();
        }

        InputStream in = session.getInputStream();
        OutputStream out = session.getOutputStream();

        if (!session.getProtocolVersion().equals(3, 3)) {
            out.write(MS_LOGON_2.getCode());
        }

        try {
            byte[] sharedKey = dhKeyExchange(in, out);
            sendEncrypted(out, usernameSupplier.get(), 256, sharedKey);
            sendEncrypted(out, passwordSupplier.get(), 64, sharedKey);
            return SecurityResult.decode(in, session.getProtocolVersion());
        } catch (GeneralSecurityException e) {
            throw new UnexpectedVncException(e);
        }
    }

    /**
     * Performs the Diffie-Hellman key exchange with the server.
     *
     * @param in  the input stream to read the server's public key from.
     * @param out the output stream to write the client's public key to.
     * @return the shared key generated from the key exchange.
     * @throws IOException if an I/O error occurs.
     */
    private byte[] dhKeyExchange(InputStream in, OutputStream out) throws IOException {
        byte[] generatorBytes = new byte[8];
        byte[] modulusBytes = new byte[8];
        byte[] serverPublicKeyBytes = new byte[8];

        DataInput dataInput = new DataInputStream(in);
        dataInput.readFully(generatorBytes);
        dataInput.readFully(modulusBytes);
        dataInput.readFully(serverPublicKeyBytes);

        BigInteger generator = new BigInteger(1, generatorBytes);
        BigInteger modulus = new BigInteger(1, modulusBytes);
        BigInteger serverPublicKey = new BigInteger(1, serverPublicKeyBytes);

        BigInteger clientPrivateKey = BigInteger.valueOf(random.nextLong() % MAX_DH_KEY_VALUE);
        BigInteger clientPublicKey = generator.modPow(clientPrivateKey, modulus);

        BigInteger sharedKey = serverPublicKey.modPow(clientPrivateKey, modulus);

        out.write(padLeft(clientPublicKey.toByteArray(), 8));

        return padLeft(sharedKey.toByteArray(), 8);
    }

    /**
     * Sends an encrypted value to the server.
     *
     * @param out    the output stream to write the encrypted value to.
     * @param value  the value to encrypt and send.
     * @param length the length of the encrypted value.
     * @param key    the shared key used for encryption.
     * @throws GeneralSecurityException if a security exception occurs during encryption.
     * @throws IOException              if an I/O error occurs.
     */
    public void sendEncrypted(OutputStream out, String value, int length, byte[] key) throws GeneralSecurityException, IOException {
        byte[] bytes = padRight(value.getBytes(ISO_8859_1), length);
        byte[] encrypted = des(bytes, key);
        out.write(encrypted);
    }

    /**
     * Performs DES encryption on the given data using the specified key.
     *
     * @param data the data to encrypt.
     * @param key  the encryption key.
     * @return the encrypted data.
     * @throws GeneralSecurityException if a security exception occurs during encryption.
     */
    public byte[] des(byte[] data, byte[] key) throws GeneralSecurityException {
        Cipher des = Cipher.getInstance("DES/CBC/NoPadding");
        IvParameterSpec iv = new IvParameterSpec(key);
        SecretKeySpec keySpec = new SecretKeySpec(reverseBits(key), 0, key.length, "DES");
        des.init(ENCRYPT_MODE, keySpec, iv);
        return des.doFinal(data);
    }
}
