package VNCClient.VNCClientModule.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends Component {
    private JFrame frame;
    private JPanel cards; // This will hold the "cards" - the Login and Signup panels
    private Login loginPanel;
    private Signup signupPanel;

    public MainFrame() {
        frame = new JFrame("VNC Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the "cards"
        loginPanel = new Login(this);
        signupPanel = new Signup(this);

        // Create the panel that contains the "cards"
        cards = new JPanel(new CardLayout());
        cards.add(loginPanel, "Login");
        cards.add(signupPanel, "Signup");

        showCard("Login");

        frame.getContentPane().add(cards);

        frame.getContentPane().setPreferredSize(loginPanel.getPreferredSize());

        frame.pack();

        frame.setVisible(true);
    }

    // Method to switch between cards
    public void showCard(String card) {
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, card);

        // Adjust the size of the frame to match the preferred size of the current card
        if (card.equals("Login")) {
            frame.getContentPane().setPreferredSize(loginPanel.getPreferredSize());
        } else if (card.equals("Signup")) {
            frame.getContentPane().setPreferredSize(signupPanel.getPreferredSize());
        }

        frame.pack();
    }

    public void dispose() {
        frame.dispose();
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new MainFrame());
//    }
}