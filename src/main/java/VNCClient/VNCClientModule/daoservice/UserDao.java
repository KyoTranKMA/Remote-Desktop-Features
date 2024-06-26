package VNCClient.VNCClientModule.daoservice;

import VNCClient.VNCClientModule.dbservice.IDataProvider;
import VNCClient.VNCClientModule.entity.UserEntity;
import VNCClient.VNCClientModule.view.VNCGUI;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDao {
    private static UserDao instance;
    IDataProvider dataProvider;

    public static UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }

    private UserDao() {
    }

    public void setDataProvider(IDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public List<UserEntity> loadEntityFromDB() {
        if (this.dataProvider == null) {
            throw new RuntimeException("DataProvider is null");
        }
        List<UserEntity> listUser = new ArrayList();
        try {
            final String sql = "Select * From users";
            this.dataProvider.open();
            ResultSet resultSet = this.dataProvider.executeQuery(sql);
            if (resultSet != null) {
                while (resultSet.next()) {
                    UserEntity entity = new UserEntity();
                    entity.setUsername(resultSet.getString("username"));
                    entity.setPassword(resultSet.getString("password"));
                    entity.setFirstName(resultSet.getString("first_name"));
                    entity.setLastName(resultSet.getString("last_name"));
                    listUser.add(entity);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listUser;
    }
    public boolean login(UserEntity entity) {
        if (this.dataProvider == null) {
            throw new RuntimeException("DataProvider is null");
        }
        try {
            final String sql = "Select * From users Where username = ? and password = ?";
            this.dataProvider.open();
            PreparedStatement preparedStatement = this.dataProvider.prepareStatement(sql);
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getPassword());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    System.out.println("Login success");
                    InetAddress inetAddress = InetAddress.getLocalHost();
                    String ipAddress = inetAddress.getHostAddress();
                    System.out.println("Local IP Address: " + ipAddress);
                    final String sql1 = "INSERT INTO login_history (username, ip_address) VALUES (?, ?)";
                    PreparedStatement preparedStatement1 = this.dataProvider.prepareStatement(sql1);
                    preparedStatement1.setString(1, entity.getUsername());
                    preparedStatement1.setString(2, ipAddress);
                    int result = preparedStatement1.executeUpdate();
                    if (result == 1) {
                        System.out.println("Add login history success");
                    } else {
                        System.out.println("Add login history failed");
                    }

//                    VNCGUI viewer = new VNCGUI(entity.getUsername());
//                    viewer.setVisible(true);

                    return true;
                } else {
                    System.out.println("Login failed: Incorrect password");
                    return false;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    public void logout(UserEntity entity) {
        if (this.dataProvider == null) {
            throw new RuntimeException("DataProvider is null");
        }
        try {
            final String sql = "Select * From users Where username = ?";
            this.dataProvider.open();
            PreparedStatement preparedStatement = this.dataProvider.prepareStatement(sql);
            preparedStatement.setString(1, entity.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    System.out.println("Logout success");
                    final String sql1 = "DELETE FROM login_history WHERE username = ?";
                    PreparedStatement preparedStatement1 = this.dataProvider.prepareStatement(sql1);
                    preparedStatement1.setString(1, entity.getUsername());
                    int result = preparedStatement1.executeUpdate();
                    if (result == 1) {
                        System.out.println("Delete login history success");
                    } else {
                        System.out.println("Delete login history failed");
                    }
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void addUser(UserEntity entity) {
        if (this.dataProvider == null) {
            throw new RuntimeException("DataProvider is null");
        }
        try {
            final String sql = "INSERT INTO users (username, password, first_name, last_name) VALUES (?, ?, ?, ?)";
            this.dataProvider.open();
            PreparedStatement preparedStatement = this.dataProvider.prepareStatement(sql);
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getPassword());
            preparedStatement.setString(3, entity.getFirstName());
            preparedStatement.setString(4, entity.getLastName());

            int result = preparedStatement.executeUpdate();
            if (result == 1) {
                System.out.println("Add user success");
            } else {
                System.out.println("Add user failed");
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                this.dataProvider.close();
            } catch (SQLException ex) {
                Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


}
