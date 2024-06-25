package VNCClient.VNCClientModule.client.exceptions;

import static java.lang.String.format;

/**
 * Exception thrown when the server's protocol version is not supported by the client.
 */
public class UnsupportedProtocolVersionException extends BaseVncException {

    private final int serverMajor;
    private final int serverMinor;
    private final int requiredMajor;
    private final int requiredMinor;

    /**
     * Constructs a new UnsupportedProtocolVersionException with the specified server and required protocol versions.
     *
     * @param serverMajor the major version of the server's protocol
     * @param serverMinor the minor version of the server's protocol
     * @param minMajor    the minimum major version required by the client
     * @param minMinor    the minimum minor version required by the client
     */
    public UnsupportedProtocolVersionException(int serverMajor, int serverMinor, int minMajor, int minMinor) {
        super(format("The server supports protocol version %d.%d. We require version %d.%d", serverMajor, serverMinor, minMajor, minMinor));
        logger.error("The server supports protocol version {}.{}. We require version {}.{}", serverMajor, serverMinor, minMajor, minMinor);
        this.serverMajor = serverMajor;
        this.serverMinor = serverMinor;
        this.requiredMajor = minMajor;
        this.requiredMinor = minMinor;
    }

    /**
     * Returns the major version of the server's protocol.
     *
     * @return the major version of the server's protocol
     */
    public int getServerMajor() {
        return serverMajor;
    }

    /**
     * Returns the minor version of the server's protocol.
     *
     * @return the minor version of the server's protocol
     */
    public int getServerMinor() {
        return serverMinor;
    }

    /**
     * Returns the minimum major version required by the client.
     *
     * @return the minimum major version required by the client
     */
    public int getRequiredMajor() {
        return requiredMajor;
    }

    /**
     * Returns the minimum minor version required by the client.
     *
     * @return the minimum minor version required by the client
     */
    public int getRequiredMinor() {
        return requiredMinor;
    }
}
