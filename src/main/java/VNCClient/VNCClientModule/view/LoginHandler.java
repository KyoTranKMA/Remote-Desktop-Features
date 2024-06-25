package VNCClient.VNCClient.view;


import javax.swing.*;

public class LoginHandler {
    private MainFrame mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginHandler(MainFrame mainFrame, JTextField usernameField, JPasswordField passwordField) {
        this.mainFrame = mainFrame;
        this.usernameField = usernameField;
        this.passwordField = passwordField;
    }

    public void handleLoginButtonClicked() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Validate the user input and perform the login
        // This is just a placeholder - you should replace this with your actual login logic
        if (username.equals("admin") && password.equals("password")) {
            System.out.println("Login successful");
        } else {
            System.out.println("Login failed");
        }
    }

    public void handleGoSignupLabelClicked() {
        mainFrame.showCard("Signup");
    }
}
