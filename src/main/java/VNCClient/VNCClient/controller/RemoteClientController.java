package VNCClient.VNCClient.controller;

import VNCClient.VNCClient.service.INetworkInter;
import VNCClient.VNCClient.service.NetworkInter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RemoteClientController extends JFrame {
    private JComboBox<String> comboBox;
    private JList<String> listView;
    private DefaultListModel<String> listModel;

    public RemoteClientController() {
        setLayout(new FlowLayout());

        comboBox = new JComboBox<>();
        listView = new JList<>();
        listModel = new DefaultListModel<>();

        listView.setModel(listModel);

        add(comboBox);
        add(new JScrollPane(listView));

        ArrayList<String> ips;
        try {
            INetworkInter net = new NetworkInter();
            ips = net.getAllIp();
            for (String ip : ips) {
                comboBox.addItem(ip);
            }
        } catch (SocketException ex) {
            Logger.getLogger(RemoteClientController.class.getName()).log(Level.SEVERE, null, ex);
        }

        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle comboBox item selection
                String selectedIp = (String) comboBox.getSelectedItem();
                // Add selected IP to the list
                listModel.addElement(selectedIp);
            }
        });

        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new RemoteClientController();
            }
        });
    }
}