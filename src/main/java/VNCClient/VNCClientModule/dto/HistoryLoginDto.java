package VNCClient.VNCClientModule.dto;

public class HistoryLoginDto {
    private String ipAddress;
    private String username;


    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
    public void setIpAddess(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    public String getIpAddress() {
        return this.ipAddress;
    }

    @Override
    public String toString() {
        return "HistoryLoginDto{" +
                "ipAddress='" + ipAddress + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
