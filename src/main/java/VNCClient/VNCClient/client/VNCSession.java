package VNCClient.VNCClient.client;

import VNCClient.VNCClient.protocol.messages.PixelFormat;
import VNCClient.VNCClient.protocol.messages.ProtocolVersion;
import VNCClient.VNCClient.protocol.messages.ServerInit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents a VNC session, managing communication with a VNC server.
 */
public class VNCSession {

    private final VNCConfig config;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    private ProtocolVersion protocolVersion;
    private ServerInit serverInit;
    private PixelFormat pixelFormat;

    private volatile int framebufferWidth;
    private volatile int framebufferHeight;

    private volatile boolean receivedFramebufferUpdate = false;
    private final ReentrantLock framebufferUpdateLock = new ReentrantLock();
    private final Condition framebufferUpdatedCondition = framebufferUpdateLock.newCondition();

    /**
     * Constructs a new VNCSession with the specified configuration and I/O streams.
     *
     * @param config        the VNC configuration
     * @param inputStream   the input stream for receiving data
     * @param outputStream  the output stream for sending data
     */
    public VNCSession(VNCConfig config, InputStream inputStream, OutputStream outputStream) {
        this.config = config;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    /**
     * Returns the input stream for receiving data.
     *
     * @return the input stream
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * Returns the output stream for sending data.
     *
     * @return the output stream
     */
    public OutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * Returns the VNC configuration.
     *
     * @return the configuration
     */
    public VNCConfig getConfig() {
        return config;
    }

    /**
     * Returns the protocol version used in the session.
     *
     * @return the protocol version
     */
    public ProtocolVersion getProtocolVersion() {
        return protocolVersion;
    }

    /**
     * Sets the protocol version for the session.
     *
     * @param protocolVersion the protocol version to set
     */
    public void setProtocolVersion(ProtocolVersion protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    /**
     * Returns the server initialization data.
     *
     * @return the server initialization data
     */
    public ServerInit getServerInit() {
        return serverInit;
    }

    /**
     * Sets the server initialization data.
     *
     * @param serverInit the server initialization data to set
     */
    public void setServerInit(ServerInit serverInit) {
        this.serverInit = serverInit;
    }

    /**
     * Returns the pixel format used in the session.
     *
     * @return the pixel format
     */
    public PixelFormat getPixelFormat() {
        return pixelFormat;
    }

    /**
     * Sets the pixel format for the session.
     *
     * @param pixelFormat the pixel format to set
     */
    public void setPixelFormat(PixelFormat pixelFormat) {
        this.pixelFormat = pixelFormat;
    }

    /**
     * Returns the width of the framebuffer.
     *
     * @return the framebuffer width
     */
    public int getFramebufferWidth() {
        return framebufferWidth;
    }

    /**
     * Sets the width of the framebuffer.
     *
     * @param framebufferWidth the framebuffer width to set
     */
    public void setFramebufferWidth(int framebufferWidth) {
        this.framebufferWidth = framebufferWidth;
    }

    /**
     * Returns the height of the framebuffer.
     *
     * @return the framebuffer height
     */
    public int getFramebufferHeight() {
        return framebufferHeight;
    }

    /**
     * Sets the height of the framebuffer.
     *
     * @param framebufferHeight the framebuffer height to set
     */
    public void setFramebufferHeight(int framebufferHeight) {
        this.framebufferHeight = framebufferHeight;
    }

    /**
     * Blocks until a framebuffer update is received.
     *
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public void waitForFramebufferUpdate() throws InterruptedException {
        framebufferUpdateLock.lock();
        try {
            while (!receivedFramebufferUpdate) {
                framebufferUpdatedCondition.await();
            }
            receivedFramebufferUpdate = false;
        } finally {
            framebufferUpdateLock.unlock();
        }
    }

    /**
     * Signals that a framebuffer update has been received.
     */
    public void framebufferUpdated() {
        framebufferUpdateLock.lock();
        try {
            receivedFramebufferUpdate = true;
            framebufferUpdatedCondition.signalAll();
        } finally {
            framebufferUpdateLock.unlock();
        }
    }

    /**
     * Closes the input and output streams, terminating the session.
     */
    public void kill() {
        try {
            inputStream.close();
        } catch (IOException ignored) {
        } finally {
            try {
                outputStream.close();
            } catch (IOException ignored) {
            }
        }
    }
}
