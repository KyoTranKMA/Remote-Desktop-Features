package VNCClient.VNCClientModule.view;


import VNCClient.VNCClientModule.ClientModuleApplication;
import VNCClient.VNCClientModule.client.VNCClient;
import VNCClient.VNCClientModule.client.VNCConfig;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;

import static VNCClient.VNCClientModule.client.rendering.ColorDepth.*;
import static java.awt.BorderLayout.CENTER;
import static java.awt.Color.*;
import static java.awt.Cursor.getDefaultCursor;
import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;
import static java.awt.RenderingHints.KEY_INTERPOLATION;
import static java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR;
import static java.awt.Toolkit.getDefaultToolkit;
import static java.awt.datatransfer.DataFlavor.stringFlavor;
import static java.awt.event.KeyEvent.*;
import static java.lang.Integer.parseInt;
import static java.lang.Math.min;
import static java.lang.System.exit;
import static java.lang.Thread.sleep;
import static javax.swing.JOptionPane.*;

public class VNCGUI extends JFrame {

    private static final int DEFAULT_SCREEN_WIDTH = 800;
    private static final int DEFAULT_SCREEN_HEIGHT = 600;

    private VNCConfig config;
    private VNCClient client;

    private JMenuItem connectMenuItem;
    private JMenuItem disconnectMenuItem;
    private JMenuItem logoutMenuItem;

    private JMenuItem bpp8IndexedColorMenuItem;
    private JMenuItem bpp16TrueColorMenuItem;
    private JMenuItem bpp24TrueColorMenuItem;
    private JMenuItem localCursorMenuItem;
    private JMenuItem clipBoardCopyEnabled;

    private JMenu encodingsMenu;
    private JMenu colorDepthsMenu;
    private JMenu optionsMenu;
    private JMenuItem copyRectMenuItem;
    private JMenuItem rreMenuItem;
    private JMenuItem hextileMenuItem;
    private JMenuItem zlibMenuItem;

    private Image lastFrame;

    private final AncestorListener focusRequester = new AncestorListener() {
        @Override
        public void ancestorAdded(AncestorEvent event) {
            event.getComponent().requestFocusInWindow();
        }

        @Override
        public void ancestorRemoved(AncestorEvent event) {
        }

        @Override
        public void ancestorMoved(AncestorEvent event) {
        }
    };

    private volatile boolean shutdown = false;

    private final Thread clipboardMonitor = new Thread(() -> {
        Clipboard clipboard = getDefaultToolkit().getSystemClipboard();

        String lastText = null;
        while (!shutdown) {
            try {
                if (connected()) {
                    String text = (String) clipboard.getData(stringFlavor);
                    if (text != null && !text.equals(lastText)) {
                        client.copyText(text);
                        lastText = text;
                    }
                }
                sleep(100L);
            } catch (Exception ignored) {
            }
        }
    });

