package VNCClient.VNCClientModule.service;

import java.net.SocketException;
import java.util.ArrayList;
public interface INetworkInter {
    public ArrayList<String> getAllIp() throws SocketException;
}
