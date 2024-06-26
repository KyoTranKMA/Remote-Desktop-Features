package VNCClient.VNCClientModule.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MainFrame extends Component {
    private JFrame frame;
    private JPanel cards; // This will hold the "cards" - the Login and Signup panels
    private Login loginPanel;
    private Signup signupPanel;

    private Image backgroundImage;

    private int frameWidth = 1280;
    private int frameHeight = 720;

    public MainFrame() {
        frame = new JFrame("VNC Client");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        try {
            backgroundImage = ImageIO.read(new File("src/main/resources/background.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create the "cards"
        loginPanel = new Login(this);
        signupPanel = new Signup(this);

        // Create the panel that contains the "cards"
        cards = new JPanel(new CardLayout());
        cards.setOpaque(false);
        cards.add(loginPanel, "Login");
        cards.add(signupPanel, "Signup");

        JPanel contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int imageWidth = backgroundImage.getWidth(null);
                int imageHeight = backgroundImage.getHeight(null);
                int x= imageWidth/4;
                int y= imageHeight/4;
                int width = imageWidth/2;
                int height = imageHeight/2;

                g.drawImage(backgroundImage, 0, 0, frameWidth, frameHeight, x, y, x+width, y+height, null);
            }
        };

        contentPane.setOpaque(false);
        contentPane.setLayout(null);

        cards.setSize(frameWidth, frameHeight);

        contentPane.add(cards);

//        showCard("Login");

//        frame.getContentPane().add(contentPane);
//
//        frame.getContentPane().setPreferredSize(loginPanel.getPreferredSize());

        frame.setContentPane(contentPane);

        frame.setSize(frameWidth, frameHeight);
        cards.setLocation(frameWidth/5, frameHeight/4);

        frame.setVisible(true);
    }

    // Method to switch between cards
    public void showCard(String card) {
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, card);

        // Adjust the size of the frame to match the preferred size of the current card
        if (card.equals("Login")) {
            frame.getContentPane().setPreferredSize(loginPanel.getPreferredSize());
            cards.setLocation(frameWidth/5, frameHeight/4);
        } else if (card.equals("Signup")) {
            frame.getContentPane().setPreferredSize(signupPanel.getPreferredSize());
            cards.setLocation(frameWidth/5, frameHeight/7);
        }

        cards.revalidate();
        cards.repaint();
    }

    public void dispose() {
        frame.dispose();
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new MainFrame());
//    }
}