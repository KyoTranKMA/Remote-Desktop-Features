package VNCClient.protocol.messages;

import java.util.Optional;

import static java.util.Arrays.stream;

/**
 * Represents the security types supported by the VNC client.
 */
public enum SecurityType {

    NONE(1),
    VNC(2),
    MS_LOGON_2(113);

    private final int code;

    /**
     * Constructs a new SecurityType with the specified code.
     *
     * @param code the code representing the security type
     */
    SecurityType(int code) {
        this.code = code;
    }

    /**
     * Resolves the SecurityType based on the given code.
     *
     * @param code the code representing the security type
     * @return an Optional containing the resolved SecurityType, or an empty Optional if no match is found
     */
    public static Optional<SecurityType> resolve(int code) {
        return stream(values()).filter(s -> s.code == code).findFirst();
    }

    /**
     * Returns the code representing the security type.
     *
     * @return the code representing the security type
     */
    public int getCode() {
        return code;
    }
}
