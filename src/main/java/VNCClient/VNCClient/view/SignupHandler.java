package VNCClient.VNCClient.view;


import javax.swing.*;

public class SignupHandler {
    private MainFrame mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public SignupHandler(MainFrame mainFrame, JTextField usernameField, JPasswordField passwordField) {
        this.mainFrame = mainFrame;
        this.usernameField = usernameField;
        this.passwordField = passwordField;
    }

    public void handleSignupButtonClicked() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Validate the user input and perform the signup
        // This is just a placeholder - you should replace this with your actual signup logic
        if (username.length() > 0 && password.length() > 0) {
            System.out.println("Signup successful");
        } else {
            System.out.println("Signup failed");
        }
    }

    public void handleLoginLabelClicked() {
        mainFrame.showCard("Login");
    }
}