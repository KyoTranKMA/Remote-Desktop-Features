package VNCClient.VNCClientModule;

import VNCClient.VNCClientModule.view.VNCGUI;

import static javax.swing.SwingUtilities.invokeLater;

public class ClientModuleApplication {
    public static void main(String[] args) {
        invokeLater(() -> {
            VNCGUI viewer = new VNCGUI();
            viewer.setVisible(true);
        });
    }
}
