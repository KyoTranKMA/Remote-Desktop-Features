package VNCClient.client;


import VNCClient.client.exceptions.BaseVncException;
import VNCClient.client.exceptions.UnexpectedVncException;
import VNCClient.protocol.messages.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.Collections.synchronizedList;
import static java.util.stream.IntStream.range;

/**
 * This class represents the event handler for the VNC client.
 * It handles various client events such as mouse movement, mouse button clicks,
 * keyboard input, and clipboard operations.
 */
public class ClientEventHandler {

    private final VNCSession session;
    private final Consumer<BaseVncException> errorHandler;
    private final List<Boolean> buttons = synchronizedList(new ArrayList<>());
    private final ReentrantLock outputLock = new ReentrantLock(true);
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private volatile boolean running;
    private int mouseX;
    private int mouseY;

    private LocalDateTime lastFramebufferUpdateRequestTime;

    /**
     * Constructs a new ClientEventHandler with the given VncSession and error handler.
     *
     * @param session      the VncSession associated with the client
     * @param errorHandler the error handler to handle any exceptions
     */
    ClientEventHandler(VNCSession session, Consumer<BaseVncException> errorHandler) {
        this.session = session;
        this.errorHandler = errorHandler;
        range(0, 8).forEach(i -> buttons.add(false));
    }

    /**
     * Starts the event handler by creating a new thread for handling framebuffer updates.
     */
    public void start() {
        running = true;
        scheduler.scheduleAtFixedRate(this::framebufferUpdateLoop, 0, 1000 / session.getConfig().getTargetFramesPerSecond(), TimeUnit.MILLISECONDS);
    }

    /**
     * Stops the event handler by shutting down the scheduler.
     */
    public void stop() {
        running = false;
        scheduler.shutdownNow();
    }

    /**
     * Updates the status of a mouse button.
     *
     * @param button  the button number (1-8)
     * @param pressed true if the button is pressed, false otherwise
     * @throws IOException if an I/O error occurs while sending the mouse button event
     */
    public void updateMouseButton(int button, boolean pressed) throws IOException {
        buttons.set(button - 1, pressed);
        updateMouseStatus();
    }

    /**
     * Moves the mouse to the specified coordinates.
     *
     * @param mouseX the x-coordinate of the mouse
     * @param mouseY the y-coordinate of the mouse
     * @throws IOException if an I/O error occurs while sending the mouse movement event
     */
    public void moveMouse(int mouseX, int mouseY) throws IOException {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        updateMouseStatus();
    }

    /**
     * Updates the status of a key.
     *
     * @param keySym  the key symbol
     * @param pressed true if the key is pressed, false otherwise
     * @throws IOException if an I/O error occurs while sending the key event
     */
    public void updateKey(int keySym, boolean pressed) throws IOException {
        KeyEvent message = new KeyEvent(keySym, pressed);
        sendMessage(message);
    }

    /**
     * Sends the client cut text capabilities to the server.
     *
     * @throws IOException if an I/O error occurs while sending the client cut text capabilities
     */
    public void sendClientCutTextCaps() throws IOException {
        ClientCutTextCaps clientCutTextCaps = new ClientCutTextCaps(session.getConfig().getMaxSizePerFormat());
        sendMessage(clientCutTextCaps);
    }

    /**
     * Copies the specified text to the clipboard.
     *
     * @param text the text to be copied
     * @throws IOException if an I/O error occurs while sending the clipboard event
     */
    public void copyText(String text) throws IOException {
        if (session.getConfig().isEnableExtendedClipboard()) {
            sendClientCutTextCaps();
            ClientCutTextExtendedClipboard clientCutTextExtendedClipboard = new ClientCutTextExtendedClipboard(text);
            sendMessage(clientCutTextExtendedClipboard);
        } else {
            ClientCutText message = new ClientCutText(text);
            sendMessage(message);
        }
    }

    /**
     * This method is responsible for continuously updating the framebuffer of the VNC client.
     * It checks if the client is running and if it's time for a framebuffer update.
     * If so, it requests a framebuffer update and waits for the update to be received from the VNC server.
     * If any IO or InterruptedException occurs during the process, it handles the exception by
     * creating an UnexpectedBaseVncException and passing it to the errorHandler.
     */
    private void framebufferUpdateLoop() {
        try {
            if (running && timeForFramebufferUpdate()) {
                requestFramebufferUpdate();
                session.waitForFramebufferUpdate();
            }
        } catch (IOException | InterruptedException e) {
            if (running) {
                errorHandler.accept(new UnexpectedVncException(e));
            }
        }
    }

    /**
     * Updates the mouse status and sends a pointer event message.
     *
     * @throws IOException if an I/O error occurs while sending the message
     */
    private void updateMouseStatus() throws IOException {
        PointerEvent message = new PointerEvent(mouseX, mouseY, buttons);
        sendMessage(message);
    }

    /**
     * Checks if it is time to request a framebuffer update based on the target frames per second.
     *
     * @return true if it is time for a framebuffer update, false otherwise.
     */
    private boolean timeForFramebufferUpdate() {
        long updateInterval = 1000 / session.getConfig().getTargetFramesPerSecond();
        return lastFramebufferUpdateRequestTime == null || now().isAfter(lastFramebufferUpdateRequestTime.plus(updateInterval, MILLIS));
    }

    /**
     * Requests a framebuffer update from the VNC server.
     * <p>
     * This method sends a framebuffer update request to the VNC server, specifying the dimensions of the framebuffer
     * to be updated. It also updates the timestamp of the last framebuffer update request.
     *
     * @throws IOException if an I/O error occurs while sending the update request.
     */
    private void requestFramebufferUpdate() throws IOException {
        int width = session.getFramebufferWidth();
        int height = session.getFramebufferHeight();
        FramebufferUpdateRequest updateRequest = new FramebufferUpdateRequest(true, 0, 0, width, height);
        sendMessage(updateRequest);
        lastFramebufferUpdateRequestTime = now();
    }

    /**
     * Sends an encoded message to the server.
     *
     * @param message the message to be sent
     * @throws IOException if an I/O error occurs while sending the message
     */
    private void sendMessage(Encodable message) throws IOException {
        outputLock.lock();
        try {
            message.encode(session.getOutputStream());
        } finally {
            outputLock.unlock();
        }
    }
}