    public VNCGUI(String username) {
        setTitle("VNC Client - " + username);
        setIconImage(getDefaultToolkit().getImage(getClass().getResource("/icon.png")));

        setSize(DEFAULT_SCREEN_WIDTH, DEFAULT_SCREEN_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                disconnect();
                shutdown = true;
                try {
                    clipboardMonitor.join();
                } catch (InterruptedException ignored) {

                }
                super.windowClosing(event);
            }
        });

        addMenu(username);
        addMouseListeners();
        addKeyListener();
        addDrawingSurface();
        initialiseVNCClient();
        if (config.isEnableClipBoard()) {
            clipboardMonitor.start();
        }
    }

    private void resetUI() {
        setMenuState(false);
        setCursor(getDefaultCursor());
        setSize(DEFAULT_SCREEN_WIDTH, DEFAULT_SCREEN_HEIGHT);
        setLocationRelativeTo(null);
        lastFrame = null;
        repaint();
    }

    private void addKeyListener() {
        setFocusTraversalKeysEnabled(false);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (connected()) {
                    client.handleKeyEvent(e);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (connected()) {
                    client.handleKeyEvent(e);
                }
            }
        });
    }

    private void addMouseListeners() {
        getContentPane().addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                mouseMoved(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (connected()) {
                    client.moveMouse(scaleMouseX(e.getX()), scaleMouseY(e.getY()));
                }
            }
        });
        getContentPane().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (connected()) {
                    client.updateMouseButton(e.getButton(), true);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (connected()) {
                    client.updateMouseButton(e.getButton(), false);
                }
            }
        });
        getContentPane().addMouseWheelListener(e -> {
            if (connected()) {
                int notches = e.getWheelRotation();
                if (notches < 0) {
                    client.scrollUp();
                } else {
                    client.scrollDown();
                }
            }
        });
    }

    private void addDrawingSurface() {
        add(new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                int width = getContentPane().getWidth();
                int height = getContentPane().getHeight();

                if (lastFrame != null) {
                    g2.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BILINEAR);
                    g2.drawImage(lastFrame, 0, 0, width, height, null);
                } else {
                    String message = "No connection. Use \"Connections > Connect\" to connect to a VNC server.";
                    int messageWidth = g2.getFontMetrics().stringWidth(message);
                    g2.setColor(DARK_GRAY);
                    g2.fillRect(0, 0, width, height);
                    g2.setColor(LIGHT_GRAY);
                    g2.drawString(message, width / 2 - messageWidth / 2, height / 2);
                }
            }
        }, CENTER);
    }

    private void initialiseVNCClient() {
        config = new VNCConfig();
        config.setColorDepth(BPP_24_TRUE);
        config.setErrorListener(e -> {
            showMessageDialog(this, e.getMessage(), "Error", ERROR_MESSAGE);
            resetUI();
        });
        config.setUsernameSupplier(this::showUsernameDialog);
        config.setPasswordSupplier(this::showPasswordDialog);
        config.setScreenUpdateListener(this::renderFrame);
        config.setMousePointerUpdateListener((p, h) -> this.setCursor(getDefaultToolkit().createCustomCursor(p, h, "vnc")));
        config.setBellListener(v -> getDefaultToolkit().beep());
        config.setRemoteClipboardListener(t -> getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(t), null));
        config.setUseLocalMousePointer(localCursorMenuItem.isSelected());
        client = new VNCClient(config);
    }

    private void addMenu(String username) {
        JMenuBar menu = new JMenuBar();

        JMenu connectionMenu = new JMenu("Connection");
        connectionMenu.setMnemonic(VK_C);

        connectMenuItem = new JMenuItem("Connect");
        connectMenuItem.setMnemonic(VK_C);
        connectMenuItem.addActionListener(event -> showConnectDialog());

        disconnectMenuItem = new JMenuItem("Disconnect");
        disconnectMenuItem.setMnemonic(VK_D);
        disconnectMenuItem.setEnabled(false);
        disconnectMenuItem.addActionListener(event -> disconnect());

        logoutMenuItem = new JMenuItem("Logout");
        logoutMenuItem.addActionListener(event -> {
            ClientModuleApplication.logout(username);
            disconnect();
            dispose();
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });

        JMenuItem exit = new JMenuItem("Exit");
        exit.setMnemonic(VK_X);
        exit.addActionListener(event -> {
            disconnect();
            exit(0);
        });

        connectionMenu.add(connectMenuItem);
        connectionMenu.add(disconnectMenuItem);
        connectionMenu.add(logoutMenuItem);
        connectionMenu.add(exit);

        colorDepthsMenu = new JMenu("Colors");
        colorDepthsMenu.setMnemonic(VK_D);
        ButtonGroup colorDepths = new ButtonGroup();

        bpp8IndexedColorMenuItem = new JRadioButtonMenuItem("8-bit Indexed Color");
        bpp16TrueColorMenuItem = new JRadioButtonMenuItem("16-bit True Color");
        bpp24TrueColorMenuItem = new JRadioButtonMenuItem("24-bit True Color", true);

        colorDepths.add(bpp8IndexedColorMenuItem);
        colorDepths.add(bpp16TrueColorMenuItem);
        colorDepths.add(bpp24TrueColorMenuItem);

        colorDepthsMenu.add(bpp8IndexedColorMenuItem);
        colorDepthsMenu.add(bpp16TrueColorMenuItem);
        colorDepthsMenu.add(bpp24TrueColorMenuItem);

        /* Encoding types menu */
        encodingsMenu = new JMenu("Encodings");
        encodingsMenu.setMnemonic(VK_E);

        bpp8IndexedColorMenuItem.addActionListener(event -> config.setColorDepth(BPP_8_INDEXED));
        bpp16TrueColorMenuItem.addActionListener(event -> config.setColorDepth(BPP_16_TRUE));
        bpp24TrueColorMenuItem.addActionListener(event -> config.setColorDepth(BPP_24_TRUE));

        copyRectMenuItem = new JCheckBoxMenuItem("COPYRECT", true);
        copyRectMenuItem.addActionListener(event -> config.setEnableCopyrectEncoding(copyRectMenuItem.isSelected()));

        rreMenuItem = new JCheckBoxMenuItem("RRE", true);
        rreMenuItem.addActionListener(event -> config.setEnableRreEncoding(rreMenuItem.isSelected()));

        hextileMenuItem = new JCheckBoxMenuItem("HEXTILE", true);
        hextileMenuItem.addActionListener(event -> config.setEnableHextileEncoding(hextileMenuItem.isSelected()));

        zlibMenuItem = new JCheckBoxMenuItem("ZLIB", false);
        zlibMenuItem.addActionListener(event -> config.setEnableZLibEncoding(zlibMenuItem.isSelected()));

        encodingsMenu.add(copyRectMenuItem);
        encodingsMenu.add(rreMenuItem);
        encodingsMenu.add(hextileMenuItem);
        encodingsMenu.add(zlibMenuItem);


        /* More options menu */
        optionsMenu = new JMenu("Options");
        optionsMenu.setMnemonic(VK_O);

        localCursorMenuItem = new JCheckBoxMenuItem("Use Local Cursor", true);
        localCursorMenuItem.addActionListener(event -> config.setUseLocalMousePointer(localCursorMenuItem.isSelected()));

        clipBoardCopyEnabled = new JCheckBoxMenuItem("Enable Clipboard Copy", false);
        clipBoardCopyEnabled.addActionListener(event -> config.setEnableClipboardCopy(clipBoardCopyEnabled.isSelected()));

        optionsMenu.add(localCursorMenuItem);
        optionsMenu.add(clipBoardCopyEnabled);

        menu.add(connectionMenu);
        menu.add(colorDepthsMenu);
        menu.add(encodingsMenu);
        menu.add(optionsMenu);
        setJMenuBar(menu);
    }

    private void showConnectDialog() {
        JPanel connectDialog = new JPanel();
        JTextField hostField = new JTextField(20);
        hostField.addAncestorListener(focusRequester);
        JTextField portField = new JTextField("5900", 4);
        JLabel hostLabel = new JLabel("Host");
        hostLabel.setLabelFor(hostField);
        JLabel portLabel = new JLabel("Port");
        portLabel.setLabelFor(hostLabel);

        connectDialog.add(hostLabel);
        connectDialog.add(hostField);
        connectDialog.add(portLabel);
        connectDialog.add(portField);

        int choice = showConfirmDialog(this, connectDialog, "Connect", OK_CANCEL_OPTION);
        if (choice == OK_OPTION) {
            String host = hostField.getText();
            if (host == null || host.isEmpty()) {
                showMessageDialog(this, "Please enter a valid host", null, WARNING_MESSAGE);
                return;
            }
            int port;
            try {
                port = parseInt(portField.getText());
            } catch (NumberFormatException e) {
                showMessageDialog(this, "Please enter a valid port", null, WARNING_MESSAGE);
                return;
            }
            connect(host, port);
        }
    }

    private String showUsernameDialog() {
        String username = "";
        JPanel usernameDialog = new JPanel();
        JTextField usernameField = new JTextField(20);
        usernameField.addAncestorListener(focusRequester);
        usernameDialog.add(usernameField);
        int choice = showConfirmDialog(this, usernameDialog, "Enter Username", OK_CANCEL_OPTION);
        if (choice == OK_OPTION) {
            username = usernameField.getText();
        }
        return username;
    }

    private String showPasswordDialog() {
        String password = "";
        JPanel passwordDialog = new JPanel();
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.addAncestorListener(focusRequester);
        passwordDialog.add(passwordField);
        int choice = showConfirmDialog(this, passwordDialog, "Enter Password", OK_CANCEL_OPTION);
        if (choice == OK_OPTION) {
            password = new String(passwordField.getPassword());
        }
        return password;
    }

    private void connect(String host, int port) {
        setMenuState(true);
        lastFrame = null;
        client.start(host, port);
    }

    private void disconnect() {
        if (connected()) {
            client.stop();
        }
        resetUI();
    }

    private void setMenuState(boolean running) {
        if (running) {
            connectMenuItem.setEnabled(false);
            disconnectMenuItem.setEnabled(true);
            encodingsMenu.setEnabled(false);
            colorDepthsMenu.setEnabled(false);
            optionsMenu.setEnabled(false);
        } else {
            connectMenuItem.setEnabled(true);
            disconnectMenuItem.setEnabled(false);
            encodingsMenu.setEnabled(true);
            colorDepthsMenu.setEnabled(true);
            optionsMenu.setEnabled(true);
        }
    }

    private boolean connected() {
        return client != null && client.isRunning();
    }

    private void renderFrame(Image frame) {
        if (resizeRequired(frame)) {
            resizeWindow(frame);
        }
        lastFrame = frame;
        repaint();
    }

    private boolean resizeRequired(Image frame) {
        return lastFrame == null
                || lastFrame.getWidth(null) != frame.getWidth(null)
                || lastFrame.getHeight(null) != frame.getHeight(null);
    }

    private void resizeWindow(Image frame) {
        int remoteWidth = frame.getWidth(null);
        int remoteHeight = frame.getHeight(null);
        Rectangle screenSize = getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int paddingTop = getHeight() - getContentPane().getHeight();
        int paddingSides = getWidth() - getContentPane().getWidth();
        int maxWidth = (int) screenSize.getWidth() - paddingSides;
        int maxHeight = (int) screenSize.getHeight() - paddingTop;
        if (remoteWidth <= maxWidth && remoteHeight < maxHeight) {
            setWindowSize(remoteWidth, remoteHeight);
        } else {
            double scale = min((double) maxWidth / remoteWidth, (double) maxHeight / remoteHeight);
            int scaledWidth = (int) (remoteWidth * scale);
            int scaledHeight = (int) (remoteHeight * scale);
            setWindowSize(scaledWidth, scaledHeight);
        }
        setLocationRelativeTo(null);
    }

    private void setWindowSize(int width, int height) {
        getContentPane().setPreferredSize(new Dimension(width, height));
        pack();
    }

    private int scaleMouseX(int x) {
        if (lastFrame == null) {
            return x;
        }
        return (int) (x * ((double) lastFrame.getWidth(null) / getContentPane().getWidth()));
    }

    private int scaleMouseY(int y) {
        if (lastFrame == null) {
            return y;
        }
        return (int) (y * ((double) lastFrame.getHeight(null) / getContentPane().getHeight()));
    }
}
