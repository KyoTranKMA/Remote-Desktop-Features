package VNCClient.VNCClientModule.view;


import VNCClient.VNCClientModule.controller.UserController;
import VNCClient.VNCClientModule.dto.UserDto;
import VNCClient.VNCClientModule.model.UserModel;

import javax.swing.*;

public class SignupHandler {
    private MainFrame mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField firstNameField;
    private JTextField lastNameField;

    public SignupHandler(MainFrame mainFrame, JTextField usernameField, JPasswordField passwordField, JPasswordField confirmPasswordField, JTextField firstNameField, JTextField lastNameField) {
        this.mainFrame = mainFrame;
        this.usernameField = usernameField;
        this.passwordField = passwordField;
        this.confirmPasswordField = confirmPasswordField;
        this.firstNameField = firstNameField;
        this.lastNameField = lastNameField;
    }

    public void handleSignupButtonClicked() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String cfmPassword = new String(confirmPasswordField.getPassword());
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        UserController userController = UserController.getInstance();
        UserDto userDto = new UserDto();
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setUsername(username);
        userDto.setPassword(password);

        boolean isPasswordMatch = password.equals(cfmPassword);
        if (!isPasswordMatch) {
            JOptionPane.showMessageDialog(mainFrame, "Password does not match", "Signup Failed", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            boolean signupSuccessful = UserController.getInstance().addUser(userDto);
            if (signupSuccessful) {
                JOptionPane.showMessageDialog(mainFrame, "Signup successful", "Signup Success", JOptionPane.INFORMATION_MESSAGE);
                mainFrame.showCard("Login");
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Signup failed", "Signup Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void handleLoginLabelClicked() {
        mainFrame.showCard("Login");
    }
}