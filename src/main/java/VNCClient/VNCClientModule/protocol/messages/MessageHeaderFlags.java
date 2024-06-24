package VNCClient.VNCClient.protocol.messages;

/**
 * The MessageHeaderFlags enum represents the flags used in the message header.
 * These flags indicate the type of message being sent or received.
 */
public enum MessageHeaderFlags {

    TEXT(1),
    RTF(1 << 1),
    HTML(1 << 2),
    DIB(1 << 3),
    FILES(1 << 4),
    CAPS(1 << 24),
    REQUEST(1 << 25),
    PEEK(1 << 26),
    NOTIFY(1 << 27),
    PROVIDE(1 << 28);

    final int code;

    /**
     * Constructs a MessageHeaderFlags enum constant with the specified code.
     *
     * @param code the code representing the flag
     */
    MessageHeaderFlags(int code) {
        this.code = code;
    }

    /**
     * Returns the code associated with the flag.
     *
     * @return the code of the flag
     */
    public int getCode() {
        return code;
    }
}
