package VNCClient.VNCClientModule.service;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;


public class NetworkInter implements INetworkInter{

    public static ArrayList<String> netips;

    @Override
    public ArrayList<String> getAllIp() throws SocketException {
        netips = new ArrayList<>();
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)) {
            displayInterfaceInformation(netint);
        }
        System.out.println("Network Interface: " + netips);
        return netips;
    }


    private void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
            for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                if(!inetAddress.toString().isEmpty() && !inetAddress.toString().contains(":")) {
                    netips.add(inetAddress.toString().replace('/',' ').trim());
                }

            }

    }
}
