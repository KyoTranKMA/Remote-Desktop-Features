package VNCClient.VNCClientModule.model;

public class HistoryLoginModel {
    private String ipAddress;
    private String username;


    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    public String getIpAddress() {
        return this.ipAddress;
    }
}
