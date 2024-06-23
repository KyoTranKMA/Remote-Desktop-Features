package VNCClient;

import VNCClient.view.VNCGUI;

import static javax.swing.SwingUtilities.invokeLater;

public class VNCApplication {
    public static void main(String[] args) {
        invokeLater(() -> {
            VNCGUI viewer = new VNCGUI();
            viewer.setVisible(true);
        });
    }
}
