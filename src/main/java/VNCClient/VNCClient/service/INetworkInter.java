package VNCClient.VNCClient.service;

import java.net.SocketException;
import java.util.ArrayList;
public interface INetworkInter {
    public ArrayList<String> getAllIp() throws SocketException;
}
