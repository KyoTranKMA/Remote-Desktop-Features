package VNCClient.VNCClientModule.view;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class Signup extends JPanel {
    private static final Logger log = LogManager.getLogger(Signup.class);
    private MainFrame mainFrame;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private SignupHandler signupHandler;

    public Signup(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        Dimension panelSize = new Dimension(500, 500);

        // Create the panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setPreferredSize(panelSize);

        GradientLabel titleLabel = new GradientLabel("Sign Up", new Color(0, 0, 255), new Color(0,255,255));
        titleLabel.setFont(FontProvider.TITLE_FONT);
        titleLabel.setForeground(FontProvider.TITLE_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));

//        // Create the components
//        TitledBorder titleLabel = BorderFactory.createTitledBorder("Sign Up");
//        titleLabel.setTitleFont(FontProvider.TITLE_FONT);
//        titleLabel.setTitleJustification(TitledBorder.CENTER);
//        panel.setBorder(titleLabel);

        Dimension textPanelSize = new Dimension(300, 20);

        JLabel firstNameLabel = new JLabel("First name:");
        firstNameLabel.setFont(FontProvider.LABEL_FONT);
        firstNameLabel.setForeground(FontProvider.LABEL_COLOR);
        firstNameField = new JTextField(20);
        firstNameField.setFont(FontProvider.INPUT_FONT);
        firstNameField.setHorizontalAlignment(JTextField.CENTER);
        firstNameField.setForeground(FontProvider.INPUT_COLOR);
        firstNameField.setOpaque(false);
        firstNameField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));

        JLabel lastNameLabel = new JLabel("Last name:");
        lastNameLabel.setFont(FontProvider.LABEL_FONT);
        lastNameLabel.setForeground(FontProvider.LABEL_COLOR);
        lastNameField = new JTextField(20);
        lastNameField.setFont(FontProvider.INPUT_FONT);
        lastNameField.setHorizontalAlignment(JTextField.CENTER);
        lastNameField.setForeground(FontProvider.INPUT_COLOR);
        lastNameField.setOpaque(false);
        lastNameField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(FontProvider.LABEL_FONT);
        emailLabel.setForeground(FontProvider.LABEL_COLOR);
        emailField = new JTextField(20);
        emailField.setFont(FontProvider.INPUT_FONT);
        emailField.setHorizontalAlignment(JTextField.CENTER);
        emailField.setForeground(FontProvider.INPUT_COLOR);
        emailField.setOpaque(false);
        emailField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(FontProvider.LABEL_FONT);
        usernameLabel.setForeground(FontProvider.LABEL_COLOR);
        usernameField = new JTextField(20);
        usernameField.setFont(FontProvider.INPUT_FONT);
        usernameField.setHorizontalAlignment(JTextField.CENTER);
        usernameField.setForeground(FontProvider.INPUT_COLOR);
        usernameField.setOpaque(false);
        usernameField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(FontProvider.LABEL_FONT);
        passwordLabel.setForeground(FontProvider.LABEL_COLOR);
        passwordField = new JPasswordField(20);
        passwordField.setFont(FontProvider.INPUT_FONT);
        passwordField.setHorizontalAlignment(JTextField.CENTER);
        passwordField.setForeground(FontProvider.INPUT_COLOR);
        passwordField.setOpaque(false);
        passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));

        JLabel confirmPasswordLabel = new JLabel("Confirm password:");
        confirmPasswordLabel.setFont(FontProvider.LABEL_FONT);
        confirmPasswordLabel.setForeground(FontProvider.LABEL_COLOR);
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(FontProvider.INPUT_FONT);
        confirmPasswordField.setHorizontalAlignment(JTextField.CENTER);
        confirmPasswordField.setForeground(FontProvider.INPUT_COLOR);
        confirmPasswordField.setOpaque(false);
        confirmPasswordField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));

        JPanel firstNamePanel = new JPanel();
        firstNamePanel.setLayout(new BoxLayout(firstNamePanel, BoxLayout.LINE_AXIS));
        firstNamePanel.setOpaque(false);
        firstNamePanel.add(firstNameLabel);
        firstNamePanel.add(Box.createHorizontalStrut(10)); // Add some space between the label and the text field (10 pixels
        firstNamePanel.add(firstNameField);
        firstNamePanel.setPreferredSize(textPanelSize);
        panel.add(firstNamePanel);
        panel.add(Box.createVerticalStrut(20));

        JPanel lastNamePanel = new JPanel();
        lastNamePanel.setLayout(new BoxLayout(lastNamePanel, BoxLayout.LINE_AXIS));
        lastNamePanel.setOpaque(false);
        lastNamePanel.add(lastNameLabel);
        lastNamePanel.add(Box.createHorizontalStrut(10));
        lastNamePanel.add(lastNameField);
        lastNamePanel.setPreferredSize(textPanelSize);
        panel.add(lastNamePanel);
        panel.add(Box.createVerticalStrut(20));

        JPanel emailPanel = new JPanel();
        emailPanel.setLayout(new BoxLayout(emailPanel, BoxLayout.LINE_AXIS));
        emailPanel.setOpaque(false);
        emailPanel.add(emailLabel);
        emailPanel.add(Box.createHorizontalStrut(10));
        emailPanel.add(emailField);
        emailPanel.setPreferredSize(textPanelSize);
        panel.add(emailPanel);
        panel.add(Box.createVerticalStrut(20));

        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.LINE_AXIS));
        usernamePanel.setOpaque(false);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(Box.createHorizontalStrut(10));
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

        JPanel confirmPasswordPanel = new JPanel();
        confirmPasswordPanel.setLayout(new BoxLayout(confirmPasswordPanel, BoxLayout.LINE_AXIS));
        confirmPasswordPanel.setOpaque(false);
        confirmPasswordPanel.add(confirmPasswordLabel);
        confirmPasswordPanel.add(Box.createHorizontalStrut(10));
        confirmPasswordPanel.add(confirmPasswordField);
        confirmPasswordPanel.setPreferredSize(textPanelSize);
        panel.add(confirmPasswordPanel);
        panel.add(Box.createVerticalStrut(20));

        JButton signupButton = new JButton("Signup");
        signupButton.setFont(FontProvider.BUTTON_FONT);
        signupButton.setForeground(FontProvider.BUTTON_COLOR);
        signupButton.setContentAreaFilled(false);
        JLabel goLoginLabel = new JLabel("Already have an account? Click here to log in!");
        goLoginLabel.setForeground(FontProvider.LINK_LABEL_COLOR);
        goLoginLabel.setFont(FontProvider.LINK_LABEL_FONT);
        goLoginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel clickPanel = new JPanel();
        clickPanel.setLayout(new BoxLayout(clickPanel, BoxLayout.LINE_AXIS));
        clickPanel.setOpaque(false);
        clickPanel.add(signupButton);

        // Add a filler between the button and the label
        Dimension minSize = new Dimension(5, 100);
        Dimension prefSize = new Dimension(5, 100);
        Dimension maxSize = new Dimension(Short.MAX_VALUE, 100);
        clickPanel.add(new Box.Filler(minSize, prefSize, maxSize));

        clickPanel.add(goLoginLabel);
        panel.add(clickPanel);

        // Create the handler
        signupHandler = new SignupHandler(mainFrame, usernameField, passwordField, confirmPasswordField, firstNameField, lastNameField);

        // Add action listeners to the buttons
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signupHandler.handleSignupButtonClicked();
            }
        });

        goLoginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                signupHandler.handleLoginLabelClicked();
            }
        });

        // Add the panel to the Signup component
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