package VNCClient.VNCClientModule.model;

public class UserModel {
    private String username;
    private String password;
    private String first_name;
    private String last_name;


    public UserModel() {
    }
    public UserModel(String username, String password, String firstname, String lastname) {
        this.username = username;
        this.password = password;
        this.first_name = firstname;
        this.last_name = lastname;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getFirstname() {
        return first_name;
    }
    public String getLastname() {
        return last_name;
    }
    public void setUsername(String username) {
       if(username == null || username.isEmpty()) {
              throw new IllegalArgumentException("Username cannot be null or empty");
       }
        this.username = username;
    }
    public void setPassword(String password) {
        if(password == null || password.isEmpty()) {
              throw new IllegalArgumentException("Password cannot be null or empty");
       }
        this.password = password;
    }
    public void setFirstname(String firstname) {
        this.first_name = firstname;
    }
    public void setLastname(String lastname) {
        this.last_name = lastname;
    }

}
