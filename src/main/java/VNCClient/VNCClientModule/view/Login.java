package VNCClient.VNCClientModule.view;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class Login extends JPanel {
    private MainFrame mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private LoginHandler loginHandler;

    public Login(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        Dimension panelSize = new Dimension(500, 290);

        // Create the panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setPreferredSize(panelSize);

//        // Create the components
//        TitledBorder titleLabel = BorderFactory.createTitledBorder("Log In");
//        titleLabel.setTitleFont(FontProvider.TITLE_FONT);
//        titleLabel.setTitleJustification(TitledBorder.CENTER);
//        panel.setBorder(titleLabel);

        GradientLabel titleLabel = new GradientLabel("Log In", Color.BLUE, Color.CYAN);
        titleLabel.setFont(FontProvider.TITLE_FONT);
//        titleLabel.setForeground(FontProvider.TITLE_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20)); // Add some space between the title and the text fields (20 pixels

        Dimension textPanelSize = new Dimension(300, 20);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(FontProvider.LABEL_FONT);
        usernameLabel.setForeground(FontProvider.LABEL_COLOR);
        usernameField = new JTextField(20);
        usernameField.setFont(FontProvider.INPUT_FONT);
        usernameField.setHorizontalAlignment(JTextField.CENTER);
        usernameField.setForeground(FontProvider.INPUT_COLOR);
        usernameField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
        usernameField.setOpaque(false);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(FontProvider.LABEL_FONT);
        passwordLabel.setForeground(FontProvider.LABEL_COLOR);
        passwordField = new JPasswordField(20);
        passwordField.setFont(FontProvider.INPUT_FONT);
        passwordField.setHorizontalAlignment(JTextField.CENTER);
        passwordField.setForeground(FontProvider.INPUT_COLOR);
        passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
        passwordField.setOpaque(false);

        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.LINE_AXIS));
        usernamePanel.setOpaque(false);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(Box.createHorizontalStrut(10)); // Add some space between the label and the text field (10 pixels
        usernamePanel.add(usernameField);
        usernamePanel.setPreferredSize(textPanelSize);
        panel.add(usernamePanel);
        panel.add(Box.createVerticalStrut(20));

        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.LINE_AXIS));
        passwordPanel.setOpaque(false);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(Box.createHorizontalStrut(10));
        passwordPanel.add(passwordField);
        passwordPanel.setPreferredSize(textPanelSize);
        panel.add(passwordPanel);
        panel.add(Box.createVerticalStrut(20));

        JButton loginButton = new JButton("Login");
        loginButton.setFont(FontProvider.BUTTON_FONT);
        loginButton.setForeground(FontProvider.BUTTON_COLOR);
        loginButton.setContentAreaFilled(false);
        JLabel goSignupLabel = new JLabel("Haven't signed up yet? Click here to sign up!");
        goSignupLabel.setForeground(FontProvider.LINK_LABEL_COLOR);
        goSignupLabel.setFont(FontProvider.LINK_LABEL_FONT);
        goSignupLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        goSignupLabel.setEnabled(true);

        JPanel clickPanel = new JPanel();
        clickPanel.setLayout(new BoxLayout(clickPanel, BoxLayout.LINE_AXIS));
        clickPanel.setOpaque(false);
        clickPanel.add(loginButton);

        // Add a filler between the button and the label
        Dimension minSize = new Dimension(5, 100);
        Dimension prefSize = new Dimension(5, 100);
        Dimension maxSize = new Dimension(Short.MAX_VALUE, 100);

        usernamePanel.add(new Box.Filler(minSize, prefSize, maxSize));
        passwordPanel.add(new Box.Filler(minSize, prefSize, maxSize));
        clickPanel.add(new Box.Filler(minSize, prefSize, maxSize));

        clickPanel.add(goSignupLabel);
        panel.add(clickPanel);

        // Add the components to the panel
//        panel.add(usernameLabel);
//        panel.add(usernameField);
//        panel.add(passwordLabel);
//        panel.add(passwordField);
//        panel.add(loginButton);
//        panel.add(goSignupLabel);

        // Create the handler
        loginHandler = new LoginHandler(mainFrame, usernameField, passwordField);

        // Add action listeners to the buttons
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginHandler.handleLoginButtonClicked();
            }
        });

        goSignupLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loginHandler.handleGoSignupLabelClicked();
            }
        });



        // Add the panel to the Login component
        this.add(panel);
        this.revalidate();
        this.repaint();
        this.setOpaque(false);
    }

    class RoundBorder extends AbstractBorder {
        private Color color;

        public RoundBorder(Color color) {
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawRoundRect(x, y, width - 1, height - 1, 25, 25);
        }
    }
}