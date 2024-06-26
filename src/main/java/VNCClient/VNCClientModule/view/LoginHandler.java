package VNCClient.VNCClientModule.view;


import VNCClient.VNCClientModule.ClientModuleApplication;
import VNCClient.VNCClientModule.controller.UserController;
import VNCClient.VNCClientModule.dto.UserDto;

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

        UserController userController = UserController.getInstance();
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword(password);

        boolean loginSuccessful = userController.login(userDto);
        if (loginSuccessful) {
            System.out.println("Login successful");
            VNCGUI viewer = new VNCGUI(username);
            viewer.setVisible(true);
            mainFrame.dispose();
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Incorrect username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            System.out.println("Login failed");
        }


//        // Validate the user input and perform the login
//        // This is just a placeholder - you should replace this with your actual login logic
//        if (username.equals("admin") && password.equals("password")) {
//            System.out.println("Login successful");
//            VNCGUI viewer = new VNCGUI();
//            viewer.setVisible(true);
//            mainFrame.dispose();
//        } else {
//            System.out.println("Login failed");
//        }
    }

    public void handleGoSignupLabelClicked() {
        mainFrame.showCard("Signup");
    }
}
