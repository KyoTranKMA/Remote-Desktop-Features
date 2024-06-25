package VNCClient.VNCClientModule.client;

import VNCClient.VNCClientModule.client.exceptions.BaseVncException;
import VNCClient.VNCClientModule.client.exceptions.UnexpectedVncException;
import VNCClient.VNCClientModule.protocol.handshaking.HandShaker;
import VNCClient.VNCClientModule.protocol.initialization.Initializer;
import VNCClient.VNCClientModule.utils.KeySyms;

import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static java.awt.event.KeyEvent.*;
import static java.util.stream.IntStream.range;

public class VNCClient {
    private final HandShaker handshaker;
    private final Initializer initializer;

    private final VNCConfig config;

    private VNCSession session;
    private ClientEventHandler clientEventHandler;
    private ServerEventHandler serverEventHandler;

    private volatile boolean running;

    /**
     * Constructs a new VNCClient object with the specified configuration.
     * 
     * @param config the VNCConfig object containing the client configuration
     */
    public VNCClient(VNCConfig config) {
        this.config = config;
        this.handshaker = new HandShaker();
        this.initializer = new Initializer();
    }


    /**
     * Starts the VNC client by establishing a connection to the specified host and port.
     *
     * @param host the host address to connect to
     * @param port the port number to connect to
     */
    public void start(String host, int port) {
        try {
            start(new Socket(host, port));
        } catch (IOException e) {
            handleError(new UnexpectedVncException(e));
        }
    }



    /**
     * Starts the VNC client by establishing a connection to the specified socket.
     *
     * @param socket the socket to connect to
     */
    public void start(Socket socket) {
        if (running) {
            throw new IllegalStateException("VNC Client is already running");
        }

        running = true;

        try {
            createSession(socket);
            clientEventHandler = new ClientEventHandler(session, this::handleError);
            serverEventHandler = new ServerEventHandler(session, this::handleError);

            serverEventHandler.start();
            clientEventHandler.start();
        } catch (IOException e) {
            handleError(new UnexpectedVncException(e));
        } catch (BaseVncException e) {
            handleError(e);
        }
    }


    /**
     * Stops the VNC client by closing the session and stopping the event handlers.
     */
    public void stop() {
        running = false;
        if (serverEventHandler != null) {
            serverEventHandler.stop();
        }
        if (clientEventHandler != null) {
            clientEventHandler.stop();
        }
        if (session != null) {
            session.kill();
        }
    }


    /**
     * Moves the mouse to the specified coordinates.
     *
     * @param x the x-coordinate of the mouse
     * @param y the y-coordinate of the mouse
     */
    public void moveMouse(int x, int y) {
        if (clientEventHandler != null) {
            try {
                clientEventHandler.moveMouse(x, y);
            } catch (IOException e) {
                handleError(new UnexpectedVncException(e));
            }
        }
    }


    /**
     * Updates the status of a mouse button.
     *
     * @param button  the button number (1-8)
     * @param pressed true if the button is pressed, false otherwise
     */
    public void updateMouseButton(int button, boolean pressed) {
        if (clientEventHandler != null) {
            try {
                clientEventHandler.updateMouseButton(button, pressed);
            } catch (IOException e) {
                handleError(new UnexpectedVncException(e));
            }
        }
    }


    /**
     * Clicks the specified mouse button.
     *
     * @param button the button number (1-8)
     */
    public void click(int button) {
        updateMouseButton(button, true);
        updateMouseButton(button, false);
    }


    /**
     * Scrolls the mouse wheel up.
     */
    public void scrollUp() {
        click(4);
    }


    /**
     * Scrolls the mouse wheel down.
     */
    public void scrollDown() {
        click(5);
    }


    /**
     * Handles the specified key event.
     *
     * @param event the key event to handle
     */
    public void handleKeyEvent(KeyEvent event) {
        KeySyms.forEvent(event).ifPresent(k -> {
            switch (event.getID()) {
                case KEY_PRESSED:
                case KEY_RELEASED:
                    updateKey(k, event.getID() == KEY_PRESSED);
                    break;
                case KEY_TYPED:
                    type(k);
                    break;
            }
        });
    }


    /**
     * Updates the status of a key.
     *
     * @param keySym  the key symbol
     * @param pressed true if the key is pressed, false otherwise
     */
    public void updateKey(int keySym, boolean pressed) {
        if (clientEventHandler != null) {
            try {
                clientEventHandler.updateKey(keySym, pressed);
            } catch (IOException e) {
                handleError(new UnexpectedVncException(e));
            }
        }
    }


    /**
     * Types the specified text.
     *
     * @param keySym the text to type
     */
    public void type(int keySym) {
        updateKey(keySym, true);
        updateKey(keySym, false);
    }


    /**
     * Types the specified text.
     *
     * @param text the text to type
     */
    public void type(String text) {
        text = text.replaceAll("\r\n", "\n").replaceAll("\r", "\n");
        range(0, text.length())
                .map(text::charAt)
                .map(c -> switch (c) {
                    case '\n' -> KeySyms.forKeyCode(VK_ENTER).get();
                    case '\t' -> KeySyms.forKeyCode(VK_TAB).get();
                    default -> c;
                })
                .forEach(this::type);
    }


    /**
     * Copies the specified text to the clipboard.
     *
     * @param text the text to copy
     */
    public void copyText(String text) {
        if (clientEventHandler != null) {
            try {
                clientEventHandler.copyText(text);
            } catch (IOException e) {
                handleError(new UnexpectedVncException(e));
            }
        }
    }


    /**
     * Returns true if the VNC client is running, false otherwise.
     *
     * @return true if the VNC client is running, false otherwise
     */
    public boolean isRunning() {
        return running;
    }



    /**
     * Creates a VNC session using the provided socket.
     *
     * @param socket the socket used for communication with the VNC server
     * @throws IOException       if an I/O error occurs while creating the session
     * @throws BaseVncException  if a VNC protocol error occurs during the session creation
     */
    private void createSession(Socket socket) throws IOException, BaseVncException {
        InputStream in = new BufferedInputStream(socket.getInputStream());
        OutputStream out = socket.getOutputStream();
        session = new VNCSession(config, in, out);
        System.out.println("Session:" + session);
        handshaker.handshake(session);
        initializer.initialise(session);
    }


    /**
     * Handles the specified exception by notifying the error listeners and stopping the client.
     *
     * @param exception the exception to handle
     */
    private void handleError(BaseVncException exception) {
        notifyErrorListeners(exception);
        stop();
    }


    /**
     * Notifies the error listeners of the specified exception.
     *
     * @param exception the exception to notify the listeners of
     */
    private void notifyErrorListeners(BaseVncException exception) {
        if (config.getErrorListener() != null) {
            config.getErrorListener().accept(exception);
        }
    }

}
