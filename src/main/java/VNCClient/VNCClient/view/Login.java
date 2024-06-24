package VNCClient.VNCClient.view;


import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login extends JPanel {
    private MainFrame mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private LoginHandler loginHandler;

    public Login(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        // Create the panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));


        // Create the components
        TitledBorder titleLabel = BorderFactory.createTitledBorder("Log In");
        titleLabel.setTitleFont(FontProvider.TITLE_FONT);
        titleLabel.setTitleJustification(TitledBorder.CENTER);
        panel.setBorder(titleLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(FontProvider.LABEL_FONT);
        usernameField = new JTextField(20);
        usernameField.setFont(FontProvider.INPUT_FONT);
        usernameField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Times New Roman", Font.BOLD, 15));
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

        JButton loginButton = new JButton("Login");
        JLabel goSignupLabel = new JLabel("Haven't signed up yet? Click here to sign up!");
        goSignupLabel.setForeground(Color.BLUE);
        goSignupLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        goSignupLabel.setEnabled(true);

        // Add the components to the panel
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(goSignupLabel);

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
    }
}