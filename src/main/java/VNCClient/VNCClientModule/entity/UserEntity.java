package VNCClient.VNCClientModule.entity;

public class UserEntity {
    private int id;
    private String first_name;
    private String last_name;
    private String username;
    private String password;


    public void setFirstName(String firstName) {
        this.first_name = firstName;
    }
    public String getFirstName() {
        return first_name;
    }
    public void setLastName(String lastName) {
        this.last_name = lastName;
    }
    public String getLastName() {
        return last_name;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }



}
