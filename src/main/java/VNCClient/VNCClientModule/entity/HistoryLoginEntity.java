package VNCClient.VNCClientModule.entity;

public class HistoryLoginEntity
{
    private String username;
    private String ip_address;


    public void setIpAddress(String ipAddress) {
        this.ip_address = ipAddress;
    }
    public String getIpAddress() {
        return ip_address;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

}
