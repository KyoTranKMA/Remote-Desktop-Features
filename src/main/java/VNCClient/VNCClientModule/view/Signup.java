package VNCClient.VNCClientModule.view;


import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Signup extends JPanel {
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

        // Create the panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create the components
        TitledBorder titleLabel = BorderFactory.createTitledBorder("Sign Up");
        titleLabel.setTitleFont(FontProvider.TITLE_FONT);
        titleLabel.setTitleJustification(TitledBorder.CENTER);
        panel.setBorder(titleLabel);

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setFont(FontProvider.LABEL_FONT);
        firstNameField = new JTextField(20);
        firstNameField.setFont(FontProvider.INPUT_FONT);
        firstNameField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setFont(FontProvider.LABEL_FONT);
        lastNameField = new JTextField(20);
        lastNameField.setFont(FontProvider.INPUT_FONT);
        lastNameField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(FontProvider.LABEL_FONT);
        emailField = new JTextField(20);
        emailField.setFont(FontProvider.INPUT_FONT);
        emailField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(FontProvider.LABEL_FONT);
        usernameField = new JTextField(20);
        usernameField.setFont(FontProvider.INPUT_FONT);
        usernameField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(FontProvider.LABEL_FONT);
        passwordField = new JPasswordField(20);
        passwordField.setFont(FontProvider.INPUT_FONT);
        passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(FontProvider.LABEL_FONT);
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(FontProvider.INPUT_FONT);
        confirmPasswordField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

        JButton signupButton = new JButton("Signup");
        JLabel goLoginLabel = new JLabel("Already have an account? Click here to log in!");
        goLoginLabel.setForeground(Color.BLUE);
        goLoginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add the components to the panel
        panel.add(firstNameLabel);
        panel.add(firstNameField);
        panel.add(lastNameLabel);
        panel.add(lastNameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(confirmPasswordLabel);
        panel.add(confirmPasswordField);
        panel.add(signupButton);
        panel.add(goLoginLabel);


        // Create the handler
        signupHandler = new SignupHandler(mainFrame, usernameField, passwordField);

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

        // Add the panel to the Login component
        this.add(panel);
        this.revalidate();
        this.repaint();
    }
}